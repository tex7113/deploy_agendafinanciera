package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.exception.FirebaseLoginException;
import com.dalficc_technologies.agendafinanciera.domain.repository.AuthRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public String login(String email, String password) throws FirebaseLoginException {
        return authRepository.login(email, password);
    }
}