package app.web.gprojuridico.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Não foi possível encontrar o recurso solicitado");
    }
}
