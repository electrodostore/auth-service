package com.electrodostore.auth_service.dto.role;

import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;

import java.util.List;

public record RoleResponseDto(Long id, String name, List<PermissionResponseDto> permissions, boolean active) {
}
