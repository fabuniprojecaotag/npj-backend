package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class InvalidReturnTypeException extends RuntimeException {
    public InvalidReturnTypeException(String returnType) {
        super("This returnType is invalid. returnType entered: \"" + returnType + "\". The available returnTypes are: min, autoComplete, forAssistido.");
    }
}
