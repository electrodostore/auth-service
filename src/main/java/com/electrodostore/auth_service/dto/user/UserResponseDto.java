package com.electrodostore.auth_service.dto.user;

import com.electrodostore.auth_service.dto.role.RoleResponseDto;

import java.util.List;

public record UserResponseDto (Long id,
                               String RoleName,
                               List<RoleResponseDto> roles){
}
