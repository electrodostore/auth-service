package com.electrodostore.auth_service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define reglas con base a las cuales se crea la cadena de filtros de seguridad
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(csrf -> csrf.disable())
                /**
                 * Habilita autenticación HTTP Basic.
                 */
                .httpBasic(Customizer.withDefaults())
                /**
                 * Configura autenticación sin estado (stateless).
                 */
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Inicialmente, todas las requests son públicas
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

                /**
                 * Configura el microservicio como OAuth2 Resource Server
                 * para validar automáticamente tokens JWT.
                 */
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                )

                .build();
    }

    /**
     * Gestiona los proveedores de autenticación y escoge el más adecuado teniendo
     * en cuenta al mecanismo de autenticación que se esté usando
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticación basado en username y password.
     * */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        //Define el algoritmo de hash usado para validar contraseñas.
        provider.setPasswordEncoder(passwordEncoder());
        //Service usado para cargar los datos del usuario autenticado
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }


}
