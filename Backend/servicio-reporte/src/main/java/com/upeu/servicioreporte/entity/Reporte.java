package com.upeu.servicioreporte.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "administrador_id")
    private Integer administradorId;

    @Column(name = "venta_id")
    private Integer ventaId;

    @Column(name = "bodega_id")
    private Integer bodegaId;

    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "inventario_cocina_id")
    private Integer inventarioCocinaId;

    @Column(name = "inventario_barra_id")
    private Integer inventarioBarraId;

    @Column(name = "detalle_pedido_id")
    private Integer detallePedidoId;

    @Column(nullable = false)
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String detalles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReporte tipo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Enum interno
    public enum TipoReporte {
        ventas,
        clientes,
        inventario,
        administracion
    }


    // Constructor vacío
    public Reporte() {}

    // Constructor con todos los campos
    public Reporte(Integer id, Integer administradorId, Integer ventaId, Integer bodegaId, Integer clienteId,
                   Integer inventarioCocinaId, Integer inventarioBarraId, Integer detallePedidoId,
                   String descripcion, String detalles, TipoReporte tipo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.administradorId = administradorId;
        this.ventaId = ventaId;
        this.bodegaId = bodegaId;
        this.clienteId = clienteId;
        this.inventarioCocinaId = inventarioCocinaId;
        this.inventarioBarraId = inventarioBarraId;
        this.detallePedidoId = detallePedidoId;
        this.descripcion = descripcion;
        this.detalles = detalles;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(Integer administradorId) {
        this.administradorId = administradorId;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public Integer getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Integer bodegaId) {
        this.bodegaId = bodegaId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getInventarioCocinaId() {
        return inventarioCocinaId;
    }

    public void setInventarioCocinaId(Integer inventarioCocinaId) {
        this.inventarioCocinaId = inventarioCocinaId;
    }

    public Integer getInventarioBarraId() {
        return inventarioBarraId;
    }

    public void setInventarioBarraId(Integer inventarioBarraId) {
        this.inventarioBarraId = inventarioBarraId;
    }

    public Integer getDetallePedidoId() {
        return detallePedidoId;
    }

    public void setDetallePedidoId(Integer detallePedidoId) {
        this.detallePedidoId = detallePedidoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // toString
    @Override
    public String toString() {
        return "Reporte{" +
                "id=" + id +
                ", administradorId=" + administradorId +
                ", ventaId=" + ventaId +
                ", bodegaId=" + bodegaId +
                ", clienteId=" + clienteId +
                ", inventarioCocinaId=" + inventarioCocinaId +
                ", inventarioBarraId=" + inventarioBarraId +
                ", detallePedidoId=" + detallePedidoId +
                ", descripcion='" + descripcion + '\'' +
                ", detalles='" + detalles + '\'' +
                ", tipo=" + tipo +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
