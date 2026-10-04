package org.example2.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    };

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initialize() {
        this.jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users ("
            + "id VARCHAR(100) PRIMARY KEY, "
            + "name VARCHAR(100) NOT NULL, "
            + "password VARCHAR(100) NOT NULL, "
            + "level INTEGER NOT NULL, "
            + "login INTEGER NOT NULL, "
            + "recommend INTEGER NOT NULL, "
            + "email VARCHAR(255))");
        // 기존 파일 기반 H2 데이터베이스에도 이메일 컬럼을 추가한다.
        this.jdbcTemplate.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(255)");
    }

    public void add(final User user) {
        this.jdbcTemplate.update(
            "insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)",
            user.getId(), user.getName(), user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
            "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?",
            user.getName(), user.getPassword(), user.getLevel().intValue(),
            user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
            this.userMapper, id
        );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }


    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
    
}
