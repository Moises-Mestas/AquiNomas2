package com.example.servicioventa.feing;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "SERVICIO-PEDIDO2", url = "http://localhost:9000", fallback = PedidoClientFallback.class)
public interface PedidoClient {


    @GetMapping("/pedidos/{id}")
    PedidoDTO obtenerPedidoPorId(@PathVariable Integer id);

    @GetMapping("/pedidos/buscar")
    List<PedidoDTO> buscarPedidosPorNombreCliente(@RequestParam String nombreCliente);

    @DeleteMapping("/pedidos/{id}")
    void eliminarPedidoPorId(@PathVariable Long id);

    @GetMapping("/clientes/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable Long id);

    @GetMapping("/clientes/buscar")
    List<ClienteDTO> buscarClientesPorNombre(@RequestParam String nombre);

    @GetMapping("/pedidos/{id}/detalle-completo")
    PedidoDTO obtenerPedidoConDetalles(@PathVariable("id") Integer id);


}
