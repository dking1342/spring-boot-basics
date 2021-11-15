package com.example.jpa.repository;

import com.example.jpa.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentInterface extends JpaRepository<Student,Long> {
}
