package com.example.dock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class DockApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockApplication.class, args);
	}

}
