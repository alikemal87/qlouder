package com.data.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.data"})
public class SpringBootRestApp {
	public static void main(String[] args) {
	    SpringApplication.run(SpringBootRestApp.class, args);
	    }
}
