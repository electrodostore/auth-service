package com.electrodostore.auth_service.exception;

import lombok.Getter;


@Getter
public class ClienteNotFoundException extends DomainException {
    private final ErrorCode errorCode;

    public ClienteNotFoundException(String message) {
        super(message);

        this.errorCode = ErrorCode.CLIENT_NOT_FOUND;
    }
}
