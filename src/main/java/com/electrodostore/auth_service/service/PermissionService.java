package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.permission.PermissionRequestDto;
import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;
import com.electrodostore.auth_service.exception.PermissionNotFoundException;
import com.electrodostore.auth_service.model.Permission;
import com.electrodostore.auth_service.repository.IPermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Override
    public List<PermissionResponseDto> buildPermissionsResponse(
            List<Permission> permissions) {

        return permissions.stream()
                .map(this::buildPermissionResponse)
                .toList();
    }

    //Método para consultar una lista de permisos por sus ids
    @Transactional(readOnly = true)
    @Override
    public List<Permission> findAllPermissionsByNames(Set<String> rolesNames){
        List<Permission> foundPermissions = permissionRepo.findAllByNameIn(rolesNames);

        //En caso de que no se encuentren todos los permisos que se consultaron, excepción indicándolo
        if(foundPermissions.size() < rolesNames.size()){throw new PermissionNotFoundException("Uno o varios permisos no fueron encontrados");}

        return foundPermissions;
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
                newPermission.permissionName()
        );

        permissionRepo.save(permission);

        return buildPermissionResponse(permission);
    }


}