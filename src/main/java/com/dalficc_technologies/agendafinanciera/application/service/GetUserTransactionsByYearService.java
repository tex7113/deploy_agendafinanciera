package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import com.dalficc_technologies.agendafinanciera.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUserTransactionsByYearService {

    private final TransactionRepository transactionRepository;

    public GetUserTransactionsByYearService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionItem> getTransactionsByYear(String userId, int year) {
        try {
            List<TransactionItem> all = transactionRepository.getTransactions(userId).get();

            long startMillis = LocalDate.of(year, 1, 1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            long endMillis = LocalDate.of(year, 12, 31)
                    .atTime(23, 59, 59)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            return all.stream()
                    .filter(t -> t.getDate() >= startMillis && t.getDate() <= endMillis)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
