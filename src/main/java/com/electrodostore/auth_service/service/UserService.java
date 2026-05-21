package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.user.UserRequestDto;
import com.electrodostore.auth_service.dto.user.UserResponseDto;
import com.electrodostore.auth_service.exception.InvalidRoleAssignmentException;
import com.electrodostore.auth_service.exception.UserNotFoundException;
import com.electrodostore.auth_service.exception.UsernameAlreadyExistsException;
import com.electrodostore.auth_service.model.UserSec;
import com.electrodostore.auth_service.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

//Service encargado de la lógica de negocio del dominio <<User>>
@Service
public class UserService implements IUserService {

    private final IUserRepository userRepo;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepo, RoleService roleService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //Método propio para validar si un username asignado a un usuario no existe ya en la base de datos
    private void validarUsername(String username){
        if (userRepo.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Ya existe usuario con username: " + username + ", intente con otro");
        }
    }

    //Construye el DTO de respuesta para exponer los datos de un usuario
    private UserResponseDto buildUserResponse(UserSec objUser) {

        return new UserResponseDto(objUser.getId(),
                objUser.getUsername(),
                //Transforma también los roles del usuario a DTOs de respuesta
                roleService.buildRolesResponse(
                        new ArrayList<>(objUser.getListRoles())),
                objUser.getClienteId(),
                objUser.isEnabled()
        );
    }

    //Construye una lista de DTOs de respuesta a partir de una lista de usuarios
    private List<UserResponseDto> buildUsersResponse(List<UserSec> listUsers) {

        List<UserResponseDto> usersResponse = new ArrayList<>();

        listUsers.forEach(
                user -> usersResponse.add(buildUserResponse(user))
        );

        return usersResponse;
    }

    //Busca un usuario por su ID o lanza excepción si no existe
    private UserSec findUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("No se encontró usuario con Id: " + id)
                );
    }


    //Valida que el usuario se encuentre habilitado
    private void validarDisponibilidadUser(UserSec user) {
        if (!user.isEnabled()) {
            throw new UserNotFoundException("No no encontró usuario con id: " + user.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAll() {
        return buildUsersResponse(userRepo.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findUserById(Long id) {
        return buildUserResponse(
                findUser(id)
        );
    }

    @Transactional
    @Override
    public UserResponseDto saveUser(UserRequestDto newUser) {

        //Verifica que el username no se encuentre registrado
        validarUsername(newUser.username());

        //El rol CLIENT solo puede registrarse mediante el flujo público de clientes
        if (newUser.rolesNames().contains("CLIENT")) {
            throw new InvalidRoleAssignmentException("Los usuarios con el rol CLIENTE deben registrarse a través del flujo de registro de clientes");
        }

        //Construcción de la entidad usuario
        UserSec objUser = new UserSec();

        objUser.setUsername(newUser.username());

        //La contraseña se almacena usando hash BCrypt
        objUser.setPassword(passwordEncoder.encode(newUser.password()));

        //Busca y asigna los roles enviados en la petición
        objUser.setListRoles(
                new LinkedHashSet<>(

                        roleService.findAllRolesByNames(new LinkedHashSet<>(newUser.rolesNames()))
                )
        );

        UserSec savedUser = userRepo.save(objUser);

        return buildUserResponse(savedUser);
    }

    @Transactional
    @Override
    public void disableUser(Long id) {

        UserSec objUser = findUser(id);

        //Deshabilitación lógica de la cuenta de usuario
        objUser.setEnabled(false);

    }

    @Transactional
    @Override
    public UserResponseDto addRolesToUser(Long userId, List<String> newRolesNames) {

        UserSec user = findUser(userId);

        validarDisponibilidadUser(user);

        //El rol CLIENT solo puede asignarse desde el flujo de registro de clientes
        if (newRolesNames.contains("CLIENT")) {
            throw new InvalidRoleAssignmentException("Los usuarios con rol 'CLIENT' deben registrarse a través del flujo de registro correspondiente");
        }

        //Busca y agrega los nuevos roles al usuario
        user.getListRoles().addAll(
                roleService.findAllRolesByNames(new LinkedHashSet<>(newRolesNames))
        );

        return buildUserResponse(user);
    }

    @Transactional
    @Override
    public UserResponseDto removeRolesFromUser(Long userId, List<String> removeRolesNames) {

        UserSec objUser = findUser(userId);

        validarDisponibilidadUser(objUser);

        //El rol CLIENT no puede removerse manualmente para evitar inconsistencias de autenticación
        if (removeRolesNames.contains("CLIENT")) {
            throw new InvalidRoleAssignmentException("Los usuarios con rol 'CLIENT' deben deshabilitarse por el flujo de deshabilitación de usuarios");
        }

        //Verifica que todos los roles enviados existan
        roleService.findAllRolesByNames(new LinkedHashSet<>(removeRolesNames));

        //Elimina del usuario todos los roles cuyos nombres estén en la lista recibida
        objUser.getListRoles().removeIf(

                role -> removeRolesNames.contains(role.getName())
        );

        return buildUserResponse(objUser);
    }
}