package services

import java.io.File
import java.sql.DriverManager


private val file = File(System.getProperty("user.home"), "wm_sqlite.db")
private val connection = DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")

fun checkAndCreateDatabase() {
    val userHome = System.getProperty("user.home")
    val file = File(userHome, "wm_sqlite.db")
//    if (file.exists()) return


    val statement = connection.createStatement()

    val createConfigResult = statement.executeUpdate("create table if not exists sys_config (" +
            "id integer primary key autoincrement, " +
            "code text, " +
            "name text, " +
            "value text )")

    statement.executeUpdate("create table if not exists project_task (id integer primary key autoincrement, project_name text, task_name text)")

    val resultSet = statement.executeQuery("select count(*) from sys_config")
    while (resultSet.next()) {
        val count = resultSet.getInt(1)
        if (count > 0) return
    }

    // Store default values
    try {
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_domain', 'Auth Domain', 'dev-e3r1z7qh4iztrv5x.us.auth0.com')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_client_id', 'Auth ClientId', 'LKi23IJ0wTFZ3aAV0dwQUyRvFKdsA4zW' )")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_scope', 'Auth Scope', 'openid profile email offline_access')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_response_type', 'Auth Response Type', 'code')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_redirect_uri', 'Auth Redirect Uri', 'http://localhost:5789/callback')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('auth_audience', 'Auth Audience', 'https://www.vt-ptm.org/wm-api')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('app_server_host', 'App Server Host', 'localhost')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('app_server_port', 'App Server Port', '5218')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('app_server_protocol', 'App Server Protocol', 'https')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('project_name', 'Project Name', 'project1')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('task_name', 'Task Name', 'task1')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('accessToken', 'Access token', '')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('refreshToken', 'Refresh Token', '')")
        statement.executeUpdate("insert into sys_config (code, name, value) values ('idToken', 'idToken', '')")

    } catch (e: Exception) {
        println(e.stackTrace.toString())
    }


}

fun readConfigValue(code: String): String {
//    val connection = DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")
    val statement = connection.prepareStatement("select value from sys_config where code = ?")
    statement.setString(1, code)

    val resultSet = statement.executeQuery()
    while (resultSet.next()) {
        return resultSet.getString("value")
    }
    return ""
}

fun saveConfigValue(code: String, value: String) {
//    val connection = DriverManager.getConnection("jdbc:sqlite:${file.absolutePath}")
    val statement = connection.prepareStatement("update sys_config set value = ? where code = ?")
    statement.setString(1, value)
    statement.setString(2, code)

    try{
        statement.executeUpdate()
//        connection.commit()
    } catch (e: Exception){
        println(e.stackTrace.toString())
    }

}
