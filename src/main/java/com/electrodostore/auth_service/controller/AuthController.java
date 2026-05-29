package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.user.ClientUserRequestDto;
import com.electrodostore.auth_service.dto.user.UserLoginRequestDto;
import com.electrodostore.auth_service.dto.user.UserLoginResponseDto;
import com.electrodostore.auth_service.dto.user.UserResponseDto;
import com.electrodostore.auth_service.service.AuthService;
import com.electrodostore.auth_service.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final IUserService userService;

    public AuthController(AuthService authService, IUserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLogin){
        return ResponseEntity.ok(
                authService.login(userLogin)
        );
    }

    //Registro públicos de clientes autenticados
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerClientUser(@Valid @RequestBody ClientUserRequestDto newClientUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        userService.registerClientUser(newClientUser)
                );
    }
}
