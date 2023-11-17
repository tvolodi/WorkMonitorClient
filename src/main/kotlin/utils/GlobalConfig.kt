package utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

data object GlobalConfig {

    @Serializable
    var authConfig: MutableMap<String, String> = mutableMapOf()

    @Serializable
    var tokens: MutableMap<String, String> = mutableMapOf()

    fun restoreAuthConfig() {
        val userHome = System.getProperty("user.home")
        val tokenFile = File(userHome, "wm_auth_config.json")

        if(!tokenFile.exists()) return

        val jsonString = tokenFile.readText()
        if(jsonString == "") return
        val configMap = Json.decodeFromString<MutableMap<String, String>>(jsonString)

        authConfig = configMap
    }


}