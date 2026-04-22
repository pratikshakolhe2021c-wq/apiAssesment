package com.assesment.apiAssesment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ApiAssesmentApplicationTests {

	@Test
	void contextLoads() {
	}

}
