package com.example.servicioventa.repository;

import com.example.servicioventa.entity.ComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    Optional<ComprobantePago> findById(Integer id);
}
