package ru.sberbank.jdbc.read;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:~/test";   // Database specific url.
        String user = "sa";
        String password = "";

        String sql = "SELECT FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY FROM USERS WHERE ID = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, 1L);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("FIRST_NAME");
                    String lastName = resultSet.getString("LAST_NAME");
                    System.out.println("FIRST_NAME = " + firstName + ", LAST_NAME = " + lastName);
                }
            }
        }
    }
}
