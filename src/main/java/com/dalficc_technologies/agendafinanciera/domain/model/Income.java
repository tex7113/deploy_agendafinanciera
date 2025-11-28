package com.dalficc_technologies.agendafinanciera.domain.model;

import lombok.Data;

@Data
public class Income {
    private String id;
    private String description;
    private Double amount;
    private String category;
    private Long date;
    private Long createdAt;

}
