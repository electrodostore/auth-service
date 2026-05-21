package com.electrodostore.auth_service.dto.permission;

import jakarta.validation.constraints.NotBlank;

//DTO para transferir los datos de un permiso cuando se quiera hacer un registro de este
public record PermissionRequestDto(@NotBlank String PermissionName) {
}
