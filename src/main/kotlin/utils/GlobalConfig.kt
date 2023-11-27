package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

data object GlobalConfig {

    @Serializable
    var authConfig: MutableMap<String, String> = mutableMapOf()

    @Serializable
    var tokens: MutableMap<String, String> = mutableMapOf()

    @Serializable
    var appConfig: MutableMap<String, String> = mutableMapOf()

//    fun readAuthConfig():  {
//        val userHome = System.getProperty("user.home")
//        val file = File(userHome, "wm_auth_config.json")
//
//        if(!file.exists()) return
//
//        val jsonString = file.readText()
//        if(jsonString == "") return
//        val configMap = Json.decodeFromString<MutableMap<String, String>>(jsonString)
//
//        authConfig = configMap
//    }

    fun readConfig(fileName: String): MutableMap<String, String> {

        val userHome = System.getProperty("user.home")
        val file = File(userHome, fileName)

        var configMap = mutableMapOf<String, String>()
        if(file.exists()){
            val jsonString = file.readText()
            if(jsonString == "") return mutableMapOf()

            configMap = Json.decodeFromString<MutableMap<String, String>>(jsonString)
            if(configMap["accessToken"]?.isNotEmpty() == true){
//                areTokenExist = true
            }
            return configMap
        }

        // There is no file, or it is empty then try default values
        when(fileName) {
            "wm_app_config.json" -> {
                configMap["app_server_host"] = "localhost"
                configMap["app_server_port"] = "5218"
                configMap["app_server_protocol"] = "http"

                saveConfig(configMap, "wm_app_config.json")
            }
            "wm_auth_config.json" -> {
                configMap["auth_domain"] = "dev-e3r1z7qh4iztrv5x.us.auth0.com"
                configMap["auth_client_id"] = "LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW"
//                    configMap["auth_client_secret"] = "wm_secret"
                configMap["auth_scope"] = "openid profile email offline_access"
                configMap["auth_response_type"] = "code"
                configMap["auth_redirect_uri"] = "http://localhost:5789/callback"
                configMap["auth_audience"] = "https://www.vt-ptm.org/wm-api"

                saveConfig(configMap, "wm_auth_config.json")
            }
        }

        return configMap
    }

    var areTokenExist = false
}

private fun saveConfig(configMap: MutableMap<String, String>, fileName: String) {

    val jsonString = Json.encodeToString(configMap)
    val userHome = System.getProperty("user.home")
    val file = File(userHome, fileName)
    file.writeText(jsonString)
}

fun saveGlobalConfig() {
    saveConfig(GlobalConfig.authConfig, "wm_auth_config.json")
    saveConfig(GlobalConfig.appConfig, "wm_app_config.json")
}

