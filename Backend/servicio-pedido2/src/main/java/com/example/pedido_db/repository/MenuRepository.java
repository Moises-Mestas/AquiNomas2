package com.example.pedido_db.repository;

import com.example.pedido_db.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Método para encontrar menús cuyo precio esté entre minPrecio y maxPrecio
    List<Menu> findByPrecioBetween(BigDecimal minPrecio, BigDecimal maxPrecio);
}
