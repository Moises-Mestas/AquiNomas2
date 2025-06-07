package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ComprobantePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoComprobante tipo;

    @Column(name = "numeroSerie", length = 10, nullable = false)
    private String numeroSerie;

    @Column(name = "numeroComprobante", length = 15, nullable = false)
    private String numeroComprobante;

    @Column(name = "fechaEmision", nullable = false, updatable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(name = "igv", precision = 10, scale = 2)
    private BigDecimal igv;

    @Column(name = "montoNeto", precision = 10, scale = 2)
    private BigDecimal montoNeto;

    // Enum para tipo de comprobante
    public enum TipoComprobante {
        BOLETA, FACTURA
    }
}
