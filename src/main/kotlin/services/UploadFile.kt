package services

import auth.TokenResponse
import auth.Tokens
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
import java.io.File
import java.net.URLEncoder
import java.util.UUID


@OptIn(InternalAPI::class)
suspend fun uploadFile(fileName: String) {

    val encodedRedirectUri = URLEncoder.encode("https://localhost:5218", Charsets.UTF_8)

    val client = HttpClient(CIO){
        install(Auth){
            bearer {
                loadTokens {
                    BearerTokens(Tokens.accessToken, Tokens.refreshToken)
                }
            }
        }
        install(ContentNegotiation){
            json()
        }
    }

//    client.post("https://localhost:5218/api/UploadFile")

    val response = client.submitFormWithBinaryData(
        url = "https://localhost:5218/api/UploadFile",
        formData = formData {
            val fileUuidName = UUID.randomUUID().toString()
//            append("file", File(fileUuidName))
            append("description", "screenshot")
            append("image", File(fileName).readBytes(), Headers.build {
                append(HttpHeaders.ContentType, "image/png")
                append(HttpHeaders.ContentDisposition, "file; filename=$fileUuidName")
            })
        }
    )
    println( "response.status ${response.status}" )
}