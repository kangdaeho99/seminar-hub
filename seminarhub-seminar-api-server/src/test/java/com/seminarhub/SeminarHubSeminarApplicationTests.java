package com.seminarhub;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * [ 2023-08-30 daeho.kang ]
 * Description :
 * Test class for loading the application context in the SeminarHubSeminarApplication.
 */
@SpringBootTest
class SeminarHubSeminarApplicationTests {

	/**
	 * [ 2023-08-30 daeho.kang ]
	 * Description : Description: Test method for loading the application context.
	 * 
	 */
	@DisplayName("Context Load Text")
	@Test
	void contextLoads() {
		System.out.println("Context Loads.....");
	}

}
