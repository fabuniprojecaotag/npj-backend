package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class InvalidFilterException extends RuntimeException {
    public InvalidFilterException() {
        super("Invalid Filter. Check the passed field and/or value.");
    }
}
