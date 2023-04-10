package com.example.webapplication;

import com.example.webapplication.model.Role;
import com.example.webapplication.model.User;
import com.example.webapplication.repository.RoleRepository;
import com.example.webapplication.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication implements CommandLineRunner {
	@Autowired
	RoleService roleService;
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		roleService.addRole("USER");
		roleService.addRole("ADMIN");
		roleService.addRole("MOD");
	}
}
