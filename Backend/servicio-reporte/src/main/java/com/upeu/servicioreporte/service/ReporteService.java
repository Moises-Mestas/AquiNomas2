package com.upeu.servicioreporte.service;

import com.upeu.servicioreporte.entity.Reporte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReporteService {

    List<Reporte> findAll();

    Optional<Reporte> findById(Integer id);

    Reporte save(Reporte reporte);

    void deleteById(Integer id);

    List<Map<String, Object>> obtenerClientesMasFrecuentes();

    Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin);

    List<Map<String, Object>> obtenerInventariosMasUsados();

    Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos();

    Map<String, Object> obtenerCostoCantidadPorInsumo(Integer insumoId);

    Map<String, Long> obtenerComprobantesMasUsados();

    Reporte guardarReporte(Reporte reporte);

    List<Reporte> listarReportes();
}
