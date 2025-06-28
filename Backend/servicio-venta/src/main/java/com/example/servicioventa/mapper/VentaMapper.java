package com.example.servicioventa.mapper;

import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Venta;
import org.springframework.stereotype.Component;

@Component
public class VentaMapper {

    public VentaDTO aDTO(Venta venta, String nombreCliente, PedidoDTO pedidoDTO) {
        if (venta == null) return null;

        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setTotal(venta.getTotal());
        dto.setMetodoPago(venta.getMetodoPago().name());
        dto.setFechaVenta(venta.getFechaVenta()); // ya es OffsetDateTime
        dto.setNombreCliente(nombreCliente); // proporcionado por el servicio
        dto.setPedido(pedidoDTO); // proporcionado por el servicio
        dto.setPromociones(venta.getPromociones()); // directamente

        return dto;
    }
}