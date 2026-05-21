package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.permission.PermissionRequestDto;
import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;
import com.electrodostore.auth_service.model.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//Interfaz para definir las operaciones de negocio de la entidad 'Permission'
public interface IPermissionService {

    List<PermissionResponseDto> buildPermissionsResponse(List<Permission> permissions);

    //Traer una lista de permisos por sus nombres
    List<Permission> findAllPermissionsByNames(Set<String> permissionNames);

    List<PermissionResponseDto> findAllPermissions();

    PermissionResponseDto findPermissionById(Long id);

    PermissionResponseDto savePermission(PermissionRequestDto newPermission);
}
