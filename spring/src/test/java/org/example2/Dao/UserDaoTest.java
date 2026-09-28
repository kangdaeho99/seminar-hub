package org.example2.Dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTest {
    public  static void main(String[] args) throws ClassNotFoundException, SQLException{

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao daoFromApllicationContext = context.getBean("userDao", UserDao.class);
        UserDao daoFromApllicationContext2 = context.getBean("userDao", UserDao.class);
        System.out.println(daoFromApllicationContext);
        System.out.println(daoFromApllicationContext2);

        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();
        System.out.println(dao1);
        System.out.println(dao2);

    }

    
}
