package ru.sberbank.springjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileNotFoundException;

import ru.sberbank.UsersDao;

@EnableTransactionManagement
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        UsersDao usersDao = applicationContext.getBean(UsersDao.class);

//        User user = usersDao.read(2L);
//        System.out.println(user);

//        User ivanov = new User();
//        ivanov.setFirstName("Ivan");
//        ivanov.setLastName("Ivanov");
//
//        User petrov = new User();
//        petrov.setFirstName("Petr");
//        petrov.setLastName("Petrov");
//
//        User[] usersToCreate = {ivanov, petrov};
//        int count = usersDao.create(usersToCreate);
//        System.out.println(count + " users created");

//        User user = new User();
//        user.setFirstName("Sidor");
//        user.setLastName("Sidorov");
//        Long id = usersDao.create(user);
//        System.out.println("User with id=" + id + " created");
//
//        ((SpringJdbcUsersDao) usersDao).transactionalExample(user);

//        File avatarFile = new File("c:/111/avatar.jpg");
//        if (avatarFile.length() > Integer.MAX_VALUE) {
//            throw new RuntimeException("Unable to load heavy images");
//        }
//        InputStream avatarInputStream = new FileInputStream(avatarFile);
//        usersDao.saveAvatar(user, avatarInputStream, (int) avatarFile.length());

//        byte[] avatarBytes = usersDao.loadAvatar(user);
//        System.out.println(Arrays.toString(avatarBytes));
    }
}
