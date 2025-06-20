package com.example.servicioventa.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DetallePedidoDTO {
    private Integer id;
    private Integer menuId;
    private String nombreMenu;
    private String descripcionMenu;
    private BigDecimal precioUnitario;
    private Integer cantidad;

//    public BigDecimal getSubtotal() {
//        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
//    }


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

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }

    public String getDescripcionMenu() {
        return descripcionMenu;
    }

    public void setDescripcionMenu(String descripcionMenu) {
        this.descripcionMenu = descripcionMenu;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        if (precioUnitario == null || cantidad == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }


    public DetallePedidoDTO() {
    }

    @Override
    public String toString() {
        return "DetallePedidoDTO{" +
                "id=" + id +
                ", menuId=" + menuId +
                ", nombreMenu='" + nombreMenu + '\'' +
                ", descripcionMenu='" + descripcionMenu + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", cantidad=" + cantidad +
                '}';
    }
}
