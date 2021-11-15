package com.example.jpa.repository;

import com.example.jpa.model.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(StudentInterface studentInterface){
        return args -> {
            Student student1 = new Student(
                    "jack",
                    "jones",
                    "jack@example.com",
                    20
            );
            studentInterface.save(student1);
        };
    }
}
