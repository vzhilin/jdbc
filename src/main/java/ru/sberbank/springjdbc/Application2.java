package ru.sberbank.springjdbc;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.sberbank.jdbc.User;
import ru.sberbank.springjdbc.controller.SomeController;

@EnableTransactionManagement
@SpringBootApplication
public class Application2 {
    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = SpringApplication.run(Application2.class, args);

        SomeController controller = applicationContext.getBean(SomeController.class);

        User ivanov = new User();
        ivanov.setFirstName("Ivan");
        ivanov.setLastName("Ivanov");

        InputStream avatarInputStream = new FileInputStream("c:/111/avatar.jpg");
        byte[] avatar = IOUtils.toByteArray(avatarInputStream);

        controller.addUser(ivanov, avatar);

        System.out.println("User with id=" + ivanov.getId() + " created");
    }
}
