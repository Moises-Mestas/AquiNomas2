package com.example.servicioventa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Pedido {
    private Long id;
    private String nombreCliente;
    private DetallePedidoDTO detallePedido;
    private OffsetDateTime fechaPedido;
    private BigDecimal total;
    @JsonProperty("estadoPedido") // Mapea el nombre correcto desde JSON
    private String estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public DetallePedidoDTO getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedidoDTO detallePedido) {
        this.detallePedido = detallePedido;
    }

    public OffsetDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(OffsetDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", detallePedido=" + detallePedido +
                ", fechaPedido=" + fechaPedido +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}
