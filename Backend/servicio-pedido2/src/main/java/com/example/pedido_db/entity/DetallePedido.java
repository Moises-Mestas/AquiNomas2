package com.example.pedido_db.entity;

import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.dto.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Entity
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    @JsonBackReference  // Evita referencia circular con Pedido
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false)
    private Menu menu;

    @Column(name = "cantidad")
    private int cantidad;



    public DetallePedido() {

    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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


    public DetallePedido(Integer id, Pedido pedido, Menu menu, int cantidad, BigDecimal precioUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.menu = menu;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", menu=" + menu +
                ", cantidad=" + cantidad +
                '}';
    }


}

