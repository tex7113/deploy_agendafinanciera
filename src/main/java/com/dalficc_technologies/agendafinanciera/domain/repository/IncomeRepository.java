package com.dalficc_technologies.agendafinanciera.domain.repository;

import com.dalficc_technologies.agendafinanciera.domain.model.Income;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IncomeRepository {
    List<Income> getIcomesByUserId(String userId) throws ExecutionException, InterruptedException;
}
