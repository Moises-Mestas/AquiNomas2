package com.example.serviciocliente.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private String ruc;
<<<<<<< HEAD


=======
>>>>>>> nelson
    // Constructor vacío
    public Cliente() {
    }

<<<<<<< HEAD
    // Constructor con todos los campos
    public Cliente(Integer id, String nombre, String apellido, String dni, String telefono,
                   String email, String direccion, LocalDateTime fechaRegistro, String ruc) {
=======
    public Cliente(Integer id, String nombre, String apellido, String dni, String telefono, String email, String direccion, LocalDateTime fechaRegistro, String ruc) {
>>>>>>> nelson
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.ruc = ruc;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
<<<<<<< HEAD
    // toString
=======

>>>>>>> nelson
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
<<<<<<< HEAD
                ", fechaRegistro=" + fechaRegistro + '\'' +
                ", ruc='" + ruc +
=======
                ", fechaRegistro=" + fechaRegistro +
                ", ruc='" + ruc + '\'' +
>>>>>>> nelson
                '}';
    }
}