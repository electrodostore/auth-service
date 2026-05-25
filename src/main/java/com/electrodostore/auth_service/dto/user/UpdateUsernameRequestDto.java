package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameRequestDto(@NotBlank String password,
                                       @NotBlank String newUsername) {
}
