package ru.sberbank.jdbc.delete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:~/test";   // Database specific url.
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("DELETE USERS WHERE ID = ?");) {
            statement.setInt(1, 1);
            int rowsAffected = statement.executeUpdate();
            System.out.println(rowsAffected);
        }
    }
}
