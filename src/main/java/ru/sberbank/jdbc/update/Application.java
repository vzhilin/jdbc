package ru.sberbank.jdbc.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:~/test";   // Database specific url.
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();) {
            int rowsAffected = statement.executeUpdate("UPDATE USERS SET MIDDLE_NAME = 'Igorevich' WHERE ID = 2");
            System.out.println(rowsAffected);
        }
    }
}
