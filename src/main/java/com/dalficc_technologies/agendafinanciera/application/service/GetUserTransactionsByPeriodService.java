package com.dalficc_technologies.agendafinanciera.application.service;


import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import com.dalficc_technologies.agendafinanciera.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserTransactionsByPeriodService {

    private final TransactionRepository transactionRepository;

    public GetUserTransactionsByPeriodService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionItem> getTransactionsByPeriod(String userId, int year, int month) {
        try {
            List<TransactionItem> all = transactionRepository.getTransactions(userId).get();

            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            long startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endMillis = end.atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            return all.stream()
                    .filter(t -> t.getDate() >= startMillis && t.getDate() <= endMillis)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}