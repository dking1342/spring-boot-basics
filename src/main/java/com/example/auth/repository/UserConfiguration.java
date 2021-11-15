package com.example.auth.repository;

import com.example.auth.model.AppRole;
import com.example.auth.model.AppUser;
import com.example.auth.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class UserConfiguration {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService){
        return args -> {
            userService.saveRole(new AppRole(null,"ROLE_USER"));
            userService.saveRole(new AppRole(null,"ROLE_MANAGER"));
            userService.saveRole(new AppRole(null,"ROLE_ADMIN"));
            userService.saveRole(new AppRole(null,"ROLE_SUPER_ADMIN"));

            userService.saveUser(new AppUser(null,"jack doe","jack","1234",new ArrayList<>()));
            userService.saveUser(new AppUser(null,"jane doe","jill","1234",new ArrayList<>()));
            userService.saveUser(new AppUser(null,"john doe","john","1234",new ArrayList<>()));

            userService.addRoleToUser("jack","ROLE_USER");
            userService.addRoleToUser("jack","ROLE_SUPER_ADMIN");
            userService.addRoleToUser("jill","ROLE_MANAGER");
            userService.addRoleToUser("john","ROLE_ADMIN");
        };
    }
}
