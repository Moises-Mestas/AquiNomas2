package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private BigDecimal descuento;

    @Column(length = 255)
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Promocion() {
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descuento=" + descuento +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}