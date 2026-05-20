package com.electrodostore.auth_service.repository;

import com.electrodostore.auth_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Contrato con Spring-Data-Jpa para acceder a los diferentes métodos de persistencia y consulta que se definen en la interfaz JpaRepository<>
@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
