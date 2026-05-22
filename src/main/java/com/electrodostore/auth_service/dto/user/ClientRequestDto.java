package com.electrodostore.auth_service.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO encargado de transportar los datos de negocio de
 * los usuarios que se registran como clientes
 */
public record ClientRequestDto(@NotBlank  String name,
                               @NotBlank @Pattern(regexp = "\\d+") String cellphone,
                               @NotBlank @Pattern(regexp = "\\d+") String document,
                               @NotBlank String address) {
}
