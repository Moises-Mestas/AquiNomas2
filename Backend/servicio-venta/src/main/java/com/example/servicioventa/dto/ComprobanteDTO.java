package com.example.servicioventa.dto;

import com.example.servicioventa.entity.ComprobantePago;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComprobanteDTO {
    private Integer id;
    private ComprobantePago.TipoComprobante tipo;
    private String numeroSerie;
    private String numeroComprobante;
    private LocalDateTime fechaEmision;
    private BigDecimal montoNeto;
    private BigDecimal igv;
    private String razonSocial;
    private String direccionFiscal;

    private VentaDTO venta;

    public ComprobanteDTO() {}

    public Integer getId() { return id; }
    public ComprobantePago.TipoComprobante getTipo() { return tipo; }
    public String getNumeroSerie() { return numeroSerie; }
    public String getNumeroComprobante() { return numeroComprobante; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public BigDecimal getMontoNeto() { return montoNeto; }
    public BigDecimal getIgv() { return igv; }
    public String getRazonSocial() { return razonSocial; }
    public String getDireccionFiscal() { return direccionFiscal; }
    public VentaDTO getVenta() {
        return venta;
    }

    public void setId(Integer id) { this.id = id; }
    public void setTipo(ComprobantePago.TipoComprobante tipo) { this.tipo = tipo; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public void setMontoNeto(BigDecimal montoNeto) { this.montoNeto = montoNeto; }
    public void setIgv(BigDecimal igv) { this.igv = igv; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }
    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }
}