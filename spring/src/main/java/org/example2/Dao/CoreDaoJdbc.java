package org.example2.Dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class CoreDaoJdbc implements CoreDao {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initialize() {
        // 이름이 중복되면 DB 예외가 발생하므로 일괄 저장의 롤백을 확인할 수 있다.
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS core_items (name VARCHAR(100) PRIMARY KEY)");
    }

    @Override
    public void add(String name) {
        jdbcTemplate.update("INSERT INTO core_items(name) VALUES (?)", name);
    }

    @Override
    public List<String> getAll() {
        return jdbcTemplate.queryForList("SELECT name FROM core_items ORDER BY name", String.class);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM core_items");
    }
}
