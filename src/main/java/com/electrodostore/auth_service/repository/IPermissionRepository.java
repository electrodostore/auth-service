package com.electrodostore.auth_service.repository;

import com.electrodostore.auth_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

//Contrato con Spring-Data-Jpa para acceder a los diferentes métodos de persistencia y consulta que se definen en la interfaz JpaRepository<>
@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Busca permisos por sus nombres
     */
    List<Permission> findAllByNameIn(Set<String> permissionName);
}
