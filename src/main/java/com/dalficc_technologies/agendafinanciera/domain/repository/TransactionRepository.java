package com.dalficc_technologies.agendafinanciera.domain.repository;

import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface TransactionRepository {
    CompletableFuture<List<TransactionItem>> getTransactions(String userId);
}