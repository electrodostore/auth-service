package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.permission.PermissionRequestDto;
import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;
import com.electrodostore.auth_service.exception.PermissionNotFoundException;
import com.electrodostore.auth_service.model.Permission;
import com.electrodostore.auth_service.repository.IPermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService implements IPermissionService {

    private final IPermissionRepository permissionRepo;

    public PermissionService(IPermissionRepository permissionRepo) {
        this.permissionRepo = permissionRepo;
    }

    /**
     * Construye el DTO de exposición para un permiso.
     */
    private PermissionResponseDto buildPermissionResponse(Permission permission) {
        return new PermissionResponseDto(
                permission.getId(),
                permission.getName()
        );
    }

    /**
     * Busca un permiso o lanza excepción si no existe.
     */
    private Permission findPermission(Long id) {
        return permissionRepo.findById(id)
                .orElseThrow(() ->
                        new PermissionNotFoundException(
                                "No se encontró permiso con id: " + id
                        )
                );
    }

    public List<PermissionResponseDto> buildPermissionsResponse(
            List<Permission> permissions) {

        return permissions.stream()
                .map(this::buildPermissionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PermissionResponseDto> findAllPermissions() {
        return buildPermissionsResponse(permissionRepo.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public PermissionResponseDto findPermissionById(Long id) {
        return buildPermissionResponse(findPermission(id));
    }

    @Transactional
    @Override
    public PermissionResponseDto savePermission(
            PermissionRequestDto newPermission) {

        Permission permission = new Permission(
                null,
                newPermission.PermissionName()
        );

        permissionRepo.save(permission);

        return buildPermissionResponse(permission);
    }


}