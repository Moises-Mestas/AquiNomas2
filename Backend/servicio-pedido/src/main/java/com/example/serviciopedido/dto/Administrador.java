package com.example.serviciopedido.dto;

import com.example.serviciopedido.entity.Pedido;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremento para el campo id
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToMany(mappedBy = "administrador")
    private List<Pedido> pedidos;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Timestamp fechaCreacion;

    // Constructor vacío
    public Administrador() {
    }

    // Constructor con todos los campos
    public Administrador(Integer id, String nombre, String email, Timestamp fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // toString
    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
