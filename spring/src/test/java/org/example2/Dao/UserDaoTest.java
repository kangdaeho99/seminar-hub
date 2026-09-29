package org.example2.Dao;

import java.sql.SQLException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
   // ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao daoFromIoC = context.getBean("userDao", UserDao.class);
        UserDao daoFromIoC2 = context.getBean("userDao", UserDao.class);
        System.out.println(daoFromIoC);
        System.out.println(daoFromIoC2);

        /// ///////////////////////////////////
        /// ///////////////////////////////////
        /// ///////////////////////////////////
        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();
        System.out.println(dao1);
        System.out.println(dao2);
        /// ///////////////////////////////////
        /// ///////////////////////////////////
        /// ///////////////////////////////////

        daoFromIoC.deleteAll();
        assertEquals(daoFromIoC.getCount(), 0);

        User user = new User(UUID.randomUUID().toString(), "박성철", "springno1");
        User user2 = new User(UUID.randomUUID().toString(), "이길원", "springno2");

        daoFromIoC.add(user);
        daoFromIoC.add(user2);
        assertEquals(daoFromIoC.getCount(), 2);

        User userget1 = daoFromIoC.get(user.getId());
        assertEquals(user.getName(), userget1.getName());
        assertEquals(user.getPassword(), userget1.getPassword());

        
        User userget2 = daoFromIoC.get(user2.getId());
        assertEquals(user2.getName(), userget2.getName());
        assertEquals(user2.getPassword(), userget2.getPassword());
    }

    @Test 
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user1 = new User("gyumee", "박성철", "springno1");
        User user2 = new User("leegw700", "이길원", "springno2");
        User user3 = new User("bumjin", "박범진", "springno3");

        dao.deleteAll();
        assertEquals(dao.getCount(), 0);

        dao.add(user1);
        assertEquals(dao.getCount(), 1);

        dao.add(user2);
        assertEquals(dao.getCount(), 2);

        dao.add(user3);
        assertEquals(dao.getCount(), 3);
    }

    @Test
    public void getUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");   
        UserDao daoFromIoC = context.getBean("userDao", UserDao.class);

        daoFromIoC.deleteAll();
        assertEquals(daoFromIoC.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> daoFromIoC.get("unknown_id"));

    }

}

