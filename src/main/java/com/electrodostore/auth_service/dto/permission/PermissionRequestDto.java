package com.electrodostore.auth_service.dto.permission;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequestDto(
        @NotBlank String permissionName) {}
