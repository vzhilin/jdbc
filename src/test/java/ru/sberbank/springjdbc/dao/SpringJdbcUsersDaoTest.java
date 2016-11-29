package ru.sberbank.springjdbc.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

import static org.junit.Assert.assertEquals;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

public class SpringJdbcUsersDaoTest {
    private EmbeddedDatabase db;

    @Before
    public void setUp() {
        // creates an H2 in-memory database populated from default scripts
        // classpath:schema.sql and classpath:data.sql
        db = new EmbeddedDatabaseBuilder()
                .setType(H2)
                .generateUniqueName(true)
                .addDefaultScripts()
                .build();
    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void testCreate() throws Exception {
        UsersDao dao = new SpringJdbcUsersDao(null, db, null, null);

        User user = new User();
        user.setFirstName("Vladimir");
        user.setLastName("Zhilin");
        Long id = dao.create(user);

        assertEquals(id, user.getId());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT ID, FIRST_NAME, LAST_NAME FROM USERS WHERE ID = ?", id);
        assertEquals(1, maps.size());
        Map<String, Object> map = maps.get(0);
        assertEquals("Vladimir", map.get("FIRST_NAME"));
        assertEquals("Zhilin", map.get("LAST_NAME"));
    }
}
