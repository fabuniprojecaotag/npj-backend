package com.uniprojecao.fabrica.gprojuridico.controllers.handlers;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.ReflectionOperationException;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.ResourceNotFoundException;
import com.uniprojecao.fabrica.gprojuridico.dto.ErrorResponse;
import com.uniprojecao.fabrica.gprojuridico.services.exceptions.UserAlreadyCreatedException;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ErrorResponse> handleInstantiationException(InstantiationException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message =
                "O objeto passado não pode ser instanciado. " +
                "Verifique se a classe passada: (1) não é Abstrata, (2) se possui um construtor público sem " +
                "argumentos ou (3), se for classe interna, se é estática";
        ErrorResponse error = new ErrorResponse(status.value(), message, request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ReflectionOperationException.class)
    public ResponseEntity<ErrorResponse> handleReflectionOperationException(ReflectionOperationException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> tokenExpiredException(TokenExpiredException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UserAlreadyCreatedException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyCreatedException(UserAlreadyCreatedException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse error = new ErrorResponse(status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse err = new ErrorResponse(status.value(), "Dados inválidos", request.getRequestURI());
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse error = new ErrorResponse(status.value(), "Validation failed", request.getRequestURI());

        for (var constraint : e.getConstraintViolations()) {
            error.addError(constraint.getPropertyPath().toString(), constraint.getMessage());
        }

        return ResponseEntity.status(status).body(error);
    }
}
