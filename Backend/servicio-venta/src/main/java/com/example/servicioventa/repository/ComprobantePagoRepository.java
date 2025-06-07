package com.example.servicioventa.repository;

import com.example.servicioventa.entity.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Integer> {
    Optional<ComprobantePago> findById(Integer id);
    List<ComprobantePago> findByNumeroSerieContainingIgnoreCase(String numeroSerie);
}
