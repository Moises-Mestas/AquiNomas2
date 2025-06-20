package com.example.servicioventa.dto;

import com.example.servicioventa.entity.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaDTO {
    private Integer id;
    private BigDecimal total;
    private String metodoPago;
    private LocalDateTime fechaVenta;
    private String nombreCliente;
    private PedidoDTO pedido;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    public VentaDTO() {
    }

    public VentaDTO(Venta venta, PedidoDTO pedido) {
        this.id = venta.getId();
        this.total = venta.getTotal();
        this.metodoPago = venta.getMetodoPago().name();
        this.fechaVenta = venta.getFechaVenta();
        this.nombreCliente = pedido.getNombreCliente();
        this.pedido = pedido;
    }


    @Override
    public String toString() {
        return "VentaDTO{" +
                "id=" + id +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                ", fechaVenta=" + fechaVenta +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", pedido=" + pedido +
                '}';
    }
}