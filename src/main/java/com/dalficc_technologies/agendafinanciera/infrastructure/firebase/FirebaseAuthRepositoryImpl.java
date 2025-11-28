package com.dalficc_technologies.agendafinanciera.infrastructure.firebase;

import com.dalficc_technologies.agendafinanciera.domain.repository.AuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dalficc_technologies.agendafinanciera.domain.exception.FirebaseLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FirebaseAuthRepositoryImpl implements AuthRepository {

    @Value("${firebase.api-key}")
    private String firebaseApiKey;

    @Override
    public String login(String email, String password) throws FirebaseLoginException {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + firebaseApiKey;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("password", password);
        request.put("returnSecureToken", true);

        try {
            // Realiza la petición POST a Firebase
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("idToken")) {
                throw new FirebaseLoginException("Error al autenticar: respuesta inválida de Firebase.");
            }

            return (String) response.get("idToken");

        } catch (HttpClientErrorException e) {
            // Errores 400/401/403 – Firebase devuelve un JSON con un mensaje de error
            String responseBody = e.getResponseBodyAsString();

            // Puedes parsear el mensaje de error si quieres mostrar uno más específico
            String firebaseMessage = extractFirebaseErrorMessage(responseBody);

            throw new FirebaseLoginException("Error de autenticación Firebase: " + firebaseMessage, e);
        } catch (Exception e) {
            // Otros errores: red, JSON, etc.
            throw new FirebaseLoginException("Error inesperado al autenticar con Firebase", e);
        }
    }

    // Método auxiliar para extraer el mensaje de error de Firebase
    private String extractFirebaseErrorMessage(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorResponse = mapper.readValue(responseBody, Map.class);
            Map<String, Object> error = (Map<String, Object>) errorResponse.get("error");
            return (String) error.get("message");
        } catch (Exception ex) {
            return "Respuesta de error no procesable";
        }
    }
}