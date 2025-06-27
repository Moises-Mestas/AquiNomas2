package com.example.serviciocliente.service;

import com.example.serviciocliente.entity.Administrador;

import java.util.List;
import java.util.Optional;

public interface AdministradorService {
    Administrador saveAdministrador(Administrador administrador);
    Administrador updateAdministrador(Administrador administrador);
    void deleteAdministrador(Integer id);
    Optional<Administrador> getAdministradorById(Integer id);
    List<Administrador> getAllAdministradores();
    List<Administrador> findByNombre(String nombre);
}
