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

//    val response = client.submitFormWithBinaryData(
//        url = "http://localhost:5218/api/UploadFile",
//        formData = formData {
//            val fileUuidName = UUID.randomUUID().toString()
////            append("file", File(fileUuidName))
//            val fileNameTmp = "2023-11-15T12-49-47.225004500_2.png"
//            append("description", "screenshot")
//            append("image", File(fileNameTmp).readBytes(), Headers.build {
//                append(HttpHeaders.ContentType, "image/png")
//                append(HttpHeaders.ContentDisposition, "file; filename=$fileNameTmp")
//            })
//        }
//    )

    val fileNameTmp = "2023-11-15T14-51-14.090359400.png"

    val response = client.post("http://localhost:5218/api/UploadFile"){
        headers {
            append(HttpHeaders.ContentType, "multipart/form-data")
            append(HttpHeaders.Authorization, "Bearer ${Tokens.accessToken}")
        }
        body = MultiPartFormDataContent(
            formData {
                append("files", File(fileName).readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "file; filename=$fileName")
                })
            }
        )}


    println("response.status: ${response.status}")

//    try {
//        val testResponse = client.get {
//            url { protocol = URLProtocol.HTTP; host = "localhost"; encodedPath = "/WeatherForecast"; port = 5218 }
//            headers {
//                append("content-type", "application/json")
//                append("Authorization", "Bearer ${Tokens.accessToken}")
//            }
//        }
//        val responseBody: ByteArray = testResponse.body()
//        val file = File("testResponse.json")
//        file.writeBytes(responseBody)
//
////        println("Test response: ${testResponse.body<Any>()}")
//
//    } catch (e: Exception) {
//        println("Exception: ${e.printStackTrace()}")
//    }


//    println( "response.status ${response.status}" )
}