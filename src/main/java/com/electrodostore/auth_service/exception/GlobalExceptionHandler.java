package com.electrodostore.auth_service.exception;

import com.electrodostore.auth_service.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maneja de forma centralizada las excepciones de la API
 * y construye respuestas HTTP consistentes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Construye el cuerpo estándar para respuestas de error.
     */
    private ApiErrorResponse buildResponseError(HttpStatus status, String message){
        return new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlerPermissionNotFound(PermissionNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        buildResponseError(HttpStatus.NOT_FOUND, ex.getMessage())
                );
    }
}
