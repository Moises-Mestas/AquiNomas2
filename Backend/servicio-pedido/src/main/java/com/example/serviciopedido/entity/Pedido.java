package com.example.serviciopedido.entity;


import com.example.serviciopedido.dto.Administrador;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne  // Relación con Administrador
    @JoinColumn(name = "administrador_id", referencedColumnName = "id", nullable = false)
    private Administrador administrador;  // Cambiado a la entidad Administrador

    @ManyToOne
    @JoinColumn(name = "detalle_pedido_id", referencedColumnName = "id", nullable = false)
    private DetallePedido detallePedido;

    @Column(name = "fecha_pedido", nullable = true, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = true, columnDefinition = "ENUM('PENDIENTE', 'INICIADO', 'COMPLETADO', 'CANCELADO') DEFAULT 'PENDIENTE'")
    private EstadoPedido estadoPedido;

    public Pedido() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
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

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", administrador=" + administrador +
                ", detallePedido=" + detallePedido +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido=" + estadoPedido +
                '}';
    }
}


