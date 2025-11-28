package com.dalficc_technologies.agendafinanciera.infrastructure.firebase;

import com.dalficc_technologies.agendafinanciera.domain.model.Expense;
import com.dalficc_technologies.agendafinanciera.domain.repository.ExpenseRepository;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository {

    @Override
    public List<Expense> getExpenseByUserId(String userId) throws ExecutionException, InterruptedException {
        List<Expense> expenses = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("expenses");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Expense expense = child.getValue(Expense.class);
                    if (expense != null) {
                        expense.setId(child.getKey());
                        expenses.add(expense);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });
        latch.await();
        return expenses;
    }

    @Override
    public Expense addExpense(String userId, Expense expense) throws ExecutionException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("expenses")
                .push();

        String newId = ref.getKey();
        expense.setId(newId);

        ref.setValue(expense, (error, ref1) -> {

            latch.countDown();
        });

        latch.await();
        return expense;
    }
}
