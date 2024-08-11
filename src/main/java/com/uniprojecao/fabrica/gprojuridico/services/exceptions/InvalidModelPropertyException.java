package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

import java.util.List;

public class InvalidModelPropertyException extends RuntimeException {
    public InvalidModelPropertyException(String modelProperty, String classModel, List<String> allowedTypes) {
        super("The allowed values for " + classModel + " are: " + allowedTypes + ", not: " + modelProperty);
    }
}
