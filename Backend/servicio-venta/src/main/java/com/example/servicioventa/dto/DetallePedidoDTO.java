package com.example.servicioventa.dto;

import java.math.BigDecimal;

public class DetallePedidoDTO {
    private Integer id;
    private Integer menuId;
    private MenuDTO menu;
    private String nombreMenu;
    private String descripcionMenu;
    private BigDecimal precioUnitario;
    private Integer cantidad;

    // 🧮 Calculado automáticamente
    public BigDecimal getSubtotal() {
        if (precioUnitario == null || cantidad == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // ✅ Enriquecer detalles visibles desde el objeto menu
    public void enriquecerDesdeMenu() {
        if (this.menu != null) {
            this.menuId = menu.getId(); // Refresca en caso de inconsistencia
            this.nombreMenu = menu.getNombre();
            this.descripcionMenu = menu.getDescripcion();
            this.precioUnitario = menu.getPrecio();
        }
    }

    // Getters y Setters
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

    // Constructor vacío
    public DetallePedidoDTO() {
    }

    @Override
    public String toString() {
        return "DetallePedidoDTO{" +
                "id=" + id +
                ", menuId=" + getMenuId() +
                ", nombreMenu='" + nombreMenu + '\'' +
                ", descripcionMenu='" + descripcionMenu + '\'' +
                ", precioUnitario=" + precioUnitario +
                ", cantidad=" + cantidad +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}