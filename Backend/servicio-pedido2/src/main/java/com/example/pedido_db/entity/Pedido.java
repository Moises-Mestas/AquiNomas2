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



    @ManyToOne
    @JoinColumn(name = "detalle_pedido_id", referencedColumnName = "id", nullable = false)
    private DetallePedido detallePedido;


    @Column(name = "fecha_pedido", columnDefinition = "TIMESTAMP")
    private Timestamp fechaPedido; // Timestamp mapeado a java.sql.Timestamp

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false, columnDefinition = "ENUM('pendiente', 'iniciado', 'completado', 'cancelado') DEFAULT 'pendiente'")
    private EstadoPedido estadoPedido;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Evitar recursión infinita
    @JsonManagedReference // Evita la recursión infinita en la relación @OneToMany
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private List<DetallePedido> detalle; // Lista de detalles del pedido (productos asociados)

    public Pedido() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<DetallePedido> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePedido> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +

                ", detallePedido=" + detallePedido +
                ", fechaPedido=" + fechaPedido +
                ", estadoPedido=" + estadoPedido +
                ", detalle=" + detalle +
                '}';
    }

    public Pedido(Integer id, Integer administradorId, DetallePedido detallePedido, Timestamp fechaPedido, EstadoPedido estadoPedido, List<DetallePedido> detalle) {
        this.id = id;
        this.detallePedido = detallePedido;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detalle = detalle;
    }
}
