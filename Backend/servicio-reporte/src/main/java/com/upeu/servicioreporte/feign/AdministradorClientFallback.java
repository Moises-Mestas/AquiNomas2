package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.AdministradorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdministradorClientFallback implements AdministradorClient {

    private static final Logger logger = LoggerFactory.getLogger(AdministradorClientFallback.class);

    @Override
    public AdministradorDto obtenerAdministradorPorId(Integer id) {
        logger.warn("Fallback activado: Administrador service no disponible para ID: {}", id);

        AdministradorDto fallback = new AdministradorDto();
        fallback.setId(id);
        fallback.setNombre("Servicio no disponible");
        fallback.setEmail("N/A");
        return fallback;
    }
}
