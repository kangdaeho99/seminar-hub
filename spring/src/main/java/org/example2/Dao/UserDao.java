package org.example2.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

public class UserDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void initialize() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(100) PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "password VARCHAR(100) NOT NULL)");
        }
    }

    public void add(User user) throws SQLException {
        Connection c = getConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException {
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement("select * from users where id = ?")) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EmptyResultDataAccessException(1);
                }

                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void deleteAll() throws SQLException {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("delete from users");

        try { // 예외가 발생할가능성이 있는 코드를 모두 try 블록으로 묶어준다.
            c = dataSource.getConnection(); 

            // ps = c.prepareStatement("delete from users"); // 변하는 부분
            ps = makeStatement(c); // 변하지 않는 부분
            
            ps.executeUpdate();
        } catch(SQLException e){ // 에러가 발생했을때 부가적인 작업을 해줄 수 있도록 catch 브록을 해준다. 그렇지 않으면 Connection을 close() 하지 못하고 메소드를 빠져나갈 수 있다.
            throw e;
        } finally { //finally 이므로 블록에서 예외가 발생했을 때나 안했을 때나 모두 실행된다.
            if ( ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) { // ps.close() 메소드에서도 SQLException이 발생할 수 있기 때문에 이를 잡아줘야 한다. 그렇지 않으면 Connection을 close() 하지 못하고 메소드를 빠져나갈 수 있다.
                }
            }

            if( c != null ) {
                try {
                    c.close(); // connection 반환
                } catch(SQLException e) {

                }
            }
        }
    }

    private PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery(); //ResultSet도 다양한 SQLException 이 발생할 수 있는 코드이므로 try 블록 안에 둬야한다. 
            rs.next();
            return rs.getInt(1);
        } catch(SQLException e){
            throw e;
        } finally {
            if( rs != null ){ // 만들어진 ResultSet을 닫아주는 기능. close()는 만들어진 순서의 반대로 하는것이 원칙이다.
                try {
                    rs.close();
                } catch(SQLException e) {
                }
            }    
            if( ps != null ){
                try {
                    ps.close();;
                } catch(SQLException e) {
                }
            }
            if( c != null) {
                try {
                    c.close();;
                } catch(SQLException e) {
                }
            }
        }
    }
    
}
