package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * Transporta los datos de usuarios que deseen
 * iniciar sesión o loguearse en el sistema.
 */
public record UserLoginRequestDto(@NotBlank String username,
                                  @NotBlank String password) {
}
