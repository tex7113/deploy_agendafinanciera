package com.dalficc_technologies.agendafinanciera.infrastructure.firebase;

import com.dalficc_technologies.agendafinanciera.domain.model.Income;
import com.dalficc_technologies.agendafinanciera.domain.repository.IncomeRepository;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Repository
public class IncomeRepositoryImpl implements IncomeRepository {
    @Override
    public List<Income> getIcomesByUserId(String userId) throws ExecutionException, InterruptedException {
        List<Income> incomes = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("incomes");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Income income = child.getValue(Income.class);
                    if (income != null){
                        income.setId(child.getKey());
                        incomes.add(income);
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
        return incomes;
    }
}
