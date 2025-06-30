package com.example.pedido_db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class InventarioCocina {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("bodega_id")
    private Integer bodegaId;

    @JsonProperty("producto_id")
    private Integer productoId;

    @JsonProperty("cantidad_disponible")
    private BigDecimal cantidadDisponible;

    @JsonProperty("unidad_medida")
    private String unidadMedida; // cambia de Enum a String para que no falle al deserializar

    public InventarioCocina() {

    }

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

    public InventarioCocina(Integer id, Integer bodegaId, Integer productoId, BigDecimal cantidadDisponible, String unidadMedida) {
        this.id = id;
        this.bodegaId = bodegaId;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return "InventarioCocinaDTO{" +
                "id=" + id +
                ", bodegaId=" + bodegaId +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida='" + unidadMedida + '\'' +
                '}';
    }
}