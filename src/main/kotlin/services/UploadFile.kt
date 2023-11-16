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

    val encodedUri = URLEncoder.encode("https://localhost:5218", Charsets.UTF_8)

    val client = HttpClient(CIO){

        headers {
            append(HttpHeaders.Authorization, "Bearer ${Tokens.accessToken}")
        }
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

    val fileNameTmp = "2023-11-15T14-51-14.090359400.png"

    val response = client.post("http://localhost:5218/api/UploadFile"){
        headers {
            append(HttpHeaders.ContentType, "multipart/form-data")
            append(HttpHeaders.Authorization, "Bearer ${Tokens.accessToken}")
        }
        body = MultiPartFormDataContent(
            formData {
                // Section name should be the same as parameter in ASP.Net controller => List<IFormFile> files (!)
                append("files", File(fileName).readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "file; filename=$fileName")
                })
            }
        )}

    println("response.status: ${response.status}")
}