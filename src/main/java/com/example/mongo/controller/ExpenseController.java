package com.example.mongo.controller;

import com.example.mongo.model.Expenses;
import com.example.mongo.services.ExpenseServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseServices expenseServices;

    public ExpenseController(ExpenseServices expenseServices) {
        this.expenseServices = expenseServices;
    }

    @PostMapping
    public ResponseEntity<Expenses> addExpense(@RequestBody Expenses expenses){
        expenseServices.addExpense(expenses);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Expenses> updateExpense(@RequestBody Expenses expenses){
        expenseServices.updateExpense(expenses);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Expenses>> getAllExpenses(){
        return ResponseEntity.ok(expenseServices.getAllExpenses());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Expenses> getExpenseByName(@PathVariable String name){
        return ResponseEntity.ok(expenseServices.getExpenseByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Expenses> deleteExpense(@PathVariable String id){
        expenseServices.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
