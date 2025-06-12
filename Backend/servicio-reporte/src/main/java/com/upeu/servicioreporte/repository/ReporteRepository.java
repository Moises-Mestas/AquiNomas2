package com.upeu.servicioreporte.repository;

import com.upeu.servicioreporte.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
