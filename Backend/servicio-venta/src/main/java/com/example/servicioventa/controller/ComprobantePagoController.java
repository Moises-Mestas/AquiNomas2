package com.example.servicioventa.controller;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comprobantes")
public class ComprobantePagoController {

    private final ComprobantePagoService comprobantePagoService;

    public ComprobantePagoController(ComprobantePagoService comprobantePagoService) {
        this.comprobantePagoService = comprobantePagoService;
    }

    @GetMapping
    public ResponseEntity<List<ComprobantePago>> listar() {
        return ResponseEntity.ok(comprobantePagoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprobantePago> listarPorId(@PathVariable Long id) {
        return comprobantePagoService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComprobantePago> guardar(@RequestBody ComprobantePago comprobantePago) {
        ComprobantePago nuevoComprobante = comprobantePagoService.guardar(comprobantePago);
        return ResponseEntity.status(201).body(nuevoComprobante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComprobantePago> actualizar(@PathVariable Long id, @RequestBody ComprobantePago comprobantePago) {
        if (!comprobantePagoService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ComprobantePago comprobanteActualizado = comprobantePagoService.actualizar(comprobantePago);
        return ResponseEntity.ok(comprobanteActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!comprobantePagoService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        comprobantePagoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}