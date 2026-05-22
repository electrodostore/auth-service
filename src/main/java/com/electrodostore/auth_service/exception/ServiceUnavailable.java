package com.electrodostore.auth_service.exception;

//Informa al cliente ante un error en la comunicación con un servicio externo
public class ServiceUnavailable extends RuntimeException {
    public ServiceUnavailable(String message) {
        super(message);
    }
}
