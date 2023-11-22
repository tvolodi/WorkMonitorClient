package services

import entity_models.AppUser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.serialization.kotlinx.json.*
import utils.GlobalConfig

@Resource("/api/AppUser")
class AppUserResource(){
    @Resource("{id}")
    class Id(val parent: AppUserResource = AppUserResource(), val id: Long )

}

private val httpClient = HttpClient(CIO){
    install(Resources)
    defaultRequest {
        host = GlobalConfig.appConfig["app_server_host"].toString()
        port = GlobalConfig.appConfig["app_server_port"].toString().toInt()
        headers {
            append(HttpHeaders.ContentType, "application/json")
            append(HttpHeaders.Authorization, "Bearer ${GlobalConfig.tokens["accessToken"]}")
        }
        url { protocol = URLProtocol.HTTP }
    }
    install(ContentNegotiation){
        json()
    }
}

suspend fun getAppUserList(): List<AppUser> {
    var responseResult: MutableList<AppUser> = mutableListOf<AppUser>()
    try {
        val response = httpClient.get() {
            url {
                encodedPath = "/api/AppUser"
            }
        }.body<List<AppUser>>()
        responseResult.addAll(response)
    } catch (e: Exception) {
        println(e)
    }


    return responseResult
}

suspend fun insertAppUser(appUser: entity_models.AppUser): AppUser {
    return httpClient.post(appUser){
        setBody(appUser)
    }.body()
}
