package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * [ 2023-07-11 daeho.kang ]
 * Description :
 * EnableJpaAuditing : JPA Auditing을 활성화시킵니다.
 * SpringBootApplication : 스프링부트의 자동설정, 스프링 Bean 읽기와 생성
 * SpringBootApplication이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야합니다.
 */
@SpringBootApplication
@EnableJpaAuditing
public class SeminarHubApplication {
	public static void main(String[] args) {
		SpringApplication.run(SeminarHubApplication.class, args);
	}
}
