package utils

import kotlinx.serialization.Serializable
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

        if(!file.exists()) return mutableMapOf()

        val jsonString = file.readText()
        if(jsonString == "") return mutableMapOf()
        val configMap = Json.decodeFromString<MutableMap<String, String>>(jsonString)
        return configMap
    }


}