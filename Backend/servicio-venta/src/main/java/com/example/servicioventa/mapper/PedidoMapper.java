package com.example.servicioventa.mapper;

import com.example.servicioventa.dto.DetallePedidoDTO;
import com.example.servicioventa.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    @Autowired
    private ClienteMapper clienteMapper;

    public PedidoDTO aDTO(PedidoDTO pedido) {
        if (pedido == null) return null;

        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setFechaPedido(pedido.getFechaPedido().toInstant().atOffset(ZoneOffset.of("-05:00")));
        dto.setEstadoPedido(pedido.getEstadoPedido());
        dto.setCliente(clienteMapper.aDTO(pedido.getCliente()));
        dto.setDetalles(pedido.getDetalles().stream()
                .map(this::mapDetalle)
                .collect(Collectors.toList()));

        return dto;
    }

    private DetallePedidoDTO mapDetalle(DetallePedidoDTO detalle) {
        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        // ... completá según tus campos
        return dto;
    }
}
