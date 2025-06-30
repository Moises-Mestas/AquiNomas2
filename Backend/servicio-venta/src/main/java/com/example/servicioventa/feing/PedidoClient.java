package com.example.servicioventa.feing;

import com.example.servicioventa.dto.Pedido;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "SERVICIO-PEDIDO2", url = "http://localhost:9000") // Ajusta la URL según tu configuración
public interface PedidoClient {

    // Obtener pedido por ID
    @GetMapping("/pedidos/{id}")
    Pedido obtenerPedidoPorId(@PathVariable Long id);

    // Buscar pedidos por nombre del cliente
    @GetMapping("/pedidos/buscar")
    List<Pedido> buscarPedidosPorNombreCliente(@RequestParam String nombreCliente);

    // Eliminar pedido por ID
    @DeleteMapping("/pedidos/{id}")
    void eliminarPedidoPorId(@PathVariable Long id);
}