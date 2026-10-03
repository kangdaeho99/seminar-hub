package org.example2.Dao;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// @ContextConfiguration(locations = "classpath:test-applicationContext.xml") // 테스트용 애플리케이션 컨텍스트 설정 위치
public class UserDaoTest {

    // @Autowired
    // private ApplicationContext context; //테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 의해 자동으로 값이 주입된다.

    // @Autowired 
    // private SingleConnectionDataSource dataSource;

    // @Autowired 
    // private DataSource dataSource2;

    // @Autowired
    private UserDaoJdbc dao;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() {
        // ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        // ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        // this.dao = context.getBean("userDao", UserDaoJdbc.class);
        // this.dao = this.context.getBean("userDao",UserDaoJdbc.class);
        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);

        dao = new UserDaoJdbc();
        DataSource dataSource = new SingleConnectionDataSource(
            "jdbc:h2:mem:springbook-test", "sa", "", true);

        dao.setDataSource(dataSource);
        dao.initialize();
    }


    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DataAccessException.class, () -> dao.add(user1));
    }


    @Test
    public void addAndGet() {
        this.dao.deleteAll();
        assertEquals(this.dao.getCount(), 0);

        this.dao.add(this.user1);
        this.dao.add(this.user2);
        assertEquals(this.dao.getCount(), 2);

        User userget1 = this.dao.get(this.user1.getId());
        assertEquals(this.user1.getName(), userget1.getName());
        assertEquals(this.user1.getPassword(), userget1.getPassword());

        User userget2 = this.dao.get(this.user2.getId());
        assertEquals(this.user2.getName(), userget2.getName());
        assertEquals(this.user2.getPassword(), userget2.getPassword());
    }

    @Test
    public void getAll() {
        this.dao.deleteAll();

        this.dao.add(this.user1);
        List<User> users1 = this.dao.getAll();
        assertEquals(1, users1.size());
        checkSameUser(this.user1, users1.get(0));

        this.dao.add(this.user2);
        List<User> users2 = this.dao.getAll();
        assertEquals(2, users2.size());
        checkSameUser(this.user1, users2.get(0));
        checkSameUser(this.user2, users2.get(1));

        this.dao.add(this.user3);
        List<User> users3 = this.dao.getAll();
        assertEquals(3, users3.size());
        checkSameUser(this.user3, users3.get(0));
        checkSameUser(this.user1, users3.get(1));
        checkSameUser(this.user2, users3.get(2));
    }

    private void checkSameUser(User expected, User actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test 
    public void count() {
        this.dao.deleteAll();
        assertEquals(this.dao.getCount(), 0);

        this.dao.add(this.user1);
        assertEquals(this.dao.getCount(), 1);

        this.dao.add(this.user2);
        assertEquals(this.dao.getCount(), 2);

        this.dao.add(this.user3);
        assertEquals(this.dao.getCount(), 3);
    }

    @Test
    public void getUserFailure() {
        this.dao.deleteAll();
        assertEquals(this.dao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> this.dao.get("unknown_id"));

    }

}

