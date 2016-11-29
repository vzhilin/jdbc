package ru.sberbank;

import java.util.List;

import ru.sberbank.jdbc.User;

public interface UsersDao extends GenericDao<User, Long> {
    List<User> findByAccount(String account);

    int create(User... users);

//    void saveAvatar(User user, InputStream avatarBinaryStream, int avatarContentLength);
    void saveAvatar(User user, byte[] avatar);

    byte[] loadAvatar(User user);
}

