package com.seminarhub.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [ 2023-09-21 daeho.kang ]
 * Description : Jasypt Config
 * It Encrypts the Yml Secret.
 */
@Configuration
@EnableEncryptableProperties
//@EncryptablePropertySources(@EncryptablePropertySource("classpath:/application.yml"))
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String jasyptEncryptorPassword;

    /**
     * [ 2023-09-21 daeho.kang ]
     * Description : It Encrypts the Secret Info In yml Using Jasypt
     * https://github.com/ulisesbocchio/jasypt-spring-boot#use-you-own-custom-encryptor
     * https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
     * Pass Password And Set it as a JVM System Property Environment Example : java -Djasypt.encryptor.password=password -jar target/jasypt-spring-boot-demo-0.0.1-SNAPSHOT.jar
     * Pass Password by Particular Jasypt Library Environment Example : java --jasypt.encryptor.password=password -jar target/jasypt-spring-boot-demo-0.0.1-SNAPSHOT.jar
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        System.out.println("HELLO JASYPT:"+jasyptEncryptorPassword);
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(jasyptEncryptorPassword);
//        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
