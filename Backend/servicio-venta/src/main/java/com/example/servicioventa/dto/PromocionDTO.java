package com.example.servicioventa.dto;

import com.example.servicioventa.entity.MenuPromocion;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PromocionDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipoDescuento; // "PORCENTAJE" o "MONTO"
    private BigDecimal valorDescuento;
    private Integer cantidadMinima;
    private BigDecimal montoMinimo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @JsonProperty("menu")  // Aquí se mapea la propiedad JSON "menu"
    private List<MenuRequeridoDTO> menuRequerido; // Lo que se expone al cliente

    // Getters y setters...

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
    public String getTipoDescuento() {
        return tipoDescuento;
    }
    public void setTipoDescuento(String tipoDescuento) {
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
    public List<MenuRequeridoDTO> getMenuRequerido() {
        return menuRequerido;
    }
    public void setMenuRequerido(List<MenuRequeridoDTO> menuRequerido) {
        this.menuRequerido = menuRequerido;
    }

    @Override
    public String toString() {
        return "PromocionDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoDescuento='" + tipoDescuento + '\'' +
                ", valorDescuento=" + valorDescuento +
                ", cantidadMinima=" + cantidadMinima +
                ", montoMinimo=" + montoMinimo +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", menuRequerido=" + menuRequerido +
                '}';
    }
}
