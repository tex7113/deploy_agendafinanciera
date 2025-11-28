package com.dalficc_technologies.agendafinanciera.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account}")
    private String firebaseServiceAccount; // variable de entorno (JSON entero)

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @PostConstruct
    public void initialize() throws IOException {

        // Convierte el JSON (String) en InputStream
        InputStream serviceAccount =
                new ByteArrayInputStream(firebaseServiceAccount.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado correctamente.");
        }
    }
}