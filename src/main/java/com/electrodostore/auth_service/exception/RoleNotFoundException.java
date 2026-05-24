package com.electrodostore.auth_service.exception;

import lombok.Getter;

@Getter
public class RoleNotFoundException extends DomainException {
    private final ErrorCode errorCode;

    public RoleNotFoundException(String message) {
        super(message);

        this.errorCode = ErrorCode.ROLE_NOT_FOUND;
    }
}
