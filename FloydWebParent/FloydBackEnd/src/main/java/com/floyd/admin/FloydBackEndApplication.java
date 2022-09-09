package com.floyd.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.floyd.common.entity", "com.floyd.admin.user"})
public class FloydBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(FloydBackEndApplication.class, args);
	}

}
