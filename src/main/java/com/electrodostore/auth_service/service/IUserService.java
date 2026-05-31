package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.user.*;

import java.util.List;

public interface IUserService {

    List<UserResponseDto> findAll();

    UserResponseDto findUserById(Long id);

    /**
     * Registro administrativo de usuarios.
     * NOTA: no se registran usuario que son clientes por este flujo
     */
    UserResponseDto saveUser(UserRequestDto newUser);

    /**
     * Método de registro público de clientes autenticables
     */
    UserResponseDto registerClientUser(ClientUserRequestDto clientUserDTO);

    void disableUser(Long id);

    UserResponseDto addRolesToUser(Long userId, List<String> newRolesNames);

    UserResponseDto removeRolesFromUser(Long userId, List<String> removeRolesNames);

    /**
     * La identidad del usuario autenticado se obtiene desde el SecurityContext
     * para evitar modificaciones o consultas sobre cuentas ajenas.
     */
    UserResponseDto findMe();
    UserResponseDto updateUsername(UpdateUsernameRequestDto objUpdateUsername);
    void updatePassword(UpdatePasswordRequestDto objUpdatePassword);
}
