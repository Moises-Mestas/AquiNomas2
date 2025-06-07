package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    Optional<Promocion> findById(Integer id);
}
