package dbdbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/dbdbd?useUnicode=true&characterEncoding=UTF-8";
    private static final String DB_USER = "DBDBDuser";
    private static final String DB_PASSWORD = "DBDBDpw";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

}