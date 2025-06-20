package com.example.servicioventa.feing;

import com.example.servicioventa.dto.ClienteDTO;
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
    public List<PedidoDTO> buscarPedidosPorNombreCliente(String nombreCliente) {
        return Collections.singletonList(
                crearPedidoIndisponible("Datos de pedido no disponibles por el momento")
        );
    }

    @Override
    public void eliminarPedidoPorId(Long id) {
        System.out.println("⚠️ No se pudo eliminar el pedido: servicio no disponible.");
    }

    @Override
    public ClienteDTO obtenerClientePorId(Long id) {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id);
        cliente.setNombre("No disponible");
        cliente.setCorreo("sin-datos@temporal.com");
        return cliente;
    }

    @Override
    public List<ClienteDTO> buscarClientesPorNombre(String nombre) {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre("No disponible");
        cliente.setCorreo("N/A");
        return List.of(cliente);
    }

    @Override
    public PedidoDTO obtenerPedidoConDetalles(Integer id) {
        return null;
    }

    private PedidoDTO crearPedidoIndisponible(String mensaje) {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(null);
        pedido.setNombreCliente(mensaje);
        pedido.setEstadoPedido("desconocido");
        pedido.setDetalles(Collections.emptyList());
        return pedido;
    }
}
