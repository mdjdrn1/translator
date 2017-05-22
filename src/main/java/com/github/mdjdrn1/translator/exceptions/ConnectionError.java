package com.github.mdjdrn1.translator.exceptions;

public class ConnectionError extends RuntimeException {
    public ConnectionError(String message) {
        super(message);
    }
}
