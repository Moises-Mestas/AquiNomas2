package com.example.servicioventa.dto;

import com.example.servicioventa.entity.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaDTO {
    private Long id;
    private String metodoPago; // ✅ Ahora recibe un String
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private String nombreCliente;
    private String nombreMenu;
    private int cantidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public VentaDTO(Long id, String metodoPago, LocalDateTime fechaVenta, BigDecimal total, String nombreCliente, String nombreMenu, int cantidad) {
        this.id = id;
        this.metodoPago = metodoPago;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.nombreMenu = nombreMenu;
        this.cantidad = cantidad;
    }
}
