package org.example2.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection("jdbc:h2:./springbook", "sa", "");
    }

}
