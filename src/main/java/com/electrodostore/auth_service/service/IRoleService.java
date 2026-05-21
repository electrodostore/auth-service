package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;
import com.electrodostore.auth_service.dto.role.RoleRequestDto;
import com.electrodostore.auth_service.dto.role.RoleResponseDto;
import com.electrodostore.auth_service.model.Permission;

import java.util.List;

/**
 * Define las operaciones de lógica de negocio de la entidad Role que serán usadas
 * por diferentes clientes (controllers, otros service, etc.)
 */
public interface IRoleService {

    List<RoleResponseDto> findAllRoles();

    RoleResponseDto findRoleById(Long id);

    RoleResponseDto saveRole(RoleRequestDto newRole);

    //Soft Delete
    void disableRole(Long id);

    RoleResponseDto addPermissionsToRole(Long idRole, List<String> newPermissionsNames);

    RoleResponseDto removePermissionsFromRole(Long idRole, List<String> removePermissionsNames);

}
