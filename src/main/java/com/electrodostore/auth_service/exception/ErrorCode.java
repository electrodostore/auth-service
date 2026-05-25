package com.electrodostore.auth_service.exception;

//Identificadores de errores de dominio expuestos por el servicio
public enum ErrorCode {

    ROLE_NOT_FOUND,
    PERMISSION_NOT_FOUND,
    USER_NOT_FOUND,
    INVALID_ROLE_ASSIGNMENT,
    USERNAME_ALREADY_EXISTS,
    SERVICE_UNAVAILABLE,
    CLIENT_NOT_FOUND,
    UNAUTHORIZED_OPERATION

}
