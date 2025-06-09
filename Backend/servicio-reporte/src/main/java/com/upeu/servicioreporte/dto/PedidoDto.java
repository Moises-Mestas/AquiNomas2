package com.upeu.servicioreporte.dto;

import java.time.LocalDateTime;

public class PedidoDto {
    private Integer id;
    private Integer detallePedidoId;
    private String estadoPedido;
    private LocalDateTime fechaPedido;
    private Integer clienteId;

    // Constructor vacío
    public PedidoDto() {
    }

    // Constructor con todos los campos
    public PedidoDto(Integer id, Integer detallePedidoId, String estadoPedido, LocalDateTime fechaPedido, Integer clienteId) {
        this.id = id;
        this.detallePedidoId = detallePedidoId;
        this.estadoPedido = estadoPedido;
        this.fechaPedido = fechaPedido;
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDetallePedidoId() {
        return detallePedidoId;
    }

    public void setDetallePedidoId(Integer detallePedidoId) {
        this.detallePedidoId = detallePedidoId;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    // toString
    @Override
    public String toString() {
        return "PedidoDto{" +
                "id=" + id +
                ", detallePedidoId=" + detallePedidoId +
                ", estadoPedido='" + estadoPedido + '\'' +
                ", fechaPedido=" + fechaPedido +
                ", clienteId=" + clienteId +
                '}';
    }
}
