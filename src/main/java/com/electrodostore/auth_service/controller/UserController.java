package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.user.*;
import com.electrodostore.auth_service.service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador para recibir las request de componente Users
@RestController
@RequestMapping("/auth/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
        return ResponseEntity.ok(
                userService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long id){
        return ResponseEntity.ok(
                userService.findUserById(id)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findMe(){
        return ResponseEntity.ok(
                userService.findMe()
        );
    }

    @PostMapping("/update-username")
    public ResponseEntity<UserResponseDto> updateMyUsername(@Valid @RequestBody UpdateUsernameRequestDto updateUsername){
        return ResponseEntity.ok(
                userService.updateUsername(updateUsername)
        );
    }

    @PostMapping("/update-password")
    public ResponseEntity<UserResponseDto> updateMyPassword(@Valid @RequestBody UpdatePasswordRequestDto updatePassword){
        userService.updatePassword(updatePassword);
        return ResponseEntity.noContent().build();
    }

    //Registro administrativo de usuarios
    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto newUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(newUser));
    }

    //Registro públicos de clientes autenticables
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> saveClientUser(@Valid @RequestBody ClientUserRequestDto newClientUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        userService.registerClientUser(newClientUser)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableUser(@PathVariable Long id){
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/add-roles")
    public ResponseEntity<UserResponseDto> addRolesToUser(@PathVariable Long userId, @RequestBody @NotEmpty List<@NotBlank String> newRolesNames){
        return ResponseEntity.ok(userService.addRolesToUser(userId, newRolesNames));
    }

    @DeleteMapping("/{userId}/remove-roles")
    public ResponseEntity<UserResponseDto> removeRoles(@PathVariable Long userId, @RequestBody @NotEmpty List<@NotBlank String> rolesNames){
        return ResponseEntity.ok(userService.removeRolesFromUser(userId, rolesNames));
    }

}