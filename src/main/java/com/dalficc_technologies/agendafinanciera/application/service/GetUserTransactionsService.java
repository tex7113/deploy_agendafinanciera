package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import com.dalficc_technologies.agendafinanciera.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class GetUserTransactionsService {

    private final TransactionRepository transactionRepository;

    public GetUserTransactionsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionItem> getUserTransactions(String userId) {
        try {
            return transactionRepository.getTransactions(userId).get(); // bloquea hasta obtener datos
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}