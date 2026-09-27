package org.example2.Dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {
    public  static void main(String[] args) throws ClassNotFoundException, SQLException{
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao daoFromApplicationContext = context.getBean("userDao", UserDao.class);
        UserDao daoFromApplicationContext2 = context.getBean("userDao", UserDao.class);
        System.out.println(daoFromApplicationContext);
        System.out.println(daoFromApplicationContext2);

        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();
        System.out.println(dao1);
        System.out.println(dao2);



    }

    
}
