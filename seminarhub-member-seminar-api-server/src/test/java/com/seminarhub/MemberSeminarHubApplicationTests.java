package com.seminarhub;

import org.junit.jupiter.api.Disabled;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class MemberSeminarHubApplicationTests {

	@DisplayName("Context Load Text")
	@Test
	void contextLoads() {
		System.out.println("Context Loads.....");
	}

}
