package com.electrodostore.auth_service.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoleRequestDto(
        @NotBlank String roleName,
        @NotNull List<@NotBlank String> permissions){}
