package com.seminarhub.data;

import com.seminarhub.global.config.QuerydslConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaAuditing
@Import(QuerydslConfig.class)
@EntityScan("com.seminarhub.domain")
@EnableJpaRepositories("com.seminarhub.domain")
public class DataTestApplication {}
