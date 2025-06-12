package com.upeu.servicioreporte.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoDto {
    private Integer id;
    private Integer clienteId;
    private ClienteDto cliente;
    private LocalDateTime fechaPedido;
    private String estadoPedido;
    private List<DetallePedidoDto> detalles;

    // Constructor vacío
    public PedidoDto() {
    }

    // Constructor con todos los campos
    public PedidoDto(Integer id, Integer clienteId, ClienteDto cliente, LocalDateTime fechaPedido, String estadoPedido, List<DetallePedidoDto> detalles) {
        this.id = id;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detalles = detalles;
    }

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

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
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

    // toString
    @Override
    public String toString() {
        return "PedidoDto{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", cliente=" + cliente +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido='" + estadoPedido + '\'' +
                ", detalles=" + detalles +
                '}';
    }
}
