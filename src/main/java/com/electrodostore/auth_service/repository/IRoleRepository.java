package com.electrodostore.auth_service.repository;

import com.electrodostore.auth_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

//Repositorio de persistencia y consulta basado en la interfaz definida por Spring Data: JpaRepository<>
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    //Busca roles por sus nombres
    List<Role> findAllByNameIn(Set<String> roleNames);
}
