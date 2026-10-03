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

    public void deleteAll() throws SQLException { //deleteAll이 클라이언트의 역할
        StatementStrategy strategy = new DeleteAllStatement(); // 선정한 전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(strategy); // 컨텍스트를 호출. 전략 오브젝트 전달 (컨텍스트란 전략을 받아서 실행하는 공통 작업 흐름을 담당)
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException { //클라이언트가 컨텍스트를 호출할 떄 넘겨줄 전략 파라미터
        Connection c = null; 
        PreparedStatement ps = null; 

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate();
        } catch(SQLException e) {
            throw e; 
        } finally {
            if( ps != null ) {
                try {
                    ps.close();;
                } catch(SQLException e) {
                }
            }
            if( c != null ) {
                try {
                    c.close();;
                } catch(SQLException e) {
                }
            }
        }

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
