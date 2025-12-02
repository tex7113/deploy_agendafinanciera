package com.dalficc_technologies.agendafinanciera.application.service;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendgridApiKey;

    @Value("${MAIL_FROM}")
    private String senderEmail;

    public void sendEmail(String to, String subject, String message) throws Exception {

        Email from = new Email(senderEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", message);

        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("SendGrid response code: " + response.getStatusCode());

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error enviando correo: " + ex.getMessage());
        }
    }
}