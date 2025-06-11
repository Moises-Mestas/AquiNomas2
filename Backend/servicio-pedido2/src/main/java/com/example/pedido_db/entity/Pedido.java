package com.example.pedido_db.entity;

import com.example.pedido_db.dto.Cliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer clienteId;  // ID del cliente que realizó el pedido
    @Transient
    private Cliente cliente;  // Relación con Cliente, que se llena mediante Feign


    @Column(name = "fecha_pedido", columnDefinition = "TIMESTAMP")
    private Timestamp fechaPedido; // Timestamp mapeado a java.sql.Timestamp

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false, columnDefinition = "ENUM('pendiente', 'iniciado', 'completado', 'cancelado') DEFAULT 'pendiente'")
    private EstadoPedido estadoPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference  // Evitar problemas con referencias circulares
    private List<DetallePedido> detalles;  // Relación con DetallePedido

    public Pedido() {

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public Pedido(Integer id, Integer clienteId, Cliente cliente, Timestamp fechaPedido, EstadoPedido estadoPedido, List<DetallePedido> detalles) {
        this.id = id;
        this.clienteId = clienteId;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", cliente=" + cliente +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido=" + estadoPedido +
                ", detalles=" + detalles +
                '}';
    }
}

