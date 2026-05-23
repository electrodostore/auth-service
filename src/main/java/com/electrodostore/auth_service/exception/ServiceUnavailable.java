package com.electrodostore.auth_service.exception;

import lombok.Getter;

//Informa al cliente ante un error en la comunicación con un servicio externo
@Getter
public class ServiceUnavailable extends RuntimeException {
    private final ErrorCode errorCode;

    public ServiceUnavailable(String message) {
        super(message);

        this.errorCode = ErrorCode.SERVICE_UNAVAILABLE;
    }
}
