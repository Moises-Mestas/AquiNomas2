package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.VentaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class VentaClientFallback implements VentaClient {

    private static final Logger logger = LoggerFactory.getLogger(VentaClientFallback.class);

    @Override
    public List<VentaDto> obtenerTodasVentas() {
        logger.warn("Fallback activado: Venta service no disponible para obtener todas las ventas");
        return Collections.emptyList();
    }

    @Override
    public List<VentaDto> obtenerVentasPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        logger.warn("Fallback activado: Venta service no disponible para rango {} - {}", inicio, fin);
        return Collections.emptyList();
    }
}
