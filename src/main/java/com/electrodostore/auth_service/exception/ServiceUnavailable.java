package com.electrodostore.auth_service.exception;

import lombok.Getter;

/**
 * Excepción personalizada para indicar que hubo
 * un problema de infraestructura en la integración
 * con los microservicios,
 */
@Getter
public class ServiceUnavailable extends RuntimeException {
    private final ErrorCode errorCode;

    public ServiceUnavailable(String message) {
        super(message);

        this.errorCode = ErrorCode.SERVICE_UNAVAILABLE;
    }
}
