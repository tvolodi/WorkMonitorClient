package services

import auth.AuthManager
import auth.TokenResponse
//import auth.Tokens
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import utils.GlobalConfig
import java.io.File
import java.net.URLEncoder
import java.util.UUID

/*
    * Upload a file to the server
    * @param fileName: name of the file to upload
    * @return: status code of the response. 0 - OK. 1 - Error
    *
 */
@OptIn(InternalAPI::class)
suspend fun uploadFile(fileName: String): Int {

    // 1. Try to upload

    val sendResult = sendFile(fileName)

    if (sendResult == -401) {
        // 2. If 401 - refresh token
        AuthManager.refreshToken()
        // 3. Try to upload with new token

        var sendResultTry2 = sendFile(fileName)

        // TODO: put exception here
        return sendResultTry2

    } else if (sendResult == 0) {
        return 0
    } else {
        return -1
    }
}

private suspend fun sendFile(fileName: String): Int {
    val client = HttpClient(CIO){

        headers {
            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
        }
        // TODO check if this process refresh token itself
//        install(Auth){
//            bearer {
//                loadTokens {
//                    BearerTokens(Tokens.accessToken, Tokens.refreshToken)
//                }
//            }
//        }
        install(ContentNegotiation){
            json()
        }
    }

    val response = client.post("https://localhost:5218/api/UploadFile") {
        headers {
            append(HttpHeaders.ContentType, "multipart/form-data")
            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
        }
        setBody(MultiPartFormDataContent(
            formData {
                // Section name should be the same as parameter in ASP.Net controller => List<IFormFile> files (!)
                append("files", File(fileName).readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "file; filename=$fileName")
                })
            }
        ))
    }

    when(response.status){
        HttpStatusCode.OK -> {
            val file = File(fileName)
            file.delete()
            return 0
        }
        HttpStatusCode.Unauthorized -> {
            return -401
        }
        else -> {
            return -1
        }
    }
}
