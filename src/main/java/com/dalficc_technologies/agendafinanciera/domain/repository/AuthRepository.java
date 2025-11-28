package com.dalficc_technologies.agendafinanciera.domain.repository;

import com.dalficc_technologies.agendafinanciera.domain.exception.FirebaseLoginException;

public interface AuthRepository {

    String login(String email, String password) throws FirebaseLoginException;
}
