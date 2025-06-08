package com.example.serviciopedido.entity;

import com.example.serviciopedido.dto.Cliente;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false)
    private Cliente cliente;


    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = true, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public DetallePedido() {

    }

    public DetallePedido(Integer id, Cliente cliente, Menu menu, Integer cantidad, BigDecimal precioUnitario) {

        this.id = id;
        this.cliente = cliente;
        this.menu = menu;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", menu=" + menu +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}

