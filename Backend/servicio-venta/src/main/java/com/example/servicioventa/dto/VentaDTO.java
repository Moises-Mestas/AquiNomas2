package com.example.servicioventa.dto;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class VentaDTO {
    private Integer id;
    private BigDecimal total;
    private String metodoPago;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime fechaVenta;
    private String nombreCliente;
    private PedidoDTO pedido;
    private List<Promocion> promociones;
    private int maximoPromocionesPermitidas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public OffsetDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(OffsetDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(List<Promocion> promociones) {
        this.promociones = promociones;
    }

    public int getMaximoPromocionesPermitidas() {
        return maximoPromocionesPermitidas;
    }

    public void setMaximoPromocionesPermitidas(int maximoPromocionesPermitidas) {
        this.maximoPromocionesPermitidas = maximoPromocionesPermitidas;
    }

    public VentaDTO(Venta venta, PedidoDTO pedido) {
        this.id = venta.getId();
        this.total = venta.getTotal();
        this.metodoPago = venta.getMetodoPago() != null ? venta.getMetodoPago().name() : null;
        this.fechaVenta = venta.getFechaVenta();
        this.promociones = venta.getPromociones();
        this.maximoPromocionesPermitidas = venta.getMaximoPromocionesPermitidas() != null
                ? venta.getMaximoPromocionesPermitidas() : 1;
        this.pedido = pedido;

        // ✅ Extraer el nombre directamente del objeto cliente
        this.nombreCliente = (pedido.getCliente() != null && pedido.getCliente().getNombre() != null)
                ? pedido.getCliente().getNombre()
                : "Desconocido";
    }

    @Override
    public String toString() {
        return "VentaDTO{" +
                "id=" + id +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                ", fechaVenta=" + fechaVenta +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", pedido=" + pedido +
                ", promociones=" + promociones +
                ", maximoPromocionesPermitidas=" + maximoPromocionesPermitidas +
                '}';
    }
}