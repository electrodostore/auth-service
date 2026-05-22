package com.electrodostore.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlerRoleNotFound(RoleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        buildResponseError(HttpStatus.NOT_FOUND, ex.getMessage())
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlerUserNotFound(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        buildResponseError(HttpStatus.NOT_FOUND, ex.getMessage())
                );
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlerUsernameAlreadyExists(UsernameAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        buildResponseError(HttpStatus.CONFLICT, ex.getMessage())
                );
    }

    @ExceptionHandler(InvalidRoleAssignmentException.class)
    public ResponseEntity<ApiErrorResponse> handlerInvalidRoleAssignment(InvalidRoleAssignmentException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        buildResponseError(HttpStatus.CONFLICT, ex.getMessage())
                );
    }

    @ExceptionHandler(ServiceUnavailable.class)
    public ResponseEntity<ApiErrorResponse> handlerServiceUnavailable(ServiceUnavailable ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(
                        buildResponseError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage())
                );
    }
}
