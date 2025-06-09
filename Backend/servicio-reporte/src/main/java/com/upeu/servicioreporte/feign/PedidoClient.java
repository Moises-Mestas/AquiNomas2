package com.upeu.servicioreporte.feign;

import com.upeu.servicioreporte.dto.PedidoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-pedido", fallback = PedidoClientFallback.class)
public interface PedidoClient {

    @GetMapping("/pedidos/{id}")
    PedidoDto obtenerPedidoPorId(@PathVariable("id") Integer id);
}
