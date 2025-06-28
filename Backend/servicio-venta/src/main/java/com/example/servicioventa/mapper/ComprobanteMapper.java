package com.example.servicioventa.mapper;

import com.example.servicioventa.dto.*;
import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.entity.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ComprobanteMapper {

    private final VentaMapper ventaMapper;

    public ComprobanteMapper(VentaMapper ventaMapper) {
        this.ventaMapper = ventaMapper;
    }

    public ComprobanteDTO aDTO(
            ComprobantePago entidad,
            String nombreCliente,
            PedidoDTO pedidoDTO
    ) {
        if (entidad == null) return null;

        ComprobanteDTO dto = new ComprobanteDTO();

        dto.setId(entidad.getId());
        dto.setTipo(entidad.getTipo());
        dto.setNumeroSerie(entidad.getNumeroSerie());
        dto.setNumeroComprobante(entidad.getNumeroComprobante());
        dto.setFechaEmision(entidad.getFechaEmision());
        dto.setRazonSocial(entidad.getRazonSocial());
        dto.setDireccionFiscal(entidad.getDireccionFiscal());

        Venta venta = entidad.getVenta();
        if (venta != null) {

            // 🧾 VentaDTO expandida
            VentaDTO ventaDTO = ventaMapper.aDTO(venta, nombreCliente, pedidoDTO);
            dto.setVenta(ventaDTO);

            // 🧮 IGV para FACTURA
            if (entidad.getTipo() == ComprobantePago.TipoComprobante.FACTURA) {
                BigDecimal neto = venta.getTotal().divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
                BigDecimal igv = venta.getTotal().subtract(neto);
                dto.setMontoNeto(neto);
                dto.setIgv(igv);
            } else {
                dto.setMontoNeto(venta.getTotal());
                dto.setIgv(BigDecimal.ZERO);
            }
        }

        return dto;
    }
}
