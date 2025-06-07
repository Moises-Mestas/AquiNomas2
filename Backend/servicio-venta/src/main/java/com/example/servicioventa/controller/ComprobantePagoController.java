package com.example.servicioventa.controller;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comprobantes")
public class ComprobantePagoController {
    @Autowired
    private ComprobantePagoService comprobantePagoService;

    @GetMapping
    public ResponseEntity<List<ComprobantePago>> listarComprobantes() {
        return ResponseEntity.ok(comprobantePagoService.listar());
    }

    @PostMapping
    public ResponseEntity<ComprobantePago> guardarComprobante(@RequestBody ComprobantePago comprobantePago) {
        return new ResponseEntity<>(comprobantePagoService.guardarComprobante(comprobantePago), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComprobantePago> actualizarComprobante(@PathVariable Integer id, @RequestBody ComprobantePago comprobantePago) {
        comprobantePago.setId(id);
        return ResponseEntity.ok(comprobantePagoService.actualizar(comprobantePago));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobantePago> obtenerComprobantePorId(@PathVariable Integer id) {
        return comprobantePagoService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComprobante(@PathVariable Integer id) {
        comprobantePagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Búsqueda por número de serie
    @GetMapping("/buscar/serie/{numeroSerie}")
    public ResponseEntity<List<ComprobantePago>> buscarPorNumeroSerie(@PathVariable String numeroSerie) {
        return ResponseEntity.ok(comprobantePagoService.buscarPorNumeroSerie(numeroSerie));
    }
}
