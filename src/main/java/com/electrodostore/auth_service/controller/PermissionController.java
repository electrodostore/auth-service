package com.electrodostore.auth_service.controller;

import com.electrodostore.auth_service.dto.permission.PermissionRequestDto;
import com.electrodostore.auth_service.dto.permission.PermissionResponseDto;
import com.electrodostore.auth_service.service.IPermissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final IPermissionService permissionService;
    public PermissionController(IPermissionService permissionService){
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> findAllPermissions(){
        return ResponseEntity.ok(permissionService.findAllPermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponseDto> findPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(permissionService.findPermissionById(id));
    }

    @PostMapping
    public ResponseEntity<PermissionResponseDto> savePermission(@Valid @RequestBody PermissionRequestDto newPermission){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.savePermission(newPermission));
    }

}