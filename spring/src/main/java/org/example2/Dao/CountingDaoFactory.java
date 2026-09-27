package org.example2.Dao;

import java.beans.BeanProperty;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class CountingDaoFactory {

    @Bean
    public UserDao userDao() throws ClassNotFoundException, SQLException{
        return new UserDao(connectionMaker());
    }

    @Bean 
    public ConnectionMaker connectionMaker(){
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean 
    public ConnectionMaker realConnectionMaker(){
        return new DConnectionMaker();
    }
    
}
