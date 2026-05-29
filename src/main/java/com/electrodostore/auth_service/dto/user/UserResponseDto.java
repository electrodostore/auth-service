package com.electrodostore.auth_service.dto.user;

import com.electrodostore.auth_service.dto.role.RoleResponseDto;

import java.util.List;

public record UserResponseDto (Long id,
                               String username,
                               List<RoleResponseDto> roles,
                               Long clientId,
                               boolean unabled){
}
