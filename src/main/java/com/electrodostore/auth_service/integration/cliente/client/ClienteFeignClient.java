package com.electrodostore.auth_service.integration.cliente.client;

import com.electrodostore.auth_service.dto.user.ClientRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Cliente Feign para comunicación con cliente-service
 */
@FeignClient(name = "cliente-service")
public interface ClienteFeignClient {

    @PostMapping("/clientes")
    Long saveCliente(@RequestBody ClientRequestDto newCliente);

    @DeleteMapping("/clientes/{clientId}")
    void disableClient(@PathVariable Long clientId);
}
