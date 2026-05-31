package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.role.RoleRequestDto;
import com.electrodostore.auth_service.dto.role.RoleResponseDto;
import com.electrodostore.auth_service.model.Role;

import java.util.List;
import java.util.Set;

public interface IRoleService {

    List<RoleResponseDto> buildRolesResponse(List<Role> listRoles);

    List<Role> findAllRolesByNames(Set<String> rolesNames);

    List<RoleResponseDto> findAllRoles();

    RoleResponseDto findRoleById(Long id);

    RoleResponseDto saveRole(RoleRequestDto newRole);

    void disableRole(Long id);

    RoleResponseDto addPermissionsToRole(Long idRole, List<String> newPermissionsNames);

    RoleResponseDto removePermissionsFromRole(Long idRole, List<String> removePermissionsNames);

}
