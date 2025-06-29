package com.upeu.servicioreporte.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class PedidoDto {
    private Integer id;
    private Integer clienteId;
    private ClienteDto cliente;
    private OffsetDateTime fechaPedido;
    private String estadoPedido;
    private List<DetallePedidoDto> detalles;

    // Getters y setters
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

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime  getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(OffsetDateTime  fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<DetallePedidoDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoDto> detalles) {
        this.detalles = detalles;
    }
}
