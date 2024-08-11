package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class NullPropertyException extends RuntimeException {
    public NullPropertyException(String property, String classModel) {
        super(property + " property cannot be null for the " + classModel + " model.");
    }
}
