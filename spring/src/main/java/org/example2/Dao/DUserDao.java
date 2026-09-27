package org.example2.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DUserDao extends UserDao {
    public DUserDao() throws ClassNotFoundException, SQLException {
        super();
    }

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection("jdbc:h2:./springbook-d", "sa", "");
    }
}
