package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.Expense;
import com.dalficc_technologies.agendafinanciera.domain.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AddExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense execute (String userId, Expense expense) throws ExecutionException, InterruptedException {
        return expenseRepository.addExpense(userId, expense);
    }
}
