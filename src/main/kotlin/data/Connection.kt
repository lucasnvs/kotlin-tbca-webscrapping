package data

import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
object Connection {
    private val MYSQL_HOST = "localhost"
    private val MYSQL_PORT = "3306"
    private val MYSQL_DATABASE_NAME = "tbca"
    private val MYSQL_USER = "root"
    private val MYSQL_PASSWORD = ""

    private val JDBC_URL = "jdbc:mysql://$MYSQL_HOST:$MYSQL_PORT/$MYSQL_DATABASE_NAME"

    val dataSource = HikariDataSource().apply {
        jdbcUrl = JDBC_URL
        username = MYSQL_USER
        password = MYSQL_PASSWORD
        minimumIdle = 8
        maximumPoolSize = 30
        validationTimeout = 300
    }

    val database = Database.connect(dataSource)
}