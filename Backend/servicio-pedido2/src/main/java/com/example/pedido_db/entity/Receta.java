package com.example.pedido_db.entity;

import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.dto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "receta")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private Producto Producto;  // Relación con Cliente, que se llena mediante Feign
    private Integer productoId;

    @Column(name = "descripcion", nullable = true, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad", nullable = false, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal cantidad;  // Cambiado a BigDecimal

    @Column(name = "unidad_medida", nullable = true, length = 50)
    private String unidadMedida;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    public Receta() {

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Receta(Integer id, Integer productoId, com.example.pedido_db.dto.Producto producto, String descripcion, BigDecimal cantidad, String unidadMedida, Menu menu) {
        this.id = id;
        this.productoId = productoId;
        Producto = producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", Producto=" + Producto +
                ", productoId=" + productoId +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", menu=" + menu +
                '}';
    }
}
