package com.electrodostore.auth_service.dto.user;

import java.util.List;

/**
 * Expone información útil para el
 * usuario después de loguearse.
 * */
public record UserLoginResponseDto(
        String username,
        List<String> authorities,
        String token
) {
}
