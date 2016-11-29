package ru.sberbank.springjdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AppConfig {
    /**
     * When using Spring’s JDBC layer, you obtain a data source from JNDI or you configure your own
     * with a connection pool implementation provided by a third party. Popular implementations are
     * Apache Jakarta Commons DBCP and C3P0.
     */
    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource("jdbc:h2:~/test", "sa", "");
    }

    /**
     * Instances of the JdbcTemplate class are thread safe once configured. This is important
     * because it means that you can configure a single instance of a JdbcTemplate and then
     * safely inject this shared reference into multiple DAOs (or repositories).
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public LobHandler lobHandler() {
        return new DefaultLobHandler();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
