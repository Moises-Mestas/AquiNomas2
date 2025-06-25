package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class VentaDto {

    private Integer id;
    private Integer pedidoId;
    private OffsetDateTime fechaVenta;
    private BigDecimal total;
    private String metodoPago;

    // Constructor vacío
    public VentaDto() {}

    // Constructor completo
    public VentaDto(Integer id, Integer pedidoId, OffsetDateTime fechaVenta, BigDecimal total, String metodoPago) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public OffsetDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(OffsetDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
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

    // toString
    @Override
    public String toString() {
        return "VentaDto{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
