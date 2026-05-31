package com.electrodostore.auth_service.repository;

import com.electrodostore.auth_service.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de datos para entidad de usuarios
 */
@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {

    /**
     * Indica si un nombre de usuario
     * existe o no en la base de datos.
     */
    boolean existsByUsername(String username);

    /**
     * Consulta usuario por nombre de usuario
     */
    Optional<UserSec> findByUsername(String username);
}
