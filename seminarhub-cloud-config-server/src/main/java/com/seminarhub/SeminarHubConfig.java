package com.seminarhub;

import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [2023-08-18 daeho.kang]
 * Description: Main class for SeminarHubConfig
 * This class serves as a ConfigServer.
 */
@SpringBootApplication
@EnableConfigServer
public class SeminarHubConfig {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubConfig.class, args);
    }
}