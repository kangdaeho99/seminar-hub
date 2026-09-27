package org.example2.Dao;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {
    
    @Bean(initMethod = "initialize")
    public UserDao userDao() throws ClassNotFoundException, SQLException {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    // public AccountDao userDao() throws ClassNotFoundException, SQLException {
    //     ConnectionMaker connectionMaker = new DConnectionMaker();
    //     UserDao userDao = new UserDao(connectionMaker);
    //     return userDao;
    // }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new DConnectionMaker();
    }
}
