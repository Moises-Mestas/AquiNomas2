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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = true)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoComprobante tipo;

    public enum TipoComprobante {
        BOLETA, FACTURA
    }

    @Column(name = "razonSocial", length = 100, nullable = true)
    private String razonSocial;

    @Column(name = "direccionFiscal", length = 255, nullable = true)
    private String direccionFiscal;

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

    ////////////////////
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
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

    public ComprobantePago() {
    }

    @Override
    public String toString() {
        return "ComprobantePago{" +
                "id=" + id +
                ", venta=" + venta +
                ", tipo=" + tipo +
                ", razonSocial='" + razonSocial + '\'' +
                ", direccionFiscal='" + direccionFiscal + '\'' +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", igv=" + igv +
                ", montoNeto=" + montoNeto +
                '}';
    }
}