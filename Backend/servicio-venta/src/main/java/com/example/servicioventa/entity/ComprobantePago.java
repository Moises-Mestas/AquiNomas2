package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "comprobante_pago")
public class ComprobantePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = true)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoComprobante tipo;

    public enum TipoComprobante {
        BOLETA, FACTURA
    }

    @Column(name = "numeroSerie", nullable = false, length = 10)
    private String numeroSerie;

    @Column(name = "numeroComprobante", nullable = false, length = 15)
    private String numeroComprobante;

    @Column(name = "fechaEmision")
    private LocalDateTime fechaEmision;

    @Column(precision = 10, scale = 2)
    private BigDecimal igv;

    @Column(name = "montoNeto", precision = 10, scale = 2)
    private BigDecimal montoNeto;

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

    public TipoComprobante getTipo() {
        return tipo;
    }

    public void setTipo(TipoComprobante tipo) {
        this.tipo = tipo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getMontoNeto() {
        return montoNeto;
    }

    public void setMontoNeto(BigDecimal montoNeto) {
        this.montoNeto = montoNeto;
    }

    @Override
    public String toString() {
        return "ComprobantePago{" +
                "id=" + id +
                ", venta=" + venta +
                ", tipo=" + tipo +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", igv=" + igv +
                ", montoNeto=" + montoNeto +
                '}';
    }

    public ComprobantePago() {
    }

    public ComprobantePago(Long id, Venta venta, TipoComprobante tipo, String numeroSerie, String numeroComprobante, LocalDateTime fechaEmision, BigDecimal igv, BigDecimal montoNeto) {
        this.id = id;
        this.venta = venta;
        this.tipo = tipo;
        this.numeroSerie = numeroSerie;
        this.numeroComprobante = numeroComprobante;
        this.fechaEmision = fechaEmision;
        this.igv = igv;
        this.montoNeto = montoNeto;
    }
}