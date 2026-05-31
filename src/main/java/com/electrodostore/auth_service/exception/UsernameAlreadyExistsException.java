package com.electrodostore.auth_service.exception;

import lombok.Getter;

/**
 * Excepción de infraestructura de seguridad para indicar que un nombre de usuario
 *  ya está dentro de un registro en la base de datos
 */
@Getter
public class UsernameAlreadyExistsException extends DomainException {
    private final ErrorCode errorCode;

    public UsernameAlreadyExistsException(String message) {
        super(message);

        this.errorCode = ErrorCode.USERNAME_ALREADY_EXISTS;
    }
}
