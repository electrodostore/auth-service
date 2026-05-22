package com.electrodostore.auth_service.dto.user;

import java.util.List;

public record UserLoginResponseDto(
        String username,
        List<String> authorities,
        String token
) {
}
