package com.electrodostore.auth_service.exception;

import lombok.Getter;

@Getter
public class PermissionNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public PermissionNotFoundException(String message) {

        super(message);
        this.errorCode = ErrorCode.PERMISSION_NOT_FOUND;
    }
}
