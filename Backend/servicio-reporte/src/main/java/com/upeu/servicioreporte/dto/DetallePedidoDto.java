package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;

public class DetallePedidoDto {
    private Long id;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String nombreCliente;
    private String nombreMenu;
    private Integer pedidoId;
    private Integer menuId; // ✅ Campo añadido para identificar el producto

    // Constructor vacío
    public DetallePedidoDto() {
    }

    // Constructor con todos los campos
    public DetallePedidoDto(Long id, Integer cantidad, BigDecimal precioUnitario, String nombreCliente,
                            String nombreMenu, Integer pedidoId, Integer menuId) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.nombreCliente = nombreCliente;
        this.nombreMenu = nombreMenu;
        this.pedidoId = pedidoId;
        this.menuId = menuId;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
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

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    // toString
    @Override
    public String toString() {
        return "DetallePedidoDto{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", nombreMenu='" + nombreMenu + '\'' +
                ", pedidoId=" + pedidoId +
                ", menuId=" + menuId +
                '}';
    }
}
