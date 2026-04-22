package com.assesment.apiAssesment;

import org.springframework.boot.SpringApplication;

public class TestApiAssesmentApplication {

	public static void main(String[] args) {
		SpringApplication.from(ApiAssesmentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
