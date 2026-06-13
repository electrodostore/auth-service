package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.user.*;
import com.electrodostore.auth_service.exception.InvalidRoleAssignmentException;
import com.electrodostore.auth_service.exception.UnauthorizedOperationException;
import com.electrodostore.auth_service.exception.UserNotFoundException;
import com.electrodostore.auth_service.exception.UsernameAlreadyExistsException;
import com.electrodostore.auth_service.integration.cliente.ClienteIntegrationService;
import com.electrodostore.auth_service.model.Role;
import com.electrodostore.auth_service.model.UserSec;
import com.electrodostore.auth_service.repository.IUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepo;
    private final IRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ClienteIntegrationService clienteIntegration;

    public UserService(IUserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder, ClienteIntegrationService clienteIntegration) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.clienteIntegration = clienteIntegration;
    }

    /**
     * válida que un nombre de usuario no se duplique
     * para mantener la consistencia de la lógica
     * de negocio.
     * */
    private void validarUsername(String username){
        if (userRepo.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Ya existe usuario con username: " + username + ", intente con otro");
        }
    }

    /**
     * Construye DTO de respuesta usado para exponer usuarios
     */
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

    /**
     * Construye una lista de DTOs de respuesta de usuarios
     */
    private List<UserResponseDto> buildUsersResponse(List<UserSec> listUsers) {

        List<UserResponseDto> usersResponse = new ArrayList<>();

        listUsers.forEach(
                user -> usersResponse.add(buildUserResponse(user))
        );

        return usersResponse;
    }

    /**
     * Busca un usuario por su ID o lanza excepción si no existe
     */
    private UserSec findUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException("No se encontró usuario con Id: " + id)
                );
    }


    /**
     * Valida que el usuario se encuentre habilitado.
     */
    private void validarDisponibilidadUser(UserSec user) {
        if (!user.isEnabled()) {
            throw new UserNotFoundException("Usuario con id: " + user.getId() + " no disponible");
        }
    }

    /**
     * Obtiene la identidad del usuario autenticado desde el contexto de seguridad
     * y retorna su id de registro
     */
    private Long getAuthenticatedUserId(){
        //Busca autenticación actual del usuario
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Recupera la identidad del usuario
        Jwt principal = (Jwt) authentication.getPrincipal();

        //Busca claim con el id del usuario
        Number userId = principal.getClaim("userId");

        return userId.longValue();
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

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findMe() {

        UserSec user = findUser(
                getAuthenticatedUserId()
        );

        return buildUserResponse(user);
    }

    @Transactional
    @Override
    public UserResponseDto updateUsername(UpdateUsernameRequestDto objUpdateUsername) {
        //Busca al usuario que se encuentra autenticado
        UserSec user = findUser(
                getAuthenticatedUserId()
        );

        //Verifica que las contraseñas coincidan y actualiza el username del usuario
        if(passwordEncoder.matches(objUpdateUsername.password(), user.getPassword())){
            user.setUsername(objUpdateUsername.newUsername());

            return buildUserResponse(user);
        }

        throw new UnauthorizedOperationException("Contraseña incorrecta");
    }

    @Transactional
    @Override
    public void updatePassword(UpdatePasswordRequestDto objUpdatePassword) {
        UserSec user = findUser(
          getAuthenticatedUserId()
        );

        //Verifica que las contraseñas coincidan
        boolean verifier = passwordEncoder.matches(objUpdatePassword.currentPassword(), user.getPassword());

        if(!verifier){
            throw new UnauthorizedOperationException("Contraseña incorrecta");
        }

        user.setPassword(
                passwordEncoder.encode(objUpdatePassword.newPassword())
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

        UserSec objUser = new UserSec();

        objUser.setUsername(newUser.username());

        //La contraseña se almacena usando hash BCrypt
        objUser.setPassword(passwordEncoder.encode(newUser.password()));

        List<Role> listRoles = roleService.findAllRolesByNames(
                new LinkedHashSet<>(newUser.rolesNames())
        );

        //Asigna los roles enviados en la petición
        objUser.setListRoles(
                new LinkedHashSet<>(
                        listRoles
                )
        );

        UserSec savedUser = userRepo.save(objUser);

        return buildUserResponse(savedUser);
    }

    @Override
    public UserResponseDto registerClientUser(ClientUserRequestDto clientUserDTO) {
        //Verificar que el username no se encuentre registrado
        validarUsername(clientUserDTO.username());

        //Registra cliente en cliente-service
        Long clientId = clienteIntegration.saveCliente(
                clientUserDTO.client()
        );

        UserSec newUser = new UserSec();

        newUser.setUsername(clientUserDTO.username());
        newUser.setPassword(passwordEncoder.encode(clientUserDTO.password()));
        newUser.setClienteId(clientId);

        //Asigna rol "CLIENT" al usuario
        newUser.setListRoles(
                new LinkedHashSet<>(
                        roleService.findAllRolesByNames(Set.of("CLIENT"))
                )
        );

        return buildUserResponse(
                userRepo.save(newUser)
        );

    }

    @Transactional
    @Override
    public void disableUser(Long id) {

        UserSec objUser = findUser(id);

        //Si el usuario es cliente, debemos deshabilitar la identidad de negocio de este
        if(objUser.getClienteId() != null){
            clienteIntegration.disableClient(objUser.getClienteId());
        }

        //Deshabilitación de la cuenta del usuario
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

        List<Role> listRoles = roleService.findAllRolesByNames(
                new LinkedHashSet<>(newRolesNames)
        );

        //Agrega los nuevos roles al usuario
        user.getListRoles().addAll(
                listRoles
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