package app.web.gprojuridico.services.exceptions;

public class ReflectionOperationException extends RuntimeException {
    public ReflectionOperationException() {
        super("O objeto passado não pôde ser convertido usando reflection.");
    }
}
