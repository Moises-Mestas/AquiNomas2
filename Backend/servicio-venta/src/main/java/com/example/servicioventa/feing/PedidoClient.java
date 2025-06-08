package com.example.servicioventa.feing;

import com.example.servicioventa.dto.Pedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pedido-service", url = "http://localhost:8081") // Ajusta la URL del servicio de pedidos
public interface PedidoClient {

    @GetMapping("/api/pedidos/{id}")
    Pedido obtenerPedidoPorId(@PathVariable Long id);
}