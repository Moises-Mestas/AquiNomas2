package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.DetallePedidoDto;
import com.upeu.servicioreporte.dto.PedidoDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PedidoDetalleClientFallback implements PedidoDetalleClient {

    @Override
    public List<DetallePedidoDto> obtenerTodosDetallePedidos() {
        System.err.println("Servicio 'servicio-pedido' - detalle pedidos no disponible");
        return Collections.emptyList(); // devolver lista vacía para no romper flujo
    }

    @Override
    public PedidoDto obtenerPedidoPorId(Integer id) {
        System.err.println("Servicio 'servicio-pedido' - pedido no disponible para id " + id);
        PedidoDto dto = new PedidoDto();
        dto.setId(id);
        return dto;
    }
}
