package com.example.servicioventa.dto;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private Integer id;
    private Integer menuId;
    private MenuDTO menu;
    private BigDecimal precioUnitario;
    private Integer cantidad;

    public BigDecimal getSubtotal() {
        if (precioUnitario == null || cantidad == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public void enriquecerDesdeMenu() {
        if (this.menu != null) {
            this.menuId = menu.getId(); // Refresca en caso de inconsistencia
            this.precioUnitario = menu.getPrecio();
        }
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuId() {
        if (menuId != null) return menuId;
        return (menu != null) ? menu.getId() : null;
    }
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public MenuDTO getMenu() {
        return menu;
    }
    public void setMenu(MenuDTO menu) {
        this.menu = menu;
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

    public DetallePedidoDTO() {
    }

}