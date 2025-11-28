package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.User;
import com.dalficc_technologies.agendafinanciera.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class GetUserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser (String userId) throws ExecutionException, InterruptedException{
        return userRepository.getUserById(userId);
    }
}