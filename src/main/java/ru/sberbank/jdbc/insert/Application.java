package ru.sberbank.jdbc.insert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class Application {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:~/test";   // Database specific url.
        String user = "sa";
        String password = "";

        String sql = "INSERT INTO USERS(ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY) VALUES(USR_SEQ.NEXTVAL, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "Vladimir");
            statement.setString(2, "");
            statement.setString(3, "Zhilin");
            Calendar birthday = Calendar.getInstance();
            birthday.set(1981, 7, 31);
            statement.setDate(4, new java.sql.Date(birthday.getTimeInMillis()));

            int rowsAffected = statement.executeUpdate();

            System.out.println(rowsAffected);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                while (generatedKeys.next()) {
                    Integer id = generatedKeys.getInt(1);
                    System.out.println(id);
                }
            }

        }
    }
}
