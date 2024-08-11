package com.uniprojecao.fabrica.gprojuridico.controllers.handlers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.uniprojecao.fabrica.gprojuridico.dto.ErrorResponse;
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

    @ExceptionHandler(InstantiationException.class)
    public ResponseEntity<ErrorResponse> handleInstantiationException(InstantiationException e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message =
                "O objeto passado não pode ser instanciado. " +
                "Verifique se a classe passada: (1) não é Abstrata, (2) se possui um construtor público sem " +
                "argumentos ou (3), se for classe interna, se é estática";
        ErrorResponse error = new ErrorResponse(status.value(), message);

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ReflectionOperationException.class)
    public ResponseEntity<ErrorResponse> handleReflectionOperationException(ReflectionOperationException e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> tokenExpiredException(TokenExpiredException e) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UserAlreadyCreatedException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyCreatedException(UserAlreadyCreatedException e) {

        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse err = new ErrorResponse(status.value(), "Dados inválidos");
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse error = new ErrorResponse(status.value(), "Validation failed");

        for (var constraint : e.getConstraintViolations()) {
            error.addError(constraint.getPropertyPath().toString(), constraint.getMessage());
        }

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidModelPropertyException.class)
    public ResponseEntity<ErrorResponse> handleInvalidModelPropertyException(InvalidModelPropertyException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NullPropertyException.class)
    public ResponseEntity<ErrorResponse> handleNullPropertyException(NullPropertyException e) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage());

        return ResponseEntity.status(status).body(error);
    }
}
