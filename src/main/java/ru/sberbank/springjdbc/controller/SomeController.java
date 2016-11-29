package ru.sberbank.springjdbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

@Controller
public class SomeController {
    private final UsersDao usersDao;

    @Autowired
    public SomeController(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Transactional
    public void addUser(User user, byte[] avatar) {
        usersDao.create(user);
        usersDao.saveAvatar(user, avatar);
    }
}
