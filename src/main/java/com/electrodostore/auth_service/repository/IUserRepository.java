package com.electrodostore.auth_service.repository;

import com.electrodostore.auth_service.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {

    //Método que Spring-Data-Jpa interpreta para verificar sin un username está registrado y asociado a algún usuario
    boolean existsByUsername(String username);
}
