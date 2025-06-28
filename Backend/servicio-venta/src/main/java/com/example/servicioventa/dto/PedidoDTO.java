package com.example.servicioventa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class PedidoDTO {
    private Integer id;
    private ClienteDTO cliente;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime fechaPedido;
    private String estadoPedido;
    private List<DetallePedidoDTO> detalles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(OffsetDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getTotal() {
        if (detalles == null) return BigDecimal.ZERO;

        return detalles.stream()
                .map(DetallePedidoDTO::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public PedidoDTO() {
    }

    public PedidoDTO(Integer id, ClienteDTO cliente, OffsetDateTime fechaPedido, String estadoPedido, List<DetallePedidoDTO> detalles) {
        this.id = id;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.detalles = detalles;
    }
}

