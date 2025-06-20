package com.example.servicioventa.repository;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByCliente(Integer cliente);
    List<Venta> findByFechaVentaBetween(LocalDateTime desde, LocalDateTime hasta);
    List<Venta> findByMetodoPago(Venta.MetodoPago metodoPago);
    Optional<Venta> findById(Integer id);
    List<Venta> findByPedidoIdIn(Collection<Integer> pedidoIds);
    @Query("SELECT p FROM Promocion p LEFT JOIN FETCH p.menu")
    List<Promocion> findAllConMenu();
    void deleteById(Integer id);
    boolean existsByPedidoId(Integer pedidoId);
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.cliente = :cliente")
    Optional<BigDecimal> totalVentasPorCliente(@Param("cliente") Integer cliente);
    List<Venta> findByPromocionId(Integer promocionId);
}
