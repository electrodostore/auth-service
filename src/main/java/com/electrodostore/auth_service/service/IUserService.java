package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.user.ClientUserRequestDto;
import com.electrodostore.auth_service.dto.user.UpdateUsernameRequestDto;
import com.electrodostore.auth_service.dto.user.UserRequestDto;
import com.electrodostore.auth_service.dto.user.UserResponseDto;

import java.util.List;

public interface IUserService {

    List<UserResponseDto> findAll();

    UserResponseDto findUserById(Long id);

    /**
     * Registrar usuarios administrativos.
     * NOTA: no se registran usuario que son clientes por este flujo
     */
    UserResponseDto saveUser(UserRequestDto newUser);

    /**
     * Método de registro público de usuarios, cuyos usuarios registrados serán
     * catalogados con rol CLIENTE automáticamente
     */
    UserResponseDto registerClientUser(ClientUserRequestDto clientUserDTO);

    //Soft Delete
    void disableUser(Long id);

    UserResponseDto addRolesToUser(Long userId, List<String> newRolesNames);

    UserResponseDto removeRolesFromUser(Long userId, List<String> removeRolesNames);

    /**
     * La identidad del usuario autenticado se obtiene desde el SecurityContext
     * para evitar modificaciones o consultas sobre cuentas ajenas.
     */
    //Método para consultar los datos del usuario que está autenticado en el contexto de seguridad (ownership)
    UserResponseDto findMe();
    //Actualizar username de usuario autenticado
    UserResponseDto updateUsername(UpdateUsernameRequestDto objUpdateUsername);
    //Actualizar contraseña de usuario autenticado
//    void updatePassword(UpdatePasswordRequestDto objUpdatePassword);
}
