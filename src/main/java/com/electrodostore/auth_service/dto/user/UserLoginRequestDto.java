package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(@NotBlank String username,
                                  @NotBlank String password) {
}
