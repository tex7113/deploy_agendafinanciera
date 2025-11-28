package com.dalficc_technologies.agendafinanciera.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() { return "home"; }

    @GetMapping("/public/servicios")
    public String services() { return "services"; }

    @GetMapping("/public/quienes-somos")
    public String about() { return "about"; }

    @GetMapping("/public/contacto")
    public String contac() { return "contact"; }

    @GetMapping("/public/iniciar-sesion")
    public String login() { return "login"; }
}
