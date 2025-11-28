package com.dalficc_technologies.agendafinanciera.domain.exception;

public class FirebaseLoginException extends Exception {

    public FirebaseLoginException(String message) {
        super(message);
    }

    public FirebaseLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}