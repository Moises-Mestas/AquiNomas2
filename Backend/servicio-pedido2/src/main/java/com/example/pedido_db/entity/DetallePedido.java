package com.example.pedido_db.entity;

import com.example.pedido_db.dto.Cliente;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Entity
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private Cliente cliente;  // Relación con Cliente, que se llena mediante Feign
    private Integer clienteId;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public DetallePedido() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public DetallePedido(Integer id, Cliente cliente, Integer clienteId, Menu menu, BigDecimal precioUnitario, int cantidad) {
        this.id = id;
        this.cliente = cliente;
        this.clienteId = clienteId;
        this.menu = menu;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", clienteId=" + clienteId +
                ", menu=" + menu +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}
