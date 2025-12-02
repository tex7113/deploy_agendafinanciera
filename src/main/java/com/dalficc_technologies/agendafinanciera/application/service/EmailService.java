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

    private final String logoUrl = "http://localhost:8080/static/img/logo.png";
    // private final String logoUrl = "https://dalficc-agendafinanciera.onrender.com/static/img/logo.png";

    public void sendEmail(String to, String subject, String name, String userEmail, String messageText) throws Exception {

        String htmlTemplate = """
            <!DOCTYPE html>
            <html lang="es">
            <body style="margin:0; font-family:'Arial', sans-serif; background:#f4f4f4; padding:20px;">

                <table width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td align="center">

                            <table width="600" cellpadding="0" cellspacing="0" 
                                   style="background:#ffffff; border-radius:10px; overflow:hidden; 
                                          box-shadow:0 4px 12px rgba(0,0,0,0.12);">

                                <tr>
                                    <td style="background:#0a1a2f; padding:25px; text-align:center;">
                                        <img src="{{logoUrl}}" alt="DALFICC Logo" width="120" style="margin-bottom:10px;">
                                        <h2 style="color:#ffffff; margin:0;">DALFICC TECHNOLOGIES</h2>
                                        <p style="color:#75a7ff; margin:0; font-size:14px;">
                                            Software Development Company
                                        </p>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:30px;">

                                        <h3 style="color:#0a1a2f; margin-top:0;">Nuevo mensaje desde Agenda Financiera</h3>

                                        <div style="background:#f7f9fc; padding:15px; 
                                                    border-left:4px solid #0a1a2f; 
                                                    border-radius:5px;">

                                            <p><strong>Nombre:</strong> {{name}}</p>
                                            <p><strong>Correo:</strong> {{email}}</p>
                                            <p><strong>Asunto:</strong> {{subject}}</p>

                                            <p style="white-space:pre-wrap; margin-top:15px;">
                                                <strong>Mensaje:</strong><br>{{message}}
                                            </p>
                                        </div>

                                        <p style="color:#555; font-size:14px; margin-top:25px;">
                                            Puedes responder directamente a este correo para contactar con el usuario.
                                        </p>

                                    </td>
                                </tr>

                                <tr>
                                    <td style="background:#f0f0f0; text-align:center; padding:15px; font-size:12px; color:#777;">
                                        © {{year}} DALFICC TECHNOLOGIES — Todos los derechos reservados.
                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

            </body>
            </html>
            """;

        // Sustituimos variables manualmente (SEGURIDAD TOTAL)
        String htmlContent = htmlTemplate
                .replace("{{logoUrl}}", logoUrl)
                .replace("{{name}}", name)
                .replace("{{email}}", userEmail)
                .replace("{{subject}}", subject)
                .replace("{{message}}", messageText)
                .replace("{{year}}", String.valueOf(java.time.Year.now().getValue()));

        Email from = new Email(senderEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);

        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("SendGrid response code: " + response.getStatusCode());
    }
}