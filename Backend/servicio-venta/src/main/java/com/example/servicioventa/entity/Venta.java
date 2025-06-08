package com.example.servicioventa.entity;

import com.example.servicioventa.dto.Pedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pedido;

    private LocalDateTime fechaVenta;

    private BigDecimal total;

    @Column(nullable = false, length = 100)
    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "promociones_id")
    private Promocion promociones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Promocion getPromociones() {
        return promociones;
    }

    public void setPromociones(Promocion promociones) {
        this.promociones = promociones;
    }

    public Venta() {
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", pedido='" + pedido + '\'' +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                ", promociones=" + promociones +
                '}';
    }
}