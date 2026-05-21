package com.electrodostore.auth_service.exception;

/**Excepción de infraestructura de seguridad para indicar que un nombre de usuario
 *  ya está dentro de un registro en la base de datos
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
