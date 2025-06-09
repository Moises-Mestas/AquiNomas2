package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.DetallePedidoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DetallePedidoClientFallback implements DetallePedidoClient {

    private static final Logger logger = LoggerFactory.getLogger(DetallePedidoClientFallback.class);

    @Override
    public List<DetallePedidoDto> obtenerTodosDetallePedidos() {
        logger.warn("Fallback activado: DetallePedido service no disponible");
        return Collections.emptyList();
    }
}
