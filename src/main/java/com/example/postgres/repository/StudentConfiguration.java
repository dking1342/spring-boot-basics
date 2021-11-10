package com.example.postgres.repository;

import com.example.postgres.model.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class StudentConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
          Student student1 = new Student(
                  1L,
                  "Jack",
                  "jack@example.com",
                  LocalDate.of(2000,1,1)
          );
          Student student2 = new Student(
                  2L,
                  "Jill",
                  "jill@example.com",
                  LocalDate.of(1990,1,1)
          );
          List<Student> studentList = new ArrayList<>();
          studentList.add(0,student1);
          studentList.add(1,student2);
          repository.saveAll(studentList);
        };
    }
}
