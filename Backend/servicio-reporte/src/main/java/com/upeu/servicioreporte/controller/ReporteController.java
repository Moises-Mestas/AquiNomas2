package com.upeu.servicioreporte.controller;

import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/clientes-frecuentes")
    public List<Map<String, Object>> clientesMasFrecuentes() {
        return reporteService.obtenerClientesMasFrecuentes();
    }

    @GetMapping("/ventas-periodo")
    public Map<String, Object> ventasPorPeriodo(@RequestParam("inicio") String inicio,
                                                @RequestParam("fin") String fin) {
        return reporteService.obtenerCantidadVentasPorPeriodo(
                LocalDateTime.parse(inicio), LocalDateTime.parse(fin));
    }

    @GetMapping("/inventarios-mas-usados")
    public List<Map<String, Object>> inventariosMasUsados() {
        return reporteService.obtenerInventariosMasUsados();
    }

    @GetMapping("/platos-bebidas")
    public Map<String, List<Map<String, Object>>> platosBebidasMasMenosPedidos() {
        return reporteService.obtenerPlatosBebidasMasMenosPedidos();
    }

    @GetMapping("/insumo-costo/{insumoId}")
    public Map<String, Object> costoPorInsumo(@PathVariable Integer insumoId) {
        return reporteService.obtenerCostoCantidadPorInsumo(insumoId);
    }

    @GetMapping("/comprobantes")
    public Map<String, Long> comprobantesMasUsados() {
        return reporteService.obtenerComprobantesMasUsados();
    }

    @PostMapping
    public Reporte guardarReporte(@RequestBody Reporte reporte) {
        return reporteService.guardarReporte(reporte);
    }

    @GetMapping
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }
}