package org.example2.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    private static final String DB_URL = "jdbc:h2:./springbook";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public UserDao() throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(100) PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "password VARCHAR(100) NOT NULL)");
        }
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws  ClassNotFoundException, SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();
        return user;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        // Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        return c;
    }

}
