package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.BodegaDto;
import com.upeu.servicioreporte.dto.InventarioCocinaDto;
import com.upeu.servicioreporte.dto.InventarioBarraDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class InventarioClientFallback implements InventarioClient {

    private static final Logger logger = LoggerFactory.getLogger(InventarioClientFallback.class);

    @Override
    public List<BodegaDto> obtenerTodasLasBodegas() {
        logger.warn("Fallback activado: Servicio de Bodegas no disponible");
        return Collections.emptyList(); // Retorna una lista vacía en caso de error
    }

    @Override
    public List<InventarioCocinaDto> obtenerInventariosCocina() {
        logger.warn("Fallback activado: Servicio de Inventario Cocina no disponible");
        return Collections.emptyList(); // Retorna una lista vacía en caso de error
    }

    @Override
    public List<InventarioBarraDto> obtenerInventariosBarra() {
        logger.warn("Fallback activado: Servicio de Inventario Barra no disponible");
        return Collections.emptyList(); // Retorna una lista vacía en caso de error
    }
}
