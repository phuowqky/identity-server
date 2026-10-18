package com.example.identity_server.exception;

public class SsoException extends RuntimeException {
    public SsoException(String message) {
        super(message);
    }
}
