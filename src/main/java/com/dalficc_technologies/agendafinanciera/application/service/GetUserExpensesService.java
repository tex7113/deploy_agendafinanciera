package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.Expense;
import com.dalficc_technologies.agendafinanciera.domain.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GetUserExpensesService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> execute(String userId) throws ExecutionException, InterruptedException {
        return expenseRepository.getExpenseByUserId(userId);
    }
}
