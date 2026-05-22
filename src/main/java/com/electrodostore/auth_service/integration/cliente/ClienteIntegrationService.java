package com.electrodostore.auth_service.integration.cliente;

import com.electrodostore.auth_service.dto.user.ClientRequestDto;
import com.electrodostore.auth_service.exception.ServiceUnavailable;
import com.electrodostore.auth_service.integration.cliente.client.ClienteFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio de integración encargado de la comunicación con cliente-service.
 *
 * Usa patrones de resiliencia como Circuit Breaker y Retry
 * para manejar fallos temporales en la comunicación.
 */
@Slf4j
@Service
public class ClienteIntegrationService {

    private final ClienteFeignClient clienteClient;

    public ClienteIntegrationService(ClienteFeignClient clienteClient) {
        this.clienteClient = clienteClient;
    }

    /**
     * Protege la integración con cliente-service.
     *
     * Si la comunicación falla repetidamente, Circuit Breaker
     * interrumpe temporalmente las llamadas y ejecuta el método fallback.
     */
    @CircuitBreaker(name = "cliente-service", fallbackMethod = "fallbackSaveCliente")
    @Retry(name="cliente-service")
    public Long saveCliente(ClientRequestDto clienteNuevo){
        return clienteClient.saveCliente(clienteNuevo);
    }

    public Long fallbackSaveCliente(ClientRequestDto clienteNuevo, Throwable ex){

        log.warn("Fallback activado para método saveCliente", ex);
        throw new ServiceUnavailable("No fue posible establecer la comunicación con cliente-service, " +
                "intente de nuevo mas tarde");
    }
}
