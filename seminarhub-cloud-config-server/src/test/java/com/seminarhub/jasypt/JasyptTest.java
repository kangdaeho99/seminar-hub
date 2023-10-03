package com.seminarhub.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JasyptTest {

    @DisplayName("Jasypt Encryption Test")
    @Test
    public void testEncryptionAndDecryption() {
        // given
        // when
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("mySecretPassword");
        String originalValue = "myData";
        String encryptedValue = encryptor.encrypt(originalValue);
        System.out.println("Encrypted Value: " + encryptedValue);
        String decryptedValue = encryptor.decrypt(encryptedValue);
        System.out.println("Decrypted Value: " + decryptedValue);

        // then
        assert decryptedValue.equals(originalValue);
    }

}
