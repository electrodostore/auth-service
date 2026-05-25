package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequestDto(@NotBlank String currentPassword,
                                       @NotBlank String newPassword) {
}
