package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Promocion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor_descuento", precision = 10, scale = 2)
    private BigDecimal valorDescuento;

    @Column(name = "motivo", length = 100)
    private String motivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValorDescuento() {
        return valorDescuento;
    }

    public void setValorDescuento(BigDecimal valorDescuento) {
        this.valorDescuento = valorDescuento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Promocion() {
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + id +
                ", valorDescuento=" + valorDescuento +
                ", motivo='" + motivo + '\'' +
                '}';
    }

    public Promocion(Integer id, BigDecimal valorDescuento, String motivo) {
        this.id = id;
        this.valorDescuento = valorDescuento;
        this.motivo = motivo;
    }
}
