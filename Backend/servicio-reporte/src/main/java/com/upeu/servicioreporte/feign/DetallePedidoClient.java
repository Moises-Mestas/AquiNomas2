package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.DetallePedidoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "servicio-pedido", fallback = DetallePedidoClientFallback.class)
public interface DetallePedidoClient {

    @GetMapping("/detalle-pedidos")
    List<DetallePedidoDto> obtenerTodosDetallePedidos();
}
