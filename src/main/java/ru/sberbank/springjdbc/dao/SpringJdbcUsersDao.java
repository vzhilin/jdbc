package ru.sberbank.springjdbc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import ru.sberbank.UsersDao;
import ru.sberbank.jdbc.User;

@Component
public class SpringJdbcUsersDao implements UsersDao {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    private final LobHandler lobHandler;

    private final UserRowMapper userRowMapper = new UserRowMapper();

    private final PlatformTransactionManager transactionManager;

    @Autowired
    public SpringJdbcUsersDao(JdbcTemplate jdbcTemplate, DataSource dataSource, LobHandler lobHandler, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("USERS").usingGeneratedKeyColumns("ID");
        this.lobHandler = lobHandler;
        this.transactionManager = transactionManager;
    }

    @Override
    public List<User> findByAccount(String account) {
        return null;
    }

    @Override
    public Long create(User user) {
        String sql = "INSERT INTO USERS(ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY) VALUES(USR_SEQ.NEXTVAL, :firstName, :middleName, :lastName, :birthday)";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        Long id = (Long) keyHolder.getKey();
        user.setId(id);
        return id;
    }

    public void transactionalExample(User user) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
        try {
            create(user);
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
    }

//    @Override
//    public Long create(User user) {
//        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
//        Long id = (Long) simpleJdbcInsert.executeAndReturnKey(parameterSource);
//        user.setId(id);
//        return id;
//    }

    @Override
    public int create(final User... users) {
        String sql = "INSERT INTO USERS(ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY) VALUES(USR_SEQ.NEXTVAL, ?, ?, ?, ?)";
        BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                User user = users[i];
                preparedStatement.setString(1, user.getFirstName());
                preparedStatement.setString(2, user.getMiddleName());
                preparedStatement.setString(3, user.getLastName());
                Date birthday = user.getBirthday();
                if (birthday == null) {
                    preparedStatement.setNull(4, Types.DATE);
                } else {
                    preparedStatement.setDate(4, new java.sql.Date(birthday.getTime()));
                }
            }

            @Override
            public int getBatchSize() {
                return users.length;
            }
        };
        int[] affectedRows = jdbcTemplate.batchUpdate(sql, setter);
        return Arrays.stream(affectedRows).sum();
    }

    @Override
    public User read(Long id) {
        String sql = "SELECT ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, BIRTHDAY FROM USERS WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public void update(User transientObject) {

    }

    @Override
    public void delete(User persistentObject) {

    }

//    @Override
//    public void saveAvatar(User user, InputStream avatarBinaryStream, int avatarContentLength) {
//        String sql = "UPDATE USERS SET AVATAR = ? WHERE ID = ?";
//        AbstractLobCreatingPreparedStatementCallback callback = new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
//            @Override
//            protected void setValues(PreparedStatement preparedStatement, LobCreator lobCreator) throws SQLException, DataAccessException {
//                lobCreator.setBlobAsBinaryStream(preparedStatement, 1, avatarBinaryStream, avatarContentLength);
//                preparedStatement.setLong(2, user.getId());
//            }
//        };
//        jdbcTemplate.execute(sql, callback);
//    }

    @Override
    public void saveAvatar(User user, byte[] avatar) {
        String sql = "UPDATE USERS SET AVATAR = ? WHERE ID = ?";
        AbstractLobCreatingPreparedStatementCallback callback = new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement preparedStatement, LobCreator lobCreator) throws SQLException, DataAccessException {
                lobCreator.setBlobAsBytes(preparedStatement, 1, avatar);
                preparedStatement.setLong(2, user.getId());
            }
        };
        jdbcTemplate.execute(sql, callback);
    }

    @Override
    public byte[] loadAvatar(User user) {
        String sql = "SELECT AVATAR FROM USERS WHERE ID = ?";
        RowMapper<byte[]> rowMapper = new RowMapper<byte[]>() {
            @Override
            public byte[] mapRow(ResultSet resultSet, int i) throws SQLException {
                return lobHandler.getBlobAsBytes(resultSet, "AVATAR");
            }
        };
        return jdbcTemplate.queryForObject(sql, rowMapper, user.getId());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("ID"));
            user.setFirstName(resultSet.getString("FIRST_NAME"));
            user.setMiddleName(resultSet.getString("MIDDLE_NAME"));
            user.setLastName(resultSet.getString("LAST_NAME"));
            user.setBirthday(resultSet.getDate("BIRTHDAY"));
            return user;
        }
    }
}

