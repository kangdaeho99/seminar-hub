package org.example2.Dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTest {
    public  static void main(String[] args) throws ClassNotFoundException, SQLException{

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
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }

    
}
