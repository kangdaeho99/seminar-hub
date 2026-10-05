package org.example2.Service;

import org.example2.Dao.DaoFactory;
import org.example2.Dao.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;

// XML 대신 Java 설정으로 같은 프록시, 포인트컷, 커밋/롤백 테스트를 실행한다.
@ContextConfiguration(classes = DaoFactoryUserServiceTest.TestDaoFactory.class, inheritLocations = false)
public class DaoFactoryUserServiceTest extends UserServiceTest {

    @Configuration
    public static class TestDaoFactory extends DaoFactory {
        @Bean
        public UserService testUserService() {
            // static 내부 클래스는 Java 설정에서 직접 생성한 뒤 빈으로 반환한다.
            TestUserServiceImpl service = new TestUserServiceImpl();
            service.setUserDao(userDao());
            service.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
            service.setMailSender(mailSender());
            return service;
        }

        @Bean
        @Override
        public MockMailSender mailSender() {
            return new MockMailSender();
        }

        @Bean(destroyMethod = "shutdown")
        @Override
        public EmbeddedDatabase dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }
    }
}
