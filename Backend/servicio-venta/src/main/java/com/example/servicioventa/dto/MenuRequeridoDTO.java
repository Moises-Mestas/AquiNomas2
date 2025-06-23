package com.example.servicioventa.dto;

import java.math.BigDecimal;

public class MenuRequeridoDTO {
    // Identificador de la relación (opcional, si lo necesitas)
    private Integer id;
    // ID del menú (obtendremos los detalles desde el servicio)
    private Integer menuId;
    // Cantidad requerida en la promoción
    private Integer cantidadRequerida;

    // Datos adicionales, que se obtendrán vía Feign
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;

    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMenuId() {
        return menuId;
    }
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
    public Integer getCantidadRequerida() {
        return cantidadRequerida;
    }
    public void setCantidadRequerida(Integer cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public BigDecimal getPrecio() {
        return precio;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public MenuRequeridoDTO() {
    }

    @Override
    public String toString() {
        return "MenuRequeridoDTO{" +
                "id=" + id +
                ", menuId=" + menuId +
                ", cantidadRequerida=" + cantidadRequerida +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}