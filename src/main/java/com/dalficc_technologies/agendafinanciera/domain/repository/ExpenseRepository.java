package com.dalficc_technologies.agendafinanciera.domain.repository;

import com.dalficc_technologies.agendafinanciera.domain.model.Expense;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ExpenseRepository {

    List<Expense> getExpenseByUserId(String userId) throws ExecutionException, InterruptedException;

    Expense addExpense(String userId, Expense expense) throws ExecutionException, InterruptedException;

}
