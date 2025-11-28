package com.dalficc_technologies.agendafinanciera.presentation.controller;

import com.dalficc_technologies.agendafinanciera.application.service.GetUserTransactionsService;
import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final GetUserTransactionsService getUserTransactionsService;

    public TransactionController(GetUserTransactionsService getUserTransactionsService) {
        this.getUserTransactionsService = getUserTransactionsService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionItem>> getTransactions (@RequestHeader(value = "Authorization") String token){
        if (token == null) {
            return null;
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decodedToken.getUid();
            System.out.println(userId);
            List<TransactionItem> transactions = getUserTransactionsService.getUserTransactions(userId);
            return ResponseEntity.ok(transactions);
        }catch (Exception e) {
            return null;
        }
    }
}
