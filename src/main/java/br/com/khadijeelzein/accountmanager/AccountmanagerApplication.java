package br.com.khadijeelzein.accountmanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class AccountmanagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(AccountmanagerApplication.class, args);
	}
}
