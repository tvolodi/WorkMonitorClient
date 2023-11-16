package services

import java.sql.DriverManager

var sqliteConnection = DriverManager.getConnection("jdbc:sqlite:./database.db")

fun createTables(){

    val statement = sqliteConnection.createStatement()
    statement.execute("""
        CREATE TABLE IF NOT EXISTS reqQueue (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            reqBody TEXT NOT NULL,
            filename TEXT NOT NULL,
            created_at TEXT NOT NULL,            
        );
    """.trimIndent())
}

fun registerRequest(reqBody: String){
    val statement = sqliteConnection.createStatement()
    statement.execute("""
        INSERT INTO reqQueue (reqBody, created_at)
        VALUES ('$reqBody', datetime('now'));
    """.trimIndent())
}

fun getRequests(): List<Request>{
    val statement = sqliteConnection.createStatement()
    val resultSet = statement.executeQuery("""
        SELECT * FROM reqQueue ORDER BY id;
    """.trimIndent())

    val requests = mutableListOf<Request>()

    while(resultSet.next()){
        val id = resultSet.getInt("id")
        val reqBody = resultSet.getString("reqBody")
        val filename = resultSet.getString("filename")
        val createdAt = resultSet.getString("created_at")

        requests.add(Request(id, reqBody, filename, createdAt))
    }

    return requests
}

fun deleteRequest(id: Int) {
    val statement = sqliteConnection.createStatement()
    statement.execute(
        """
        DELETE FROM reqQueue WHERE id = $id;
    """.trimIndent()
    )
}

data class Request(
    val id: Int,
    val reqBody: String,
    val filename: String,
    val createdAt: String,
)