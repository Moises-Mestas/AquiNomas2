package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.PedidoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PedidoClientFallback implements PedidoClient {

    private static final Logger logger = LoggerFactory.getLogger(PedidoClientFallback.class);

    @Override
    public PedidoDto obtenerPedidoPorId(Integer id) {
        logger.warn("Fallback activado: Pedido service no disponible para obtener pedido con ID {}", id);
        return null;
    }
}
