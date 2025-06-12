package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventarioCocinaDto {
    private Integer id;
    private Integer bodegaId;
    private Integer productoId;
    private BigDecimal cantidadDisponible;
    private String unidadMedida;
    private BigDecimal stockMinimo;
    private LocalDateTime ultimaFechaEntrada;
    private LocalDateTime ultimaFechaSalida;

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

    public LocalDateTime getUltimaFechaEntrada() {
        return ultimaFechaEntrada;
    }

    public void setUltimaFechaEntrada(LocalDateTime ultimaFechaEntrada) {
        this.ultimaFechaEntrada = ultimaFechaEntrada;
    }

    public LocalDateTime getUltimaFechaSalida() {
        return ultimaFechaSalida;
    }

    public void setUltimaFechaSalida(LocalDateTime ultimaFechaSalida) {
        this.ultimaFechaSalida = ultimaFechaSalida;
    }

    @Override
    public String toString() {
        return "InventarioCocinaDto{" +
                "id=" + id +
                ", bodegaId=" + bodegaId +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", stockMinimo=" + stockMinimo +
                ", ultimaFechaEntrada=" + ultimaFechaEntrada +
                ", ultimaFechaSalida=" + ultimaFechaSalida +
                '}';
    }
}
