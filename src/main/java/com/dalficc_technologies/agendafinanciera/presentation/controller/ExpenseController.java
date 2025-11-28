package com.dalficc_technologies.agendafinanciera.presentation.controller;

import com.dalficc_technologies.agendafinanciera.application.service.AddExpenseService;
import com.dalficc_technologies.agendafinanciera.application.service.GetUserExpensesService;
import com.dalficc_technologies.agendafinanciera.domain.model.Expense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController {

    @Autowired
    private GetUserExpensesService getUserExpensesService;
    @Autowired
    private AddExpenseService addExpenseService;

    @GetMapping
    public List<Expense> getExpenses(@RequestHeader(value = "Authorization") String token) throws ExecutionException, InterruptedException {
        if (token == null){
            return null;
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decodedToken.getUid();
            System.out.println(userId);
            return getUserExpensesService.execute(userId);

        } catch (FirebaseAuthException e) {
            return null;
        }
    }

    @PostMapping("/{userId}")
    public Expense addExpense(
            @PathVariable String userId,
            @RequestBody @Valid Expense expense
    ) throws ExecutionException, InterruptedException {
        return addExpenseService.execute(userId, expense);
    }
}