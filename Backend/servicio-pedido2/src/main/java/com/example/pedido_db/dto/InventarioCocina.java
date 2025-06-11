package com.example.pedido_db.dto;



import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class InventarioCocina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Transient
    private Producto Producto;  // Relación con Cliente, que se llena mediante Feign
    private Integer productoId;

    @Column(name = "cantidad_disponible", nullable = true, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal cantidadDisponible;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = true)
    private UnidadMedida unidadMedida;  // Enum para las unidades de medida


    // Constructor vacío
    public InventarioCocina() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.example.pedido_db.dto.Producto getProducto() {
        return Producto;
    }

    public void setProducto(com.example.pedido_db.dto.Producto producto) {
        Producto = producto;
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

    public InventarioCocina(Integer id, com.example.pedido_db.dto.Producto producto, Integer productoId, BigDecimal cantidadDisponible, UnidadMedida unidadMedida) {
        this.id = id;
        Producto = producto;
        this.productoId = productoId;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
    }

    @Override
    public String toString() {
        return "InventarioCocina{" +
                "id=" + id +
                ", Producto=" + Producto +
                ", productoId=" + productoId +
                ", cantidadDisponible=" + cantidadDisponible +
                ", unidadMedida=" + unidadMedida +
                '}';
    }

    // Enum para las unidades de medida
    public enum UnidadMedida {
        KG, G, L, ML, UNIDAD
    }

}
