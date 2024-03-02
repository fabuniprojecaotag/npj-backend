package com.uniprojecao.fabrica.gprojuridico.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Não foi possível encontrar o recurso solicitado");
    }
}
