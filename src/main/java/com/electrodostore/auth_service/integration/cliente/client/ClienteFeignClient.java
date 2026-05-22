package com.electrodostore.auth_service.integration.cliente.client;

import com.electrodostore.auth_service.dto.user.ClientRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Cliente Feign para comunicación con cliente-service
 */
@FeignClient(name = "cliente-service")
public interface ClienteFeignClient {

    @PostMapping("/clientes")
    Long saveCliente(ClientRequestDto newCliente);
}
