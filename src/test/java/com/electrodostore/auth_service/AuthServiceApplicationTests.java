package com.electrodostore.auth_service;

import com.electrodostore.auth_service.bootstrap.SystemBootstrap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceApplicationTests {

	@MockitoBean
	private JwtDecoder jwtDecoder;

	@MockitoBean
	private SystemBootstrap systemBootstrap;

	@Test
	void contextLoads() {
	}

}
