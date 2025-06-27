package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BodegaDto {
    private Integer id;
    private Integer compraProveedorId;
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaMovimiento;
    private BigDecimal cantidad;
    private String unidadMedida;
    private String tipoInsumo;
    private String duracionInsumo;
    private String productoId;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompraProveedorId() {
        return compraProveedorId;
    }

    public void setCompraProveedorId(Integer compraProveedorId) {
        this.compraProveedorId = compraProveedorId;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(String tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public String getDuracionInsumo() {
        return duracionInsumo;
    }

    public void setDuracionInsumo(String duracionInsumo) {
        this.duracionInsumo = duracionInsumo;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    @Override
    public String toString() {
        return "BodegaDto{" +
                "id=" + id +
                ", compraProveedorId=" + compraProveedorId +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaMovimiento=" + fechaMovimiento +
                ", cantidad=" + cantidad +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", tipoInsumo='" + tipoInsumo + '\'' +
                ", duracionInsumo='" + duracionInsumo + '\'' +
                ", productoId='" + productoId + '\'' +
                '}';
    }
}
