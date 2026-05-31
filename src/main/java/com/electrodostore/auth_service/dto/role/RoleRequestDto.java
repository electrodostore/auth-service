package com.electrodostore.auth_service.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RoleRequestDto(
        @NotBlank String roleName,
        @NotEmpty List<@NotBlank String> permissions){}
