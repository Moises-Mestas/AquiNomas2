package com.example.pedido_db.dto;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class InventarioCocina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bodega_id")
    private Integer bodegaId;

    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "cantidad_disponible", columnDefinition = "DECIMAL(10,3)")
    private BigDecimal cantidadDisponible;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida")
    private UnidadMedida unidadMedida;


    public InventarioCocina() {
    }

    public enum UnidadMedida {
        kg, g, l, ml, unidad
    }

    // Getters and Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Integer bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public BigDecimal getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(BigDecimal cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public InventarioCocina(Integer id, Integer bodegaId, Integer productoId, BigDecimal cantidadDisponible, UnidadMedida unidadMedida) {
        this.id = id;
        this.bodegaId = bodegaId;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return "InventarioCocina{" +
                "id=" + id +
                ", bodegaId=" + bodegaId +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida=" + unidadMedida +
                '}';
    }
}
