package com.example.serviciocliente.repository;

import com.example.serviciocliente.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    Optional<Cliente> findByDni(String dni);
    @Query("SELECT c FROM Cliente c WHERE c.fechaRegistro >= :desde")
    List<Cliente> findClientesRecientes(LocalDateTime desde);
}
