package com.cardiocare360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Cardiocare360Application {

	public static void main(String[] args) {
		SpringApplication.run(Cardiocare360Application.class, args);
		
		 PasswordEncoder encoder = new BCryptPasswordEncoder();
		    System.out.println("HASH = " + encoder.encode("123"));
	}

}
