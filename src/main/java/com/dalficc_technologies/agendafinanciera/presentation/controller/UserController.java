package com.dalficc_technologies.agendafinanciera.presentation.controller;

import com.dalficc_technologies.agendafinanciera.application.service.GetUserService;
import com.dalficc_technologies.agendafinanciera.domain.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private GetUserService getUserService;

    @GetMapping
    public User getCurrentUser(@RequestHeader(value = "Authorization") String token) throws ExecutionException, InterruptedException, FirebaseAuthException {
        if (token == null) {
            return null;
        }
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String userId = decodedToken.getUid();
            return getUserService.getUser(userId);

        } catch (FirebaseAuthException e) {
            return null;
        }
    }
}