package ru.sberbank.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

public class Application {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:~/test";   // Database specific url.
        String login = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, login, password)) {
            connection.setAutoCommit(false);

//            User user = new User();
//            user.setFirstName("Vladimir");
//            user.setLastName("Zhilin");
//            Calendar birthday = Calendar.getInstance();
//            birthday.set(1981, 7, 31);
//            user.setBirthday(birthday.getTime());
//            user.setAccounts(Arrays.asList("111", "222"));
//
//            UsersDao usersDao = new RdbUsersDao(connection);
//            Long id = usersDao.create(user);
//            System.out.println("Id = " + id);

            UsersDao usersDao = new RdbUsersDao(connection);
            User user = usersDao.read(2L);
            System.out.println(user);

            connection.commit();
        }

        System.out.println("Good bye!");
    }
}
