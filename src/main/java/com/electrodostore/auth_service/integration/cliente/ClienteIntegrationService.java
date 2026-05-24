package com.electrodostore.auth_service.integration.cliente;

import com.electrodostore.auth_service.dto.user.ClientRequestDto;
import com.electrodostore.auth_service.exception.ClienteNotFoundException;
import com.electrodostore.auth_service.exception.DomainException;
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

    private final ClienteFeignClient clienteFeignClient;

    public ClienteIntegrationService(ClienteFeignClient clienteFeignClient) {
        this.clienteFeignClient = clienteFeignClient;
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
        return clienteFeignClient.saveCliente(clienteNuevo);
    }

    public Long fallbackSaveCliente(ClientRequestDto clienteNuevo, Throwable ex){

        log.warn("Fallback activado para método saveCliente", ex);
        throw new ServiceUnavailable("No fue posible establecer la comunicación con cliente-service, " +
                "intente de nuevo mas tarde");
    }

    @CircuitBreaker(name = "cliente-service", fallbackMethod = "fallbackDisableClient")
    @Retry(name = "cliente-service")
    public void disableClient(Long clientId){
        clienteFeignClient.disableClient(clientId);
    }

    public void fallbackDisableClient(Long clientId, Throwable ex){

        //Si el error es conocido, se lanza la excepción de dominio
        if(ex instanceof DomainException e){
            throw e;
        }

        log.warn("Fallback activado para método disableCliente, clientId={}", clientId, ex);
        throw new ServiceUnavailable("No fue posible establecer la comunicación con cliente-service, " +
                "intente de nuevo mas tarde");

    }
}
