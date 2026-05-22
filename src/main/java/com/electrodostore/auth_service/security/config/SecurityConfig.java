package com.electrodostore.auth_service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(csrf -> csrf.disable())
                /**
                 * Mecánismo de autenticación que estableces que las credenciales del
                 * usuario se envían en cada request
                 */
                .httpBasic(Customizer.withDefaults())
                /**
                 * Define la política de creación de sesión como STATELESS, lo que significa
                 * que el servidor no almacena ningún tipo de estado del usuario
                 */
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Inicialmente, definimos a todas las request como publicas
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

}
