package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.role.RoleRequestDto;
import com.electrodostore.auth_service.dto.role.RoleResponseDto;
import com.electrodostore.auth_service.service.IRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Define endpoints o rutas de acceso a las operaciones de la entidad Role
@RestController
@RequestMapping("/auth/roles")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> findAllRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> findRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

    @PostMapping
    public ResponseEntity<RoleResponseDto> saveRole(@Valid @RequestBody RoleRequestDto newRole) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.saveRole(newRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableRole(@PathVariable Long id) {
        roleService.disableRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idRole}/add-permissions")
    public ResponseEntity<RoleResponseDto> addPermissionToRole(@PathVariable Long idRole, @RequestBody @NotEmpty List<@NotBlank String> newPermissionsNames){
        return ResponseEntity.ok(roleService.addPermissionsToRole(idRole, newPermissionsNames));
    }

    @DeleteMapping("/{idRole}/remove-permissions")
    public ResponseEntity<RoleResponseDto> deletePermissionsFromRole(@PathVariable Long idRole, @RequestBody @NotEmpty List<@NotBlank String> removePermissionsNames){
        return ResponseEntity.ok(
                roleService.removePermissionsFromRole(idRole, removePermissionsNames)
        );
    }
}
