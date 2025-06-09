package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.ClienteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClienteClientFallback implements ClienteClient {

    private static final Logger logger = LoggerFactory.getLogger(ClienteClientFallback.class);

    @Override
    public ClienteDto obtenerClientePorId(Integer id) {
        logger.warn("Fallback activado: Cliente service no disponible para ID: {}", id);

        ClienteDto fallback = new ClienteDto();
        fallback.setId(id);
        fallback.setNombre("Servicio de Cliente no disponible");
        fallback.setApellido("Intente más tarde");
        fallback.setEmail("N/A");
        return fallback;
    }
}