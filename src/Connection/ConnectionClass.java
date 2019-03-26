package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass
{
    public static Connection getConnection() throws SQLException
    {
        String dbName = "ThePieGuys";
        String userName = "root";
        String password = "";

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName,userName,password);
        return connection;
    }
}
