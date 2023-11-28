//package utils
//
//import kotlinx.serialization.*
//import kotlinx.serialization.json.Json
//import java.io.File
//
//
//@Serializable
//object GlobalConfig_ {
//
//    @Serializable
//    var authConfig: MutableMap<String, String> = mutableMapOf()
//
//    @Serializable
////    @SerialName("tokens")
//    var tokens: MutableMap<String, String> = mutableMapOf()
//
//    @Serializable
////    @SerialName("app_config")
//    var appConfig: MutableMap<String, String> = mutableMapOf()
//
//    @Serializable
//    var projectTaskAttr: MutableMap<String, String> = mutableMapOf()
//
//    var areTokensOk: Boolean = false
//
////    fun readAuthConfig():  {
////        val userHome = System.getProperty("user.home")
////        val file = File(userHome, "wm_auth_config.json")
////
////        if(!file.exists()) return
////
////        val jsonString = file.readText()
////        if(jsonString == "") return
////        val configMap = Json.decodeFromString<MutableMap<String, String>>(jsonString)
////
////        authConfig = configMap
////    }
//}
//
//    fun readGlobalConfig() {
//
//        val userHome = System.getProperty("user.home")
//        val fileName = "wm_config.json"
//
//        val file = File(userHome, fileName)
//
//        //var configMap = mutableMapOf<String, String>()
//        if(file.exists()){
//            val jsonString = file.readText()
//            if(jsonString.count() > 0 ){
//                val globalConfig = Json.decodeFromString<GlobalConfig>(jsonString)
//
//                GlobalConfig.authConfig = globalConfig.authConfig
//                GlobalConfig.tokens = globalConfig.tokens
//                GlobalConfig.areTokensOk = true
//                GlobalConfig.appConfig = globalConfig.appConfig
//                GlobalConfig.projectTaskAttr = globalConfig.projectTaskAttr
//            }
//        }
//
//                // Default values
//
//        GlobalConfig.authConfig["auth_domain"] = "dev-e3r1z7qh4iztrv5x.us.auth0.com"
//        GlobalConfig.authConfig["auth_client_id"] = "LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW"
//        GlobalConfig.authConfig["auth_scope"] = "openid profile email offline_access"
//        GlobalConfig.authConfig["auth_response_type"] = "code"
//        GlobalConfig.authConfig["auth_redirect_uri"] = "http://localhost:5789/callback"
//        GlobalConfig.authConfig["auth_audience"] = "https://www.vt-ptm.org/wm-api"
//
//        GlobalConfig.appConfig["app_server_host"] = "localhost"
//        GlobalConfig.appConfig["app_server_port"] = "5218"
//        GlobalConfig.appConfig["app_server_protocol"] = "https"
//
//        GlobalConfig.projectTaskAttr["projectName"] = "project1"
//        GlobalConfig.projectTaskAttr["taskName"] = "task1"
//
//        saveGlobalConfig()
//    }
//
//fun saveGlobalConfig() {
//
//    val jsonString = Json.encodeToString(GlobalConfig)
//    val userHome = System.getProperty("user.home")
//    val file = File(userHome, "wm_config.json")
//    file.writeText(jsonString)
//}
//
//
