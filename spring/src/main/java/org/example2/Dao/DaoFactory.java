package org.example2.Dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

// Java-based configuration example kept for comparison with applicationContext.xml.
@Configuration
public class DaoFactory {
    
    @Bean(initMethod = "initialize")
    public UserDao userDao() throws SQLException {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    // public AccountDao userDao() throws SQLException {
    //     ConnectionMaker connectionMaker = new DConnectionMaker();
    //     UserDao userDao = new UserDao(connectionMaker);
    //     return userDao;
    // }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUrl("jdbc:h2:./springbook");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    
}
