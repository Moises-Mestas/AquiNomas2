package com.upeu.serviciocliente.repository;

import com.upeu.serviciocliente.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    List<Administrador> findByNombreContainingIgnoreCase(String nombre);
}