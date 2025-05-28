package br.com.khadijeelzein.accountmanager.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.DateTimeException;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class,ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> illegalArgumentException(Exception ex) {
        String error = "Requisição inválida";
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> duplicateKeyException(DuplicateKeyException ex) {
        String error = "Valor deve ser único";
        var errorResponse = new ErrorResponse(HttpStatus.CONFLICT,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> nullPointerException(NullPointerException ex) {
        String error = "Valor não pode ser nulo";
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> exception(Exception ex) {
        String error = "Erro inesperado";
        var errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler({HttpMediaTypeException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> httpMediaTypeException(RuntimeException ex) {
        String error = "Tipo de parametro não aceito";
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> dateTimeException(DateTimeException ex) {
        String error = "Data Inválida";
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> noResourceFoundException(NoResourceFoundException ex) {
        String error = "URL Inválida";
        var errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,ex.getLocalizedMessage(),error);
        return new ResponseEntity<>(
                errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}