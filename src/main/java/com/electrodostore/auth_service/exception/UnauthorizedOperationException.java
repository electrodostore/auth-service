package com.electrodostore.auth_service.exception;

import lombok.Getter;

/**
 * Excepción para proteger los recursos
 * a los que quiera acceder un usuario no autorizado
 */
@Getter
public class UnauthorizedOperationException extends DomainException {
    private final ErrorCode errorCode;
    public UnauthorizedOperationException(String message) {

        super(message);

        errorCode = ErrorCode.UNAUTHORIZED_OPERATION;
    }
}
