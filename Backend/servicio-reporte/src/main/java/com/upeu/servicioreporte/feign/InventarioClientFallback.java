package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.InventarioDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class InventarioClientFallback implements InventarioClient {

    private static final Logger logger = LoggerFactory.getLogger(InventarioClientFallback.class);

    @Override
    public List<InventarioDto> obtenerTodosInventarios() {
        logger.warn("Fallback activado: Inventario service no disponible");
        return Collections.emptyList();
    }
}
