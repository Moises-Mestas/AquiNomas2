package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventarioBarraDto {
    private Integer id;
    private Integer bodegaId;
    private String nombreProducto;
    private BigDecimal cantidadDisponible;
    private String unidadMedida;
    private BigDecimal stockMinimo;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaBotellaAbierta;

    // Getters y Setters
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

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(BigDecimal cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDateTime getFechaBotellaAbierta() {
        return fechaBotellaAbierta;
    }

    public void setFechaBotellaAbierta(LocalDateTime fechaBotellaAbierta) {
        this.fechaBotellaAbierta = fechaBotellaAbierta;
    }

    @Override
    public String toString() {
        return "InventarioBarraDto{" +
                "id=" + id +
                ", bodegaId=" + bodegaId +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", stockMinimo=" + stockMinimo +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaBotellaAbierta=" + fechaBotellaAbierta +
                '}';
    }
}
