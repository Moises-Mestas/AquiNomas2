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
        // Devuelve los clientes más frecuentes
        return reporteService.obtenerClientesMasFrecuentes();
    }

    @GetMapping("/ventas-periodo")
    public Map<String, Object> ventasPorPeriodo(@RequestParam("inicio") String inicio,
                                                @RequestParam("fin") String fin) {
        // Convierte los parámetros de fecha (inicio y fin) y pasa a obtener ventas por periodo
        return reporteService.obtenerCantidadVentasPorPeriodo(
                LocalDateTime.parse(inicio), LocalDateTime.parse(fin));
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
