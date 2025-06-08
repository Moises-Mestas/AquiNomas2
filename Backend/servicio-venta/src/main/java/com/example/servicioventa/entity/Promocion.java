package com.example.servicioventa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "promociones")
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_descuento", precision = 5, scale = 2)
    private BigDecimal valorDescuento;

    @Column(name = "motivo", length = 100)
    private String motivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}