package com.dalficc_technologies.agendafinanciera.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel-de-control")
public class WebUserController {

    @GetMapping
    public String dashboard(Model model) {
        return "dashboard";
    }
}
