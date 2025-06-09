package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.DetallePedidoDto;
import com.upeu.servicioreporte.dto.PedidoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "servicio-pedido", fallback = PedidoDetalleClientFallback.class)
public interface PedidoDetalleClient {

    @GetMapping("/detalle-pedidos")
    List<DetallePedidoDto> obtenerTodosDetallePedidos();

    @GetMapping("/pedidos/{id}")
    PedidoDto obtenerPedidoPorId(@PathVariable("id") Integer id);
}
