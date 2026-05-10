package com.seminarhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {
    // DataSource and JdbcTemplate are now auto-configured by Spring Boot 
    // based on application.yml and .env properties.
}