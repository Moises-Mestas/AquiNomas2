package com.example.servicioventa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promociones")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_descuento", nullable = false)
    private TipoDescuento tipoDescuento;  // MONTO o PORCENTAJE

    @Column(name = "valorDescuento", precision = 10, scale = 2)
    private BigDecimal valorDescuento; // Monto fijo o porcentaje (ej. 10.00 = 10%)

    @Column(name = "cantidad_minima", nullable = true)
    private Integer cantidadMinima; // Opcional: aplica si se compran >= X unidades

    @Column(name = "monto_minimo", precision = 10, scale = 2)
    private BigDecimal montoMinimo;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    //@OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MenuPromocion> menu = new ArrayList<>();

    public enum TipoDescuento {
        MONTO, PORCENTAJE
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoDescuento getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(TipoDescuento tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<MenuPromocion> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuPromocion> menu) {
        this.menu = menu;
    }

    public Promocion() {
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoDescuento=" + tipoDescuento +
                ", valorDescuento=" + valorDescuento +
                ", cantidadMinima=" + cantidadMinima +
                ", montoMinimo=" + montoMinimo +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", menu=" + menu +
                '}';
    }
}
