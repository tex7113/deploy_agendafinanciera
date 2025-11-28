package com.dalficc_technologies.agendafinanciera.infrastructure.firebase;

import com.dalficc_technologies.agendafinanciera.domain.model.Expense;
import com.dalficc_technologies.agendafinanciera.domain.model.Income;
import com.dalficc_technologies.agendafinanciera.domain.model.TransactionItem;
import com.dalficc_technologies.agendafinanciera.domain.repository.TransactionRepository;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private DatabaseReference userReference(String userId) {
        return FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);
    }

    @Override
    public CompletableFuture<List<TransactionItem>> getTransactions(String userId) {

        CompletableFuture<List<TransactionItem>> future = new CompletableFuture<>();
        List<TransactionItem> transactions = new ArrayList<>();

        DatabaseReference ref = userReference(userId);

        // Leer incomes
        ref.child("incomes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    Income income = child.getValue(Income.class);

                    if (income != null) {
                        TransactionItem item = new TransactionItem();
                        item.setId(income.getId());
                        item.setDescription(income.getDescription());
                        item.setAmount(income.getAmount());
                        item.setCategory(income.getCategory());
                        item.setDate(income.getDate());
                        item.setIsIncome(true);

                        transactions.add(item);
                    }
                }

                ref.child("expenses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot2) {

                        for (DataSnapshot child : snapshot2.getChildren()) {
                            Expense expense = child.getValue(Expense.class);

                            if (expense != null && Boolean.TRUE.equals(expense.getPaid())) {
                                TransactionItem item = new TransactionItem();
                                item.setId(expense.getId());
                                item.setDescription(expense.getDescription());
                                item.setAmount(expense.getAmount());
                                item.setCategory(expense.getCategory());
                                item.setDate(expense.getDate());
                                item.setIsIncome(false);

                                transactions.add(item);
                            }
                        }

                        transactions.sort((a, b) -> Long.compare(b.getDate(), a.getDate()));



                        future.complete(transactions);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }
}
