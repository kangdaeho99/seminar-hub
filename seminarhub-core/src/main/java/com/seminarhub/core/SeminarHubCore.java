package com.seminarhub.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * [ 2023-08-19 daeho.kang ]
 * Description : SeminarHub Core Module
 * It is for Jar ( not BootJar )
 *
 */
//@SpringBootApplication
public class SeminarHubCore {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubCore.class, args);;
    }
}