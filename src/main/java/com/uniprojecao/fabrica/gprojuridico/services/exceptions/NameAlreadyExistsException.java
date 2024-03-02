package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class NameAlreadyExistsException extends RuntimeException {
    public NameAlreadyExistsException(String message) {
        super(message);
    }
}
