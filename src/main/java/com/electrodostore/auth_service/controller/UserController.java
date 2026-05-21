package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.user.UserRequestDto;
import com.electrodostore.auth_service.dto.user.UserResponseDto;
import com.electrodostore.auth_service.service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Controlador para recibir las request de componente Users
@RestController
@RequestMapping("/users")
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

    //Registro administrativo de usuarios
    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserRequestDto newUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(newUser));
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