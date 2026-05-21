package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.permission.PermissionRequestDto;
import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;

import java.util.List;

//Interfaz para definir las operaciones de negocio de la entidad 'Permission'
public interface IPermissionService {

    List<PermissionResponseDto> findAllPermissions();

    PermissionResponseDto findPermissionById(Long id);

    PermissionResponseDto savePermission(PermissionRequestDto newPermission);
}
