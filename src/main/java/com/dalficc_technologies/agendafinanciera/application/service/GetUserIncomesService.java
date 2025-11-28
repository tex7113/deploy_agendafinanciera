package com.dalficc_technologies.agendafinanciera.application.service;

import com.dalficc_technologies.agendafinanciera.domain.model.Income;
import com.dalficc_technologies.agendafinanciera.domain.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class GetUserIncomesService {

    @Autowired
    private IncomeRepository incomeRepository;

    public List<Income> getIcomes(String userid) throws ExecutionException, InterruptedException{
        return incomeRepository.getIcomesByUserId(userid);
    }
}
