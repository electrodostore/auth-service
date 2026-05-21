package com.electrodostore.auth_service.exception;

import java.time.LocalDateTime;

/**
 * Define la estructura del cuerpo
 * de las excepciones personalizadas de la API
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message
) {
}
