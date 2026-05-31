package com.electrodostore.auth_service.service;

import com.electrodostore.auth_service.model.UserSec;
import com.electrodostore.auth_service.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepo;

    public UserDetailsServiceImp(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    //Valida existencia de usuario y consulta sus detalles en la base de datos
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Busca usuario por username y en caso de que no exista lanza AuthenticationException
        UserSec user = userRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("usuario o contraseña incorrecta")
                );

        List<GrantedAuthority> authorities = new ArrayList<>();

        /*Recorre la lista de roles del usuario y por cada elemento crea un objeto GrantedAuthority con el
         * prefijo "ROLE_" y el nombre del rol*/
        user.getListRoles().forEach(
                role -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getName()))
                )
        );

        /*Aplana las listas de permisos de cada rol, formando una única lista, la cual recorre y por cada permiso
         * crea un objeto GrantedAuthority con el nombre de esta y lo agrega a la lista de autoridades*/
        user.getListRoles()
                .stream()
                .flatMap(role -> role.getListPermissions().stream())
                .forEach(
                    permission -> authorities.add(
                            new SimpleGrantedAuthority(permission.getName())
                    )
                );

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNotExpired(),
                user.isCredentialNotExpired(),
                user.isAccountNotLocked(),
                authorities

        );
    }
}
