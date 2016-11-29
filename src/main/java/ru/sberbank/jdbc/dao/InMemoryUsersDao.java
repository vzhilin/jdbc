package ru.sberbank.jdbc.dao;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

public class InMemoryUsersDao implements UsersDao {
    private AtomicLong usrSeq = new AtomicLong();
    private ConcurrentMap<Long, User> users = new ConcurrentHashMap<>();


    @Override
    public void saveAvatar(User user, byte[] avatar) {

    }

    @Override
    public byte[] loadAvatar(User user) {
        return new byte[0];
    }

    @Override
    public List<User> findByAccount(String account) {
        return null;
    }

    @Override
    public int create(User... users) {
        return 0;
    }

    @Override
    public Long create(User user) {
        long id = usrSeq.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return id;
    }

    @Override
    public User read(Long id) {
        return null;
    }

    @Override
    public void update(User transientObject) {

    }

    @Override
    public void delete(User persistentObject) {

    }
}

