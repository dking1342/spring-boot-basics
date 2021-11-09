package com.example.mongo.services;

import com.example.mongo.model.Expenses;
import com.example.mongo.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServices {

    private final ExpenseRepository expenseRepository;

    public ExpenseServices(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void addExpense(Expenses expense){
        expenseRepository.insert(expense);
    }

    public void updateExpense(Expenses expenses){
        Expenses savedExpense = expenseRepository.findById(expenses.getId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot Find Expense by ID %s",expenses.getId())
                ));

        savedExpense.setExpenseName(expenses.getExpenseName());
        savedExpense.setExpenseCategory(expenses.getExpenseCategory());
        savedExpense.setExpenseAmount(expenses.getExpenseAmount());
        expenseRepository.save(expenses);
    }

    public List<Expenses> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Expenses getExpenseByName(String name){
        return expenseRepository.findByName(name)
                .orElseThrow(()-> new RuntimeException(
                        String.format("Cannot find expense by name %s",name)
                ));
    }

    public void deleteExpense(String id){
        expenseRepository.deleteById(id);
    }
}
