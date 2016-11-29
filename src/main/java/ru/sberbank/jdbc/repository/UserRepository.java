package ru.sberbank.jdbc.repository;

import java.util.List;

import ru.sberbank.jdbc.User;

public interface UserRepository {
    void addUser(User user);

    void removeUser(User user);

    void updateUser(User user);

    List<User> query(UserSpecification specification);
}
