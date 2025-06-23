package com.example.servicioventa.feing;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "servicio-pedido", url = "http://localhost:9000", fallback = PedidoClientFallback.class)
public interface PedidoClient {

    @GetMapping("/pedidos/{id}")
    PedidoDTO obtenerPedidoPorId(@PathVariable Integer id);

    @GetMapping("/clientes/{id}")
    ClienteDTO obtenerClientePorId(@PathVariable Integer id);

    @GetMapping("/menus/{id}")
    MenuDTO obtenerMenuPorId(@PathVariable Integer id);

    @GetMapping("/pedidos/buscar-por-cliente")
    List<PedidoDTO> buscarPedidosPorNombreCliente(@RequestParam String nombre);

}
