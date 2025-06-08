package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findById(Integer id);
}
