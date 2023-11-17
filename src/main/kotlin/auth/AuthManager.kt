package auth

import com.auth0.jwt.JWT
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.GlobalConfig
import java.awt.Desktop
import java.io.File
import java.lang.RuntimeException
import java.net.URI
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

object AuthManager {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val callbackJob = MutableStateFlow<Job?>(null)

    val isLoggingIn = callbackJob.map {it?.isActive == true}

    private val _userId = MutableStateFlow<String?>(null)
    val userId = _userId.asStateFlow()

    fun authenticateUser() {
        val job = coroutineScope.launch {
            try {
                val verifier = createVerifier()
                val challenge = createChallenge(verifier)
                val url = createLoginUrl(
                    domain = GlobalConfig.authConfig["auth_domain"].toString(),
                    clientId = GlobalConfig.authConfig["auth_client_id"].toString(),
                    redirectUri = GlobalConfig.authConfig["auth_redirect_uri"].toString(),
                    scope = GlobalConfig.authConfig["auth_scope"].toString(),
                    challenge = challenge,
                    audience = GlobalConfig.authConfig["auth_audience"].toString(),
                )

                println("Launching URL: $url")

                withContext(Dispatchers.IO) {
                    Desktop.getDesktop().browse(URI(url))
                }

                val code = waitForCallback()

                getToken(
//                    domain = domain,
//                    clientId = clientId,
                    verifier = verifier,
                    code = code,
//                    redirectUri = redirectUri,
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        callbackJob.value = job
        job.invokeOnCompletion { callbackJob.value = null }
    }

    private fun createLoginUrl(
        domain: String,
        clientId: String,
        redirectUri: String,
        scope: String,
        challenge: String,
        audience: String,
    ): String {
        val encodedRedirectUri = URLEncoder.encode(redirectUri, Charsets.UTF_8)
        val encodedScope = URLEncoder.encode(scope, Charsets.UTF_8)
        val encodedAudience = URLEncoder.encode(audience, Charsets.UTF_8)
        val result = "https://$domain/authorize?response_type=code&code_challange=$challenge" +
                "&code_challenge_method=S256&client_id=$clientId&redirect_uri=$encodedRedirectUri" +
                "&scope=$encodedScope&audience=$encodedAudience"
        return result
    }

    private fun createVerifier(): String {
        val secRand = SecureRandom()
        val code = ByteArray(32)
        secRand.nextBytes(code)
        val result = Base64.getUrlEncoder().withoutPadding().encodeToString(code)
        return  result
    }

    fun cancelLogin() {
        callbackJob.value?.cancel()
        callbackJob.value = null
    }

    private fun extractUserId(token: String): String {
        val decodedJwt = JWT.decode(token)
        return decodedJwt.getClaim("sub").asString()
    }

    private suspend fun waitForCallback(): String {
        var server: NettyApplicationEngine? = null

        val code = suspendCancellableCoroutine<String> {continuation ->
            server = embeddedServer(Netty, port=5789){
                routing {
                    get("/callback"){
                        val code = call.parameters["code"] ?: throw RuntimeException("Response with no code")
                        call.respondText("OK")
                        continuation.resume(code)
                }
                }
            }.start(wait = false)
        }

        coroutineScope.launch {
            server!!.stop(1, 5, TimeUnit.SECONDS)
        }

        println("Got code ")
        return code
    }

    @OptIn(InternalAPI::class)
    private suspend fun getToken(
//        domain: String,
//        clientId:String,
        verifier: String,
        code: String,
//        redirectUri: String,
    ) {
        val encodedRedirectUri = URLEncoder.encode(GlobalConfig.authConfig["auth_redirect_uri"], Charsets.UTF_8)

        val client = HttpClient{
            install(ContentNegotiation){
                json()
            }
        }

        val response = client.post{
            url { protocol = URLProtocol.HTTPS; host = GlobalConfig.authConfig["auth_domain"].toString(); encodedPath = "/oauth/token" }
            headers { append("content-type", "application/x-www-form-urlencoded") }
            body = "grant_type=authorization_code&client_id=${GlobalConfig.authConfig["auth_client_id"]}&code_verifier=$verifier" +
                    "&code=$code&redirect_uri=$encodedRedirectUri"
        }.body<TokenResponse>()

        // Debug
        println("response: $response")

        saveTokens(response)
    }

    suspend fun refreshToken(
    ){
        val client = HttpClient{
            install(ContentNegotiation){
                json()
            }
        }

        val response = client.post{

            var domain = GlobalConfig.authConfig["auth_domain"].toString()
            var clientId = GlobalConfig.authConfig["auth_client_id"].toString()
            var refreshToken = GlobalConfig.tokens["refreshToken"].toString()

            url { protocol = URLProtocol.HTTPS; host = domain; encodedPath = "/oauth/token" }
            headers { append("content-type", "application/x-www-form-urlencoded") }

            setBody("grant_type=refresh_token" +
                    "&client_id=${clientId}" +
                    "&refresh_token=${refreshToken}")
        }.body<TokenResponse>()

        saveTokens(response)

        // Debug
        println("response.token: ${response.accessToken}")
    }

    private fun createChallenge(verifier: String): String {
        val bytes: ByteArray = verifier.toByteArray(Charsets.US_ASCII)
        val md = MessageDigest.getInstance("SHA-256")
        md.update(bytes, 0, bytes.size)
        val digest = md.digest()
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(digest)
    }

    private val json = Json { encodeDefaults = true }

    private fun saveTokens(response: TokenResponse) {

        GlobalConfig.tokens["accessToken"] = response.accessToken
        GlobalConfig.tokens["refreshToken"] = response.refreshToken
        GlobalConfig.tokens["idToken"] = response.idToken

        JWT.decode(response.accessToken).claims.forEach{
            when(it.key){
                "sub" -> GlobalConfig.tokens["userAuthId"] = it.value.toString()
                "https://www.vt-ptm.org/email" -> GlobalConfig.tokens["email"] = it.value.toString()
            }
        }

        val jsonString = Json.encodeToString(GlobalConfig.tokens)

        val userHome = System.getProperty("user.home")
        val tokenFile = File(userHome, "tokens.json")

        tokenFile.writeText(jsonString)
    }

    fun restoreTokens(){

        val userHome = System.getProperty("user.home")
//        val tokensFilePath = "$userHome/tokens.json"

        val tokenFile = File(userHome, "tokens.json")
        if(!tokenFile.exists()) return

        val jsonString = tokenFile.readText()
        if(jsonString == "") return
        val tokens = Json.decodeFromString<MutableMap<String, String>>(jsonString)

        GlobalConfig.tokens = tokens

    }
}