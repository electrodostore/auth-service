package com.electrodostore.auth_service.exception;

import lombok.Getter;

/**
 * Excepción para proteger de usuario no autorizados
 * el acceso a los recursos privados.
 */
@Getter
public class UnauthorizedOperationException extends DomainException {
    private final ErrorCode errorCode;
    public UnauthorizedOperationException(String message) {

        super(message);

        errorCode = ErrorCode.UNAUTHORIZED_OPERATION;
    }
}
