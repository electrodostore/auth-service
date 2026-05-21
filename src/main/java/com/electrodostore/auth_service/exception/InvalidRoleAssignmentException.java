package com.electrodostore.auth_service.exception;

// Enviar una Response de error para informar que un rol no puede ser asignado a un usuario
public class InvalidRoleAssignmentException extends RuntimeException {
    public InvalidRoleAssignmentException(String message) {
        super(message);
    }
}
