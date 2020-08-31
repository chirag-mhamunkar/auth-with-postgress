package com.example.authservice;

import com.example.authservice.configuration.DBConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DBConfiguration.class)
class AuthWithPostgressApplicationTests {

	@Test
	void contextLoads() {
	}

}
