package com.electrodostore.auth_service.exception;

import lombok.Getter;

// Enviar una Response de error para informar que un rol no puede ser asignado a un usuario
@Getter
public class InvalidRoleAssignmentException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidRoleAssignmentException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_ROLE_ASSIGNMENT;
    }
}
