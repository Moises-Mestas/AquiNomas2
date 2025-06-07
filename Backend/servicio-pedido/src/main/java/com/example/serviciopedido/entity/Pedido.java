package com.example.serviciopedido.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Column(name = "fecha_pedido", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false, columnDefinition = "ENUM('pendiente', 'iniciado', 'completado', 'cancelado') DEFAULT 'pendiente'")
    private EstadoPedido estadoPedido;

    public Pedido() {
        // Default constructor
    }

    public Pedido(Integer id, Integer clienteId, java.sql.Timestamp fechaPedido, EstadoPedido estadoPedido) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
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

    public java.sql.Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(java.sql.Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido=" + estadoPedido +
                '}';
    }
}
