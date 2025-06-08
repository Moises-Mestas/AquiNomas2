package com.example.serviciopedido.entity;

import com.example.serviciopedido.dto.AdministradorDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id", nullable = false)
    private AdministradorDTO administrador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_pedido_id", nullable = false)
    private DetallePedido detallePedido;

    @Column(name = "fecha_pedido", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaPedido;

    @Column(name = "estado_pedido", nullable = false)
    private String estadoPedido;

    public Pedido() {

    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AdministradorDTO getAdministrador() {
        return administrador;
    }

    public void setAdministrador(AdministradorDTO administrador) {
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

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Pedido(Integer id, AdministradorDTO administrador, DetallePedido detallePedido, Timestamp fechaPedido, String estadoPedido) {
        this.id = id;
        this.administrador = administrador;
        this.detallePedido = detallePedido;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", administrador=" + administrador +
                ", detallePedido=" + detallePedido +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido='" + estadoPedido + '\'' +
                '}';
    }
}
