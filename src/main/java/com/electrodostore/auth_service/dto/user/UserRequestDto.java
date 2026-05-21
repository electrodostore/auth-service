package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
 /**
  * Transporta los datos de los usuarios registrados por administradores
  * que no son clientes de negocio
  */
public record UserRequestDto(@NotBlank String username,
                             @NotBlank String password,
                            @NotEmpty List<@NotBlank String> rolesNames) {
}
