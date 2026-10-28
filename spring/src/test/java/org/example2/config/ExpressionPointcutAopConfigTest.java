package org.example2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;

import static org.mockito.Mockito.mock;

// 같은 프록시/커밋/롤백 테스트를 표현식 기반 Config에도 적용한다.
class ExpressionPointcutAopConfigTest extends PureSpringAopConfigTest {
    @Override
    protected Class<?> configurationClass() {
        return TestConfig.class;
    }

    @Configuration
    static class TestConfig extends ExpressionPointcutAopConfig {
        @Override
        @Bean(destroyMethod = "shutdown")
        public EmbeddedDatabase dataSource() {
            return new EmbeddedDatabaseBuilder()
                    .generateUniqueName(true)
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }

        @Override
        @Bean
        public MailSender mailSender() {
            return mock(MailSender.class);
        }
    }
}
