package data

import org.ktorm.database.Database

object Connection {
    private val MYSQL_HOST = "localhost"
    private val MYSQL_PORT = "3306"
    private val MYSQL_DATABASE_NAME = "tbca"
    private val MYSQL_USER = "root"
    private val MYSQL_PASSWORD = ""

    val database = Database.connect(
        "jdbc:mysql://$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DATABASE_NAME",
        user = MYSQL_USER,
        password = MYSQL_PASSWORD
    )
}