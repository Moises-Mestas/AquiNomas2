package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

// -----------------------------
// BodegaDto
// -----------------------------
public class BodegaDto {
    private int id;
    private int compraProveedorId;
    private Timestamp fechaEntrada;
    private Timestamp fechaMovimiento;
    private BigDecimal cantidad;
    private String unidadMedida;
    private String tipoInsumo;
    private String duracionInsumo;
    private String productoId;

    public BodegaDto() {}

    public BodegaDto(int id, int compraProveedorId, Timestamp fechaEntrada, Timestamp fechaMovimiento, BigDecimal cantidad, String unidadMedida, String tipoInsumo, String duracionInsumo, String productoId) {
        this.id = id;
        this.compraProveedorId = compraProveedorId;
        this.fechaEntrada = fechaEntrada;
        this.fechaMovimiento = fechaMovimiento;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.tipoInsumo = tipoInsumo;
        this.duracionInsumo = duracionInsumo;
        this.productoId = productoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCompraProveedorId() { return compraProveedorId; }
    public void setCompraProveedorId(int compraProveedorId) { this.compraProveedorId = compraProveedorId; }
    public Timestamp getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(Timestamp fechaEntrada) { this.fechaEntrada = fechaEntrada; }
    public Timestamp getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Timestamp fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public String getTipoInsumo() { return tipoInsumo; }
    public void setTipoInsumo(String tipoInsumo) { this.tipoInsumo = tipoInsumo; }
    public String getDuracionInsumo() { return duracionInsumo; }
    public void setDuracionInsumo(String duracionInsumo) { this.duracionInsumo = duracionInsumo; }
    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }

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