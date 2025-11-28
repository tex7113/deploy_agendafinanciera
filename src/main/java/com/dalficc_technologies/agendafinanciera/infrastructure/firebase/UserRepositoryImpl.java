package com.dalficc_technologies.agendafinanciera.infrastructure.firebase;

import com.dalficc_technologies.agendafinanciera.domain.model.User;
import com.dalficc_technologies.agendafinanciera.domain.repository.UserRepository;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User getUserById(String userId) throws ExecutionException, InterruptedException {

        final User[] user = new User[1];
        CountDownLatch latch = new CountDownLatch(1);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("profile");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user[0] = dataSnapshot.getValue(User.class);
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });
        latch.await();
        return user[0];
    }
}