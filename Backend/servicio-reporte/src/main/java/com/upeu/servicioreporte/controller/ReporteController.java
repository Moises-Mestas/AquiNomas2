package com.upeu.servicioreporte.controller;

import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReportePorId(@PathVariable Integer id) {
        Optional<Reporte> reporte = reporteService.findById(id);
        return reporte.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reporte crearReporte(@RequestBody Reporte reporte) {
        return reporteService.guardarReporte(reporte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Integer id, @RequestBody Reporte reporteDetalles) {
        Optional<Reporte> optionalReporte = reporteService.findById(id);

        if (!optionalReporte.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reporte reporte = optionalReporte.get();
        // Actualizar campos necesarios
        reporte.setAdministradorId(reporteDetalles.getAdministradorId());
        reporte.setVentaId(reporteDetalles.getVentaId());
        reporte.setBodegaId(reporteDetalles.getBodegaId());
        reporte.setClienteId(reporteDetalles.getClienteId());
        reporte.setInventarioCocinaId(reporteDetalles.getInventarioCocinaId());
        reporte.setInventarioBarraId(reporteDetalles.getInventarioBarraId());
        reporte.setDetallePedidoId(reporteDetalles.getDetallePedidoId());
        reporte.setDescripcion(reporteDetalles.getDescripcion());
        reporte.setDetalles(reporteDetalles.getDetalles());
        reporte.setTipo(reporteDetalles.getTipo());
        // No actualizamos fecha_creacion para mantener histórico

        Reporte reporteActualizado = reporteService.save(reporte);
        return ResponseEntity.ok(reporteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Integer id) {
        Optional<Reporte> optionalReporte = reporteService.findById(id);

        if (!optionalReporte.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reporteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para reportes con lógica de negocio

    @GetMapping("/clientes-mas-frecuentes")
    public List<Map<String, Object>> clientesMasFrecuentes() {
        return reporteService.obtenerClientesMasFrecuentes();
    }

    @GetMapping("/ventas-por-periodo")
    public Map<String, Object> ventasPorPeriodo(@RequestParam("inicio") String inicioStr,
                                                @RequestParam("fin") String finStr) {
        LocalDateTime inicio = LocalDateTime.parse(inicioStr);
        LocalDateTime fin = LocalDateTime.parse(finStr);
        return reporteService.obtenerCantidadVentasPorPeriodo(inicio, fin);
    }

    @GetMapping("/inventarios-mas-usados")
    public List<Map<String, Object>> inventariosMasUsados() {
        return reporteService.obtenerInventariosMasUsados();
    }

    @GetMapping("/platos-bebidas-mas-menos-pedidos")
    public Map<String, List<Map<String, Object>>> platosBebidasMasMenosPedidos() {
        return reporteService.obtenerPlatosBebidasMasMenosPedidos();
    }

    @GetMapping("/costo-cantidad-insumo/{insumoId}")
    public Map<String, Object> costoCantidadPorInsumo(@PathVariable Integer insumoId) {
        return reporteService.obtenerCostoCantidadPorInsumo(insumoId);
    }

    @GetMapping("/comprobantes-mas-usados")
    public Map<String, Long> comprobantesMasUsados() {
        return reporteService.obtenerComprobantesMasUsados();
    }
}
