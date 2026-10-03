package org.example2.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void initialize() throws SQLException {
        this.jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users ("
            + "id VARCHAR(100) PRIMARY KEY, "
            + "name VARCHAR(100) NOT NULL, "
            + "password VARCHAR(100) NOT NULL)");
    }

    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)",
            user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }, id
        );
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }


    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
    
}
