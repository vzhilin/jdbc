package ru.sberbank.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

public class RdbUsersDao implements UsersDao {
    private final Connection connection;

    public RdbUsersDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveAvatar(User user, byte[] avatar) {

    }

    @Override
    public byte[] loadAvatar(User user) {
        return new byte[0];
    }

    @Override
    public List<User> findByAccount(String account) {
        // TODO

        return null;
    }

    @Override
    public int create(User... users) {
        return 0;
    }

    @Override
    public Long create(User user) {
        String sql = "INSERT INTO USERS(ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY) VALUES(USR_SEQ.NEXTVAL, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getMiddleName());
            statement.setString(3, user.getLastName());
            Date birthday = user.getBirthday();
            if (birthday == null) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, new java.sql.Date(birthday.getTime()));
            }

            if (statement.executeUpdate() == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        user.setId(id);

                        return id;
                    } else {
                        throw new RuntimeException("Unable to get user id");
                    }
                }
            } else {
                throw new RuntimeException("User not created");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User read(Long id) {
        String sql = "SELECT FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY FROM USERS WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(id);
                user.setFirstName(resultSet.getString("FIRST_NAME"));
                user.setMiddleName(resultSet.getString("MIDDLE_NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                java.sql.Date birthday = resultSet.getDate("BIRTHDAY");
                user.setBirthday(birthday == null ? null : new Date(birthday.getTime()));

                return user;
            } else {
                throw new RuntimeException("User with id = " + id + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        // TODO
    }

    @Override
    public void delete(User user) {
        // TODO
        user.setId(null);
    }
}

