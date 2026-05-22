package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.user.UserLoginRequestDto;
import com.electrodostore.auth_service.dto.user.UserLoginResponseDto;
import com.electrodostore.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLogin){
        return ResponseEntity.ok(
                authService.login(userLogin)
        );
    }
}
