package com.electrodostore.auth_service.exception;

/**
 * SuperExcepción personalizada usada para identificar excepciones de dominio.
 */
public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
