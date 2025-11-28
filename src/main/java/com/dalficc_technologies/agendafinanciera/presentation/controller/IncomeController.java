package com.dalficc_technologies.agendafinanciera.presentation.controller;

import com.dalficc_technologies.agendafinanciera.application.service.GetUserIncomesService;
import com.dalficc_technologies.agendafinanciera.domain.model.Income;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/incomes")
public class IncomeController {

    @Autowired
    private GetUserIncomesService getUserIncomesService;

    @GetMapping
    public List<Income> getIncomes(@RequestHeader(value = "Authorization") String token) throws ExecutionException, InterruptedException {
        if (token == null) {
            System.out.println("token basio");
            return null;
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decodedToken.getUid();
            return getUserIncomesService.getIcomes(userId);
        } catch (FirebaseAuthException e) {
            System.out.println("token error");
            return null;
        }
    }
}
