package com.example.servicioventa.dto;

import java.time.LocalDateTime;

public class Pedido {
    private Integer id;
    private Integer clienteId;
    private LocalDateTime fechaPedido;
    private EstadoPedido estadoPedido;

    // Enum para estado del pedido
    public enum EstadoPedido {
        CANCELADO, COMPLETADO, INICIADO, PENDIENTE
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}
