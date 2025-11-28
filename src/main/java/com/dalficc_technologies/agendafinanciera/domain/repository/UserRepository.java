package com.dalficc_technologies.agendafinanciera.domain.repository;


import com.dalficc_technologies.agendafinanciera.domain.model.User;

import java.util.concurrent.ExecutionException;

public interface UserRepository {

    User getUserById(String userId) throws ExecutionException, InterruptedException;
}