package com.upeu.servicioreporte.service;

import com.upeu.servicioreporte.dto.ReporteGeneralDto;
import com.upeu.servicioreporte.entity.Reporte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReporteService {

    List<Reporte> findAll();

    Optional<Reporte> findById(Integer id);

    Reporte save(Reporte reporte);

    List<Map<String, Object>> obtenerProductosMasRentables();

    Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);

    Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos();

    Map<String, Long> obtenerComprobantesMasUsados();

    Reporte guardarReporte(Reporte reporte);

    List<Reporte> listarReportes();

    Reporte actualizarReporte(Long id, Reporte reporteActualizado);

    void eliminarReporte(Long id);
}
