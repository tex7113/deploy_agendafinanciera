package com.dalficc_technologies.agendafinanciera.web.controller;


import com.dalficc_technologies.agendafinanciera.application.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ContactController {

    private final EmailService emailService;

    // Correo destino de la empresa (variable de entorno)
    @Value("${company.email}")
    private String companyEmail;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/public/contacto")
    public String showContactForm() {
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            Model model) {

        try {
            String fullMessage =
                    "Nombre: " + name +
                            "\nCorreo: " + email +
                            "\n\nMensaje:\n" + message;

            // Enviar al correo de la empresa
            emailService.sendEmail(companyEmail, subject, fullMessage);

            model.addAttribute("success", "Tu mensaje ha sido enviado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al enviar el mensaje. Inténtalo nuevamente.");
            System.out.println("MAIL_USERNAME = " + System.getenv("MAIL_USERNAME"));
            System.out.println("MAIL_PASSWORD = " + System.getenv("MAIL_PASSWORD"));
            System.out.println("COMPANY_EMAIL = " + companyEmail);
        }

        return "contact";
    }

    @GetMapping("/test-mail")
    @ResponseBody
    public String testEmail() throws Exception {
        emailService.sendEmail("dalficctechnologies@gmail.com", "Test", "Correo de prueba");
        return "OK";
    }
}