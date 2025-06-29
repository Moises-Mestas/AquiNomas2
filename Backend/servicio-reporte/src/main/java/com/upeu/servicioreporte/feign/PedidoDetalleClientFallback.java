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
        return Collections.emptyList();
    }

    @Override
    public PedidoDto obtenerPedidoPorId(Integer id) {
        return null;
    }

    @Override
    public List<PedidoDto> obtenerTodosPedidos() { // ← tipo actualizado
        return Collections.emptyList();
    }
}
