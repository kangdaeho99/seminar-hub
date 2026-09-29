package org.example2.Dao;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.transaction.event.TransactionalEventListener;

public class UserDaoTest {

    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
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

        User user = new User();
        user.setId("user");
        user.setName("백기선");
        user.setPassword("married");

        daoFromIoC.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = daoFromIoC.get(user.getId());
        if(!user.getName().equals(user2.getName())){
            System.out.println("테스트 실패 (name)");
        }
        else if(!user.getPassword().equals(user2.getPassword())){
            System.out.println("테스트 실패 (password)");
        }
        else {
            System.out.println("조회 테스트 성공");
        }
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }


}
