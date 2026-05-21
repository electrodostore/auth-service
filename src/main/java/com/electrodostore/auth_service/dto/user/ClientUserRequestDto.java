package com.electrodostore.auth_service.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Transporta los datos de los usuarios que se registran como clientes
 */
public record ClientUserRequestDto(@NotBlank String username,
                                   @NotBlank String password,
                                   @NotNull @Valid ClientRequestDto client){
}
