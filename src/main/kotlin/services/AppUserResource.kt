//package services
//
//import auth.AuthManager
//import entity_models.AppUser
//import io.ktor.client.*
//import io.ktor.client.engine.cio.*
//import io.ktor.client.plugins.*
//import io.ktor.client.plugins.resources.*
//import io.ktor.client.plugins.resources.Resources
//import io.ktor.client.request.*
//import io.ktor.client.call.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.client.statement.*
//import io.ktor.http.*
//import io.ktor.resources.*
//import io.ktor.serialization.kotlinx.json.*
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.encodeToString
//
//
//@Resource("/api/AppUser")
//class AppUserResource(){
//    @Resource("{id}")
//    class Id(val parent: AppUserResource = AppUserResource(), val id: Long )
//
//}
//
//
//private val httpClient = HttpClient(CIO){
////        install(Resources)
//        defaultRequest {
//            host = readConfigValue("app_server_host")
//            port = readConfigValue("app_server_port").toInt()
//            headers {
//                append(HttpHeaders.ContentType, "application/json")
////                append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
//            }
//            url { protocol = URLProtocol.HTTP }
//        }
//        install(ContentNegotiation){
//            json()
//        }
//    }
//
////private val httpClient = HttpClient(CIO){
////    install(Resources)
////    defaultRequest {
////        host = GlobalConfig.appConfig["app_server_host"].toString()
////        port = GlobalConfig.appConfig["app_server_port"].toString().toInt()
////        headers {
////            append(HttpHeaders.ContentType, "application/json")
////            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
////        }
////        url { protocol = URLProtocol.HTTP }
////    }
////    install(ContentNegotiation){
////        json()
////    }
////}
//
//
//
//suspend fun getAppUserList(filter: GetRequest): List<AppUser> {
//    val responseResult: MutableList<AppUser> = mutableListOf<AppUser>()
//
//    try {
//        val jsonBody =Json.encodeToString(filter)
//
//        var response = httpClient.request() {
//            method = HttpMethod.Post
//            url {
////                host = GlobalConfig.appConfig["app_server_host"].toString()
////                port = GlobalConfig.appConfig["app_server_port"].toString().toInt()
////                protocol = URLProtocol.HTTP
//                encodedPath = "/api/AppUser/get"
//            }
//            headers {
////                append(HttpHeaders.ContentType, "application/json")
//                append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
//            }
//
//
//            setBody(jsonBody)
//        }
//        val responseStatus = response.status
//        when(responseStatus) {
//
//            HttpStatusCode.OK -> {
//                println(response.bodyAsText())
//                val appUsers = response.body<List<AppUser>>()
//
//                responseResult.addAll(appUsers)
//            }
//
//            HttpStatusCode.Unauthorized -> {
//
//                AuthManager.refreshToken().also {
//                    response = httpClient.request() {
//                        method = HttpMethod.Post
//                        headers {
//                            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
//                        }
//                        url {
//                            encodedPath = "/api/AppUser"
//                        }
//                        setBody(jsonBody)
//                    }
//
//
//                    responseResult.addAll(response.body())
//                }
//
////                response = httpClient.get() {
////                    headers {
////                        append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
////                    }
////                    url {
////                        encodedPath = "/api/AppUser"
////                    }
////                }
//            }
//            else -> {
//                throw Exception("Error getting AppUser list")
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        throw Exception("Error getting AppUser list")
//    }
//
//    return responseResult
//}
//
//suspend fun insertAppUser(appUser: entity_models.AppUser): AppUser {
//    return httpClient.post(appUser){
//        url {
//            host = GlobalConfig.appConfig["app_server_host"].toString()
//            port = GlobalConfig.appConfig["app_server_port"].toString().toInt()
//            protocol = URLProtocol.HTTP
//            encodedPath = "/api/AppUser/new"
//        }
//        headers {
//            append(HttpHeaders.ContentType, "application/json")
//            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
//        }
//        setBody(appUser)
//    }.body()
//}
