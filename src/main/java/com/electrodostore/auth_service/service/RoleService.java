package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.dto.role.RoleRequestDto;
import com.electrodostore.auth_service.dto.role.RoleResponseDto;
import com.electrodostore.auth_service.exception.RoleNotFoundException;
import com.electrodostore.auth_service.model.Role;
import com.electrodostore.auth_service.repository.IRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

//Service encargado de la lógica de negocio del dominio <<Role>>
@Service
public class RoleService implements IRoleService{

    private final IRoleRepository roleRepo;
    private final IPermissionService permissionService;

    public RoleService(IRoleRepository roleRepo, PermissionService permissionService){
        this.roleRepo = roleRepo;
        this.permissionService = permissionService;
    }

    //Construye el DTO de respuesta a partir de los datos de un rol
    private RoleResponseDto buildRoleResponse(Role objRole){
        return new RoleResponseDto(
                objRole.getId(), objRole.getName(),

                //Transforma también los permisos del rol a DTOs de respuesta
                permissionService.buildPermissionsResponse(
                        new ArrayList<>(objRole.getListPermissions())
                ),

                objRole.isActive()
        );
    }

    //Valida que el rol se encuentre activo
    private void validarEstadoDeRol(Role objRole){
        if(!objRole.isActive()){throw new RoleNotFoundException("No se encontró rol con id: " + objRole.getId() + " disponible");}
    }

    //Construye una lista de DTOs de respuesta a partir de una lista de roles
    public List<RoleResponseDto> buildRolesResponse(List<Role> listRoles){

        List<RoleResponseDto> rolesExponer = new ArrayList<>();

        listRoles.forEach(
                role -> rolesExponer.add(buildRoleResponse(role))
        );

        return rolesExponer;
    }

    //Busca un rol por su ID o lanza excepción si no existe
    private Role findRole(Long id){
        return roleRepo.findById(id)
                .orElseThrow(
                        () -> new RoleNotFoundException("No se encontró role con Id: " + id)
                );
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleResponseDto> findAllRoles() {
        return buildRolesResponse(roleRepo.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto findRoleById(Long id) {

        Role objRole = findRole(id);

        return buildRoleResponse(objRole);
    }

    @Transactional
    @Override
    public RoleResponseDto saveRole(RoleRequestDto newRole) {

        Role objRole = new Role(null, newRole.roleName(),
                new LinkedHashSet<>(

                        //Busca y asigna los permisos enviados en la petición
                        permissionService.findAllPermissionsByNames(new LinkedHashSet<>(newRole.permissions()))),

                true
        );

        Role persistedRole = roleRepo.save(objRole);

        return buildRoleResponse(persistedRole);
    }

    @Transactional
    @Override
    public void disableRole(Long id) {

        Role objRole = findRole(id);

        //Desactivación lógica del rol usando Soft Delete
        objRole.setActive(false);
    }

    @Transactional
    @Override
    public RoleResponseDto addPermissionsToRole(Long idRole, List<String> newPermissionsNames) {

        Role objRole = findRole(idRole);

        validarEstadoDeRol(objRole);

        //Agrega al rol los nuevos permisos encontrados
        objRole.getListPermissions().addAll(
                permissionService.findAllPermissionsByNames(new LinkedHashSet<>(newPermissionsNames))
        );

        return buildRoleResponse(objRole);
    }

    @Transactional
    @Override
    public RoleResponseDto removePermissionsFromRole(Long idRole, List<String> removePermissionsNames) {

        Role objRole = findRole(idRole);

        validarEstadoDeRol(objRole);

        //Verifica que todos los permisos enviados existan
        permissionService.findAllPermissionsByNames(new LinkedHashSet<>(removePermissionsNames));

        //Elimina del rol todos los permisos cuyos nombres estén en la lista recibida
        objRole.getListPermissions().removeIf(
                permission -> removePermissionsNames.contains(permission.getName())
        ) ;

        return buildRoleResponse(objRole);
    }
}