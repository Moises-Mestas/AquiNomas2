package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findById(Integer id);
    List<Venta> findByPedidoIdIn(List<Long> pedidoIds);
    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);

}
