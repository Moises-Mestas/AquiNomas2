package com.example.pedido_db.entity;

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

    @Column(name = "detalle_pedido_id", nullable = false)
    private Integer detallePedidoId; // Solo el ID de detalle_pedido, no una relación directa

    @Transient  // Añadido para que no sea persistido directamente en la base de datos
    private DetallePedido detallePedido; // Agregado para contener el detalle completo del pedido (no solo el ID)

    @Column(name = "fecha_pedido", columnDefinition = "TIMESTAMP")
    private Timestamp fechaPedido; // Timestamp mapeado a java.sql.Timestamp

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false, columnDefinition = "ENUM('pendiente', 'iniciado', 'completado', 'cancelado') DEFAULT 'pendiente'")
    private EstadoPedido estadoPedido;

    public Pedido() {

    }

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

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
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

    public Pedido(Integer id, Integer detallePedidoId, DetallePedido detallePedido, Timestamp fechaPedido, EstadoPedido estadoPedido) {
        this.id = id;
        this.detallePedidoId = detallePedidoId;
        this.detallePedido = detallePedido;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", detallePedidoId=" + detallePedidoId +
                ", detallePedido=" + detallePedido +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido=" + estadoPedido +
                '}';
    }


}

