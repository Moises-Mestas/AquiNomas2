package com.example.servicioventa.controller;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.ComprobanteDTO;
import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.mapper.ComprobanteMapper;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final ComprobanteMapper comprobanteMapper;
    private final PedidoClient pedidoClient;

    public ComprobanteController(ComprobantePagoService comprobanteService, ComprobanteMapper comprobanteMapper, @Qualifier("com.example.servicioventa.feing.PedidoClient") PedidoClient pedidoClient) {
        this.comprobanteService = comprobanteService;
        this.comprobanteMapper = comprobanteMapper;
        this.pedidoClient = pedidoClient;
    }

    @PostMapping("/venta/{ventaId}")
    public ResponseEntity<?> generarComprobante(
            @PathVariable Integer ventaId,
            @RequestParam ComprobantePago.TipoComprobante tipo
    ) {
        try {
            ComprobantePago comprobante = comprobanteService.guardarComprobante(ventaId, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("⚠️ " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarPDF(@PathVariable Long id) {
        try {
            byte[] pdfBytes = comprobanteService.generarComprobantePDF(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ComprobanteDTO>> listarComprobantes() {
        List<ComprobantePago> comprobantes = comprobanteService.listar();

        List<ComprobanteDTO> dtos = comprobantes.stream()
                .map(comprobante -> {
                    Venta venta = comprobante.getVenta();

                    String nombreCliente = "Cliente desconocido";
                    PedidoDTO pedidoDTO = null;

                    if (venta != null) {
                        // 🔎 Obtener ClienteDTO por Feign
                        try {
                            ClienteDTO clienteDTO = pedidoClient.obtenerClientePorId(venta.getCliente());
                            if (clienteDTO != null) {
                                nombreCliente = clienteDTO.getNombre() + " " + clienteDTO.getApellido();
                            }
                        } catch (Exception e) {
                            // Loguear si querés: cliente no disponible
                        }

                        // 📦 Obtener PedidoDTO por Feign
                        try {
                            pedidoDTO = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
                        } catch (Exception e) {
                            // Loguear si querés: pedido no disponible
                        }
                    }

                    return comprobanteMapper.aDTO(comprobante, nombreCliente, pedidoDTO);
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerComprobante(@PathVariable Long id) {
        Optional<ComprobantePago> comprobanteOpt = comprobanteService.obtenerPorId(id);

        if (comprobanteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Comprobante no encontrado.");
        }

        ComprobantePago comprobante = comprobanteOpt.get();
        Venta venta = comprobante.getVenta();

        String nombreCliente = "Cliente desconocido";
        PedidoDTO pedidoDTO = null;

        if (venta != null) {
            // Obtener cliente vía Feign
            try {
                ClienteDTO cliente = pedidoClient.obtenerClientePorId(venta.getCliente());
                if (cliente != null) {
                    nombreCliente = cliente.getNombre() + " " + cliente.getApellido();
                }
            } catch (Exception e) {
                // Podés loguear si querés
            }

            // Obtener pedido vía Feign
            try {
                pedidoDTO = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
            } catch (Exception e) {
                // Podés loguear si querés
            }
        }

        ComprobanteDTO dto = comprobanteMapper.aDTO(comprobante, nombreCliente, pedidoDTO);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<ComprobantePago>> filtrar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        List<ComprobantePago> filtrados = comprobanteService.filtrarComprobantes(tipo, fechaInicio, fechaFin);
        return ResponseEntity.ok(filtrados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        comprobanteService.eliminarPorId(id);
        return ResponseEntity.ok("✅ Comprobante eliminado correctamente.");
    }
}
