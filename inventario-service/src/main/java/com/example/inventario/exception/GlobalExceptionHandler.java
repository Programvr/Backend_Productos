package com.example.inventario.exception;

import com.example.inventario.jsonapi.JsonApiError;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<JsonApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        JsonApiError error = new JsonApiError(
                "Resource Not Found",
                ex.getMessage(),
                "404"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<JsonApiError> handleFeignException(FeignException ex) {
        JsonApiError error = new JsonApiError(
                "External Service Error",
                ex.getMessage(),
                String.valueOf(ex.status())
        );
        HttpStatus status = ex.status() > 0 ? HttpStatus.valueOf(ex.status()) : HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiError> handleGeneralException(Exception ex) {
        JsonApiError error = new JsonApiError(
                "Internal Server Error",
                ex.getMessage(),
                "500"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}