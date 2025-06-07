package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    Optional<Promocion> findById(Integer id);
    List<Promocion> findByMotivoContainingIgnoreCase(String motivo);
}
