package org.example2.Dao;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class CountingDaoFactory {

    @Bean(initMethod = "initialize")
    public UserDao userDao() throws ClassNotFoundException, SQLException{
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean 
    public ConnectionMaker connectionMaker(){
        CountingConnectionMaker ccm = new CountingConnectionMaker();
        ccm.setRealConnectionMaker(realConnectionMaker());
        return ccm;
    }

    @Bean 
    public ConnectionMaker realConnectionMaker(){
        return new DConnectionMaker();
    }
    
}
