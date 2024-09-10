package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class InvalidCollectionNameException extends RuntimeException {
    public InvalidCollectionNameException() {
        super("Collection name invalid. Checks if the collection exists.");
    }
}
