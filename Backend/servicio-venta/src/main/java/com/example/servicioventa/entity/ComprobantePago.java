package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    private LocalDateTime fechaEmision;

    @Column(nullable = false, length = 50)
    private String tipoComprobante;

    private BigDecimal monto;

    @Column(nullable = false, length = 20)
    private String numeroComprobante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public ComprobantePago() {
    }

    @Override
    public String toString() {
        return "ComprobantePago{" +
                "id=" + id +
                ", venta=" + venta +
                ", fechaEmision=" + fechaEmision +
                ", tipoComprobante='" + tipoComprobante + '\'' +
                ", monto=" + monto +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                '}';
    }
}