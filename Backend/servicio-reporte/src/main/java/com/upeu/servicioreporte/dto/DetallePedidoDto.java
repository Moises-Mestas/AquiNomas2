package com.upeu.servicioreporte.dto;

import java.math.BigDecimal;

public class DetallePedidoDto {
    private Long id;
    private Integer cantidad;
    private Integer pedidoId;
    private MenuDto menu;
    public Integer getMenuId() {
        return (menu != null) ? menu.getId() : null;
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

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public MenuDto getMenu() {
        return menu;
    }

    public void setMenu(MenuDto menu) {
        this.menu = menu;
    }
}
