package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class InventarioDto {
    private Integer id;
    private Integer bodegaId;
    private String nombreProducto; // Solo para barra
    private Integer productoId;    // Solo para cocina
    private BigDecimal cantidadDisponible;
    private String unidadMedida;
    private BigDecimal stockMinimo;
    private Timestamp fechaEntrada;
    private Timestamp fechaBotellaAbierta; // Solo barra
    private Timestamp ultimaFechaSalida;   // Solo cocina

    public InventarioDto() {}

    public InventarioDto(Integer id, Integer bodegaId, String nombreProducto, Integer productoId,
                         BigDecimal cantidadDisponible, String unidadMedida,
                         BigDecimal stockMinimo, Timestamp fechaEntrada,
                         Timestamp fechaBotellaAbierta, Timestamp ultimaFechaSalida) {
        this.id = id;
        this.bodegaId = bodegaId;
        this.nombreProducto = nombreProducto;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
        this.stockMinimo = stockMinimo;
        this.fechaEntrada = fechaEntrada;
        this.fechaBotellaAbierta = fechaBotellaAbierta;
        this.ultimaFechaSalida = ultimaFechaSalida;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBodegaId() { return bodegaId; }
    public void setBodegaId(Integer bodegaId) { this.bodegaId = bodegaId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public BigDecimal getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(BigDecimal cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(BigDecimal stockMinimo) { this.stockMinimo = stockMinimo; }

    public Timestamp getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(Timestamp fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public Timestamp getFechaBotellaAbierta() { return fechaBotellaAbierta; }
    public void setFechaBotellaAbierta(Timestamp fechaBotellaAbierta) { this.fechaBotellaAbierta = fechaBotellaAbierta; }

    public Timestamp getUltimaFechaSalida() { return ultimaFechaSalida; }
    public void setUltimaFechaSalida(Timestamp ultimaFechaSalida) { this.ultimaFechaSalida = ultimaFechaSalida; }

    @Override
    public String toString() {
        return "InventarioDto{" +
                "id=" + id +
                ", bodegaId=" + bodegaId +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", stockMinimo=" + stockMinimo +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaBotellaAbierta=" + fechaBotellaAbierta +
                ", ultimaFechaSalida=" + ultimaFechaSalida +
                '}';
    }
}
