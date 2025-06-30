package com.example.pedido_db.entity;

import com.example.pedido_db.dto.Producto;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "receta")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer productoId;  // ID del cliente que realizó el pedido
    @Transient
    private Producto producto;  // Relación con Cliente, que se llena mediante Feign


    @Column(name = "descripcion", nullable = true, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "cantidad", nullable = false, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal cantidad;  // Cambiado a BigDecimal

    @Column(name = "unidad_medida", nullable = true, length = 50)
    private String unidadMedida;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @Column(name = "cantidad_disponible", nullable = false, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal cantidadDisponible;  // Cambiado a BigDecimal

    public Receta() {

    }

    public BigDecimal convertirUnidad(BigDecimal cantidad, String unidadOrigen, String unidadDestino) {
        // Si las unidades son iguales, no se hace conversión
        if (unidadOrigen.equals(unidadDestino)) {
            return cantidad;
        }

        // Si la unidad de origen es "kg" y la de destino es "g", multiplicamos por 1000
        if (unidadOrigen.equals("kg") && unidadDestino.equals("g")) {
            return cantidad.multiply(BigDecimal.valueOf(1000));
        }

        // Si la unidad de origen es "g" y la de destino es "kg", dividimos por 1000
        else if (unidadOrigen.equals("g") && unidadDestino.equals("kg")) {
            return cantidad.divide(BigDecimal.valueOf(1000), 3, BigDecimal.ROUND_HALF_UP);  // Redondear a 3 decimales
        } else {
            // Si no se soporta la conversión, lanzar una excepción
            throw new IllegalArgumentException("Conversión no soportada de " + unidadOrigen + " a " + unidadDestino);
        }
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
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

    public BigDecimal getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(BigDecimal cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public Receta(Integer id, Integer productoId, Producto producto, String descripcion, BigDecimal cantidad, String unidadMedida, Menu menu, BigDecimal cantidadDisponible) {
        this.id = id;
        this.productoId = productoId;
        this.producto = producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.menu = menu;
        this.cantidadDisponible = cantidadDisponible;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id +
                ", productoId=" + productoId +
                ", producto=" + producto +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", unidadMedida='" + unidadMedida + '\'' +
                ", menu=" + menu +
                ", cantidadDisponible=" + cantidadDisponible +
                '}';
    }
}