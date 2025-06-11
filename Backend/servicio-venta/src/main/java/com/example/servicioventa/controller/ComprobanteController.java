package com.example.servicioventa.controller;


import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final ComprobantePagoService comprobanteService;

    public ComprobanteController(ComprobantePagoService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    // ✅ Generar comprobante a partir de una venta
    @PostMapping("/{ventaId}/generar")
    public ResponseEntity<?> generarComprobante(@PathVariable Long ventaId, @RequestParam ComprobantePago.TipoComprobante tipo) {
        try {
            ComprobantePago comprobante = comprobanteService.guardarComprobante(ventaId, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Error: " + e.getMessage());
        }
    }

    // ✅ Descargar comprobante en PDF
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable Long id) {
        try {
            byte[] pdfBytes = comprobanteService.generarComprobantePDF(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ✅ Listar todos los comprobantes
    @GetMapping
    public ResponseEntity<List<ComprobantePago>> listarComprobantes() {
        return ResponseEntity.ok(comprobanteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerComprobante(@PathVariable Long id) {
        Optional<ComprobantePago> comprobante = comprobanteService.obtenerPorId(id);

        if (comprobante.isPresent()) {
            return ResponseEntity.ok(comprobante.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Comprobante no encontrado.");
        }
    }


    // ✅ Filtrar comprobantes por tipo y fecha
    @GetMapping("/filtrar")
    public ResponseEntity<List<ComprobantePago>> filtrarComprobantes(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<ComprobantePago> comprobantes = comprobanteService.filtrarComprobantes(tipo, fechaInicio, fechaFin);
        return ResponseEntity.ok(comprobantes);
    }

    // ✅ Eliminar comprobante por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarComprobante(@PathVariable Long id) {
        comprobanteService.eliminarPorId(id);
        return ResponseEntity.ok("✅ Comprobante eliminado correctamente.");
    }
}