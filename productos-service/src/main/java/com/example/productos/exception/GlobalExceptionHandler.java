package com.example.productos.exception;

import com.example.productos.jsonapi.JsonApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<JsonApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        JsonApiError error = new JsonApiError(
            "Recurso no encontrado",
            ex.getMessage(),
            String.valueOf(HttpStatus.NOT_FOUND.value())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
   @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiError> handleGeneralException(Exception ex) {
        JsonApiError error = new JsonApiError(
            "Error interno del servidor",
            "Ha ocurrido un error inesperado.",
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}