package com.seminarhub;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest
class SeminarhubBatchApplicationTests {

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Test
	void contextLoads() {
		assertThat(transactionManager.getClass().getName())
				.isEqualTo("org.springframework.orm.jpa.JpaTransactionManager");
	}

}
