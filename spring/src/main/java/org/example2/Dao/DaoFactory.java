package org.example2.Dao;

import java.sql.SQLException;

public class DaoFactory {
    
    public UserDao userDao() throws ClassNotFoundException, SQLException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

    // public AccountDao userDao() throws ClassNotFoundException, SQLException {
    //     ConnectionMaker connectionMaker = new DConnectionMaker();
    //     UserDao userDao = new UserDao(connectionMaker);
    //     return userDao;
    // }

    // public ConnectionMaker connectionMaker(){
    //     return new DConnectionMaker();
    // }
}
