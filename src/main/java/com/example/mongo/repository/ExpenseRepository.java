package com.example.mongo.repository;

import com.example.mongo.model.Expenses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expenses, String> {

    @Query("{'name':?0}")
    Optional<Expenses> findByName(String name);

}
