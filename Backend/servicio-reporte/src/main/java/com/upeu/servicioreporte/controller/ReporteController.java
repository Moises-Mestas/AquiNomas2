package com.upeu.servicioreporte.controller;

import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.service.ReporteService;
import com.upeu.servicioreporte.util.PdfExportUtil;
import com.upeu.servicioreporte.util.PlatosBebidasMasMenosPedidosPdf;
import com.upeu.servicioreporte.util.VentasPorPeriodoPdfGenerator;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
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

    @GetMapping("/productos-mas-rentables/pdf")
    public ResponseEntity<byte[]> exportarProductosMasRentablesPdf() {
        List<Map<String, Object>> productos = reporteService.obtenerProductosMasRentables();
        ByteArrayInputStream pdfStream = PdfExportUtil.exportarProductosMasRentables(productos);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=productos_mas_rentables.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfStream.readAllBytes());
    }

    @GetMapping("/productos-mas-rentables")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductosMasRentables() {
        return ResponseEntity.ok(reporteService.obtenerProductosMasRentables());
    }

    @GetMapping("/pdf/ventas-por-periodo")
    public void exportarVentasPorPeriodoPdf(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            HttpServletResponse response
    ) {
        try {
            Map<String, Object> datos = reporteService.obtenerCantidadVentasPorPeriodo(inicio, fin);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=ventas-por-periodo.pdf");

            VentasPorPeriodoPdfGenerator.generarPdf(datos, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ventas-por-periodo")
    public ResponseEntity<Map<String, Object>> obtenerCantidadVentasPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(reporteService.obtenerCantidadVentasPorPeriodo(inicio, fin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerReportePorId(@PathVariable Integer id) {
        return reporteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pdf/platos-bebidas")
    public ResponseEntity<Resource> exportarPlatosBebidasMasMenosPedidosPDF() {
        try {
            Map<String, List<Map<String, Object>>> datos = reporteService.obtenerPlatosBebidasMasMenosPedidos();
            File pdf = PlatosBebidasMasMenosPedidosPdf.exportar(
                    datos.get("masPedidos"), datos.get("menosPedidos")
            );

            InputStreamResource resource = new InputStreamResource(new java.io.FileInputStream(pdf));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdf.length())
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
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
