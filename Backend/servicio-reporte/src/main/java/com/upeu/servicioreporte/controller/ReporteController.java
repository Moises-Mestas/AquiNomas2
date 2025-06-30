package com.upeu.servicioreporte.controller;

import com.upeu.servicioreporte.dto.ReporteGeneralDto;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.ClienteAdministradorClient;
import com.upeu.servicioreporte.service.ReporteService;
import com.upeu.servicioreporte.util.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;
    private final ClienteAdministradorClient clienteAdministradorClient;

    public ReporteController(ReporteService reporteService, @Qualifier("com.upeu.servicioreporte.feign.ClienteAdministradorClient") ClienteAdministradorClient clienteAdministradorClient) {
        this.reporteService = reporteService;
        this.clienteAdministradorClient = clienteAdministradorClient;
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

    @GetMapping("/pdf/comprobantes-mas-usados")
    public ResponseEntity<Resource> exportarComprobantesMasUsadosPDF() {
        try {
            Map<String, Long> datosOriginales = reporteService.obtenerComprobantesMasUsados();

            // Transformar a List<Map<String, Object>> usando HashMap para evitar errores de tipos
            List<Map<String, Object>> datosFormateados = datosOriginales.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> map = new java.util.HashMap<>();
                        map.put("comprobante", entry.getKey());
                        map.put("cantidad", entry.getValue());
                        return map;
                    })
                    .toList();

            File pdf = ComprobantesMasUsadosPdf.exportar(datosFormateados);

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

    @GetMapping("/comprobantes-mas-usados")
    public Map<String, Long> comprobantesMasUsados() {
        // Obtiene los comprobantes más usados
        return reporteService.obtenerComprobantesMasUsados();
    }

    @PostMapping
    public Reporte guardarReporte(@RequestBody Reporte reporte) {
        // Guarda el reporte recibido
        return reporteService.guardarReporte(reporte);
    }

    @GetMapping("/pdf")
    public ResponseEntity<Resource> exportarReporteGeneralPDF() {
        try {
            List<Reporte> reportes = reporteService.listarReportes();

            List<Map<String, Object>> datosFormateados = reportes.stream()
                    .map(reporte -> {
                        Map<String, Object> map = new java.util.HashMap<>();
                        map.put("id", reporte.getId());
                        map.put("descripcion", reporte.getDescripcion());
                        map.put("detalles", reporte.getDetalles());
                        map.put("tipo", reporte.getTipo().name());
                        map.put("fechaCreacion", reporte.getFechaCreacion().toString());

                        // Obtener nombres reales desde Feign Client
                        String clienteNombre = "No Asignado";
                        String adminNombre = "No Asignado";
                        try {
                            if (reporte.getClienteId() != null) {
                                clienteNombre = clienteAdministradorClient
                                        .obtenerClientePorId(reporte.getClienteId())
                                        .getNombre();
                            }
                        } catch (Exception ignored) {}

                        try {
                            if (reporte.getAdministradorId() != null) {
                                adminNombre = clienteAdministradorClient
                                        .obtenerAdministradorPorId(reporte.getAdministradorId())
                                        .getNombre();
                            }
                        } catch (Exception ignored) {}

                        map.put("cliente", clienteNombre);
                        map.put("admin", adminNombre);
                        return map;
                    }).toList();

            File pdf = ListaReportesPdf.exportar(datosFormateados); // Actualiza también esta clase

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



    @GetMapping
    public List<Reporte> listarReportes() {
        // Listar todos los reportes
        return reporteService.listarReportes();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporte) {
        return ResponseEntity.ok(reporteService.actualizarReporte(id, reporte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

}
