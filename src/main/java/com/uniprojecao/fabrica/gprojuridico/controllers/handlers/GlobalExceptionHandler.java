package com.uniprojecao.fabrica.gprojuridico.controllers.handlers;

import com.uniprojecao.fabrica.gprojuridico.dto.error.ErrorResponseDTO;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCollectionNameException.class)
    public ResponseEntity<?> handleInvalidCollectionNameException(InvalidCollectionNameException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidReturnTypeException.class)
    public ResponseEntity<?> handleInvalidReturnTypeException(InvalidReturnTypeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InstantiationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInstantiationException(InstantiationException e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message =
                "O objeto passado não pode ser instanciado. " +
                "Verifique se a classe passada: (1) não é Abstrata, (2) se possui um construtor público sem " +
                "argumentos ou (3), se for classe interna, se é estática";
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), message);

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ReflectionOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleReflectionOperationException(ReflectionOperationException e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UserAlreadyCreatedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyCreatedException(UserAlreadyCreatedException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValid(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponseDTO err = new ErrorResponseDTO(status.value(), "Dados inválidos");
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), "Validation failed");

        for (var constraint : e.getConstraintViolations()) {
            error.addError(constraint.getPropertyPath().toString(), constraint.getMessage());
        }

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidModelPropertyException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidModelPropertyException(InvalidModelPropertyException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NullPropertyException.class)
    public ResponseEntity<ErrorResponseDTO> handleNullPropertyException(NullPropertyException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO error = new ErrorResponseDTO(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }
}
