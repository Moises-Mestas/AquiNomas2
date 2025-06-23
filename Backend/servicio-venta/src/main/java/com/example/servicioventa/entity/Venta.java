package com.example.servicioventa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pedido_id", nullable = false)
    private Integer pedidoId;

    @ManyToMany
    @JoinTable(
            name = "venta_promocion",
            joinColumns = @JoinColumn(name = "venta_id"),
            inverseJoinColumns = @JoinColumn(name = "promocion_id")
    )
    private List<Promocion> promociones;

    private Integer cliente;

    @Column(name = "fecha_venta")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime fechaVenta;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public List<Promocion> getPromociones() {
        return promociones;
    }

    public void setPromociones(List<Promocion> promociones) {
        this.promociones = promociones;
    }

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(OffsetDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", promociones=" + promociones +
                ", cliente=" + cliente +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", metodoPago=" + metodoPago +
                '}';
    }

    public Venta() {
    }

    @Transient
    private Integer maximoPromocionesPermitidas;

    public Integer getMaximoPromocionesPermitidas() {
        return maximoPromocionesPermitidas;
    }

    public void setMaximoPromocionesPermitidas(Integer maximoPromocionesPermitidas) {
        this.maximoPromocionesPermitidas = maximoPromocionesPermitidas;
    }

}