package com.example.servicioventa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class MenuPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "promocion_id")
    @JsonBackReference
    private Promocion promocion;

    @Column(name = "menu_id")
    private Integer menuId; // ID del platillo o bebida del menú

    @Column(name = "cantidad_requerida")
    private Integer cantidadRequerida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Promocion getPromocion() {
        return promocion;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(Integer cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public MenuPromocion() {
    }

    @Override
    public String toString() {
        return "MenuPromocion{" +
                "id=" + id +
                ", promocion=" + promocion +
                ", menuId=" + menuId +
                ", cantidadRequerida=" + cantidadRequerida +
                '}';
    }
}
