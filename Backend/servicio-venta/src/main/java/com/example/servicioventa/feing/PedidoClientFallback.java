package com.example.servicioventa.feing;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.PedidoDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PedidoClientFallback implements PedidoClient {

    @Override
    public PedidoDTO obtenerPedidoPorId(Integer id) {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(id);
        pedido.setNombreCliente("No disponible - Servicio de pedidos inactivo");
        pedido.setEstadoPedido("desconocido");
        pedido.setDetalles(Collections.emptyList());
        return pedido;
    }

    @Override
    public ClienteDTO obtenerClientePorId(Integer id) {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id);
        cliente.setNombre("No disponible");
        cliente.setCorreo("sin-datos@temporal.com");
        return cliente;
    }

    @Override
    public MenuDTO obtenerMenuPorId(Integer id) {
        return null;
    }

    @Override
    public List<PedidoDTO> buscarPedidosPorNombreCliente(String nombre) {
        return List.of();
    }
}
