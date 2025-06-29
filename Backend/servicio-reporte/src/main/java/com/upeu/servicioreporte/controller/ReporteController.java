package com.upeu.servicioreporte.controller;

import com.upeu.servicioreporte.dto.VentaDto;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.VentaClient;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/productos-mas-rentables")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosMasRentables() {
        return ResponseEntity.ok(reporteService.obtenerProductosMasRentables());
    }

    @GetMapping("/ventas-por-periodo")
    public ResponseEntity<Map<String, Object>> obtenerCantidadVentasPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(reporteService.obtenerCantidadVentasPorPeriodo(inicio, fin));
    }




    @GetMapping("/inventarios-mas-usados")
    public List<Map<String, Object>> inventariosMasUsados() {
        // Devuelve los inventarios más usados
        return reporteService.obtenerInventariosMasUsados();
    }

    @GetMapping("/platos-bebidas")
    public Map<String, List<Map<String, Object>>> platosBebidasMasMenosPedidos() {
        // Obtiene los platos y bebidas más y menos pedidos
        return reporteService.obtenerPlatosBebidasMasMenosPedidos();
    }

    @GetMapping("/insumo-costo/{insumoId}")
    public Map<String, Object> costoPorInsumo(@PathVariable Integer insumoId) {
        // Obtiene el costo por insumo según el ID
        return reporteService.obtenerCostoCantidadPorInsumo(insumoId);
    }

    @GetMapping("/comprobantes")
    public Map<String, Long> comprobantesMasUsados() {
        // Obtiene los comprobantes más usados
        return reporteService.obtenerComprobantesMasUsados();
    }

    @PostMapping
    public Reporte guardarReporte(@RequestBody Reporte reporte) {
        // Guarda el reporte recibido
        return reporteService.guardarReporte(reporte);
    }

    @GetMapping
    public List<Reporte> listarReportes() {
        // Listar todos los reportes
        return reporteService.listarReportes();
    }

}
