package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_venta", nullable = false, updatable = false)
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(name = "pedido_id", nullable = false)
    private Integer pedidoId;

    @ManyToOne
    @JoinColumn(name = "promociones_id", nullable = true)
    private Promocion promocion;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "metodo_pago", length = 100, nullable = false)
    private String metodoPago;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
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

    public Venta() {
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fechaVenta=" + fechaVenta +
                ", pedidoId=" + pedidoId +
                ", promocion=" + promocion +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }

    public Venta(Integer id, LocalDateTime fechaVenta, Integer pedidoId, Promocion promocion, BigDecimal total, String metodoPago) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.pedidoId = pedidoId;
        this.promocion = promocion;
        this.total = total;
        this.metodoPago = metodoPago;
    }
}
