package com.example.servicioventa.feing;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.PedidoDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class PedidoClientFallback implements PedidoClient {

    @Override
    public PedidoDTO obtenerPedidoPorId(Integer id) {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(id);
        pedido.setEstadoPedido("DESCONOCIDO");
        pedido.setDetalles(Collections.emptyList());

        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(-1);
        cliente.setNombre("No disponible");
        cliente.setApellido("Desconocido");
        cliente.setDni("00000000");
        cliente.setEmail("sin-contacto@example.com");
        cliente.setTelefono("000-000-000");
        cliente.setDireccion("No registrada");
        cliente.setRuc("00000000000");

        pedido.setCliente(cliente);
        return pedido;
    }

    @Override
    public ClienteDTO obtenerClientePorId(Integer id) {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id != null ? id : -1);
        cliente.setNombre("No disponible");
        cliente.setApellido("Desconocido");
        cliente.setDni("00000000");
        cliente.setEmail("sin-contacto@example.com");
        cliente.setTelefono("000-000-000");
        cliente.setDireccion("No registrada");
        cliente.setRuc("00000000000");
        return cliente;
    }

    @Override
    public MenuDTO obtenerMenuPorId(Integer id) {
        MenuDTO menu = new MenuDTO();
        menu.setId(id != null ? id : -1);
        menu.setNombre("⚠️ No disponible");
        menu.setDescripcion("Información del menú no accesible en este momento.");
        menu.setPrecio(BigDecimal.ZERO);
        menu.setImagen("imagen-no-disponible.png");
        return menu;
    }

    @Override
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return Collections.emptyList();
    }
}
