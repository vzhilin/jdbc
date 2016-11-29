package ru.sberbank.jdbc.repository;

import java.sql.Connection;
import java.util.List;

import ru.sberbank.jdbc.User;

public class RdbUserRepository implements UserRepository {
    private final Connection connection;

    public RdbUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUser(User user) {
        // TODO
    }

    @Override
    public void removeUser(User user) {
        // TODO
    }

    @Override
    public void updateUser(User user) {
        // TODO
    }

    @Override
    public List<User> query(UserSpecification specification) {
        // TODO
        return null;
    }
}
