package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.BodegaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BodegaClientFallback implements BodegaClient {

    private static final Logger logger = LoggerFactory.getLogger(BodegaClientFallback.class);

    @Override
    public List<BodegaDto> obtenerTodasLasBodegas() {
        logger.warn("Fallback activado: Bodega service no disponible");
        return Collections.emptyList();
    }
}
