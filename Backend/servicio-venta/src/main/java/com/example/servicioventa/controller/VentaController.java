package com.example.servicioventa.controller;

import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> listar() { // ✅ Ahora devuelve VentaDTO
        List<VentaDTO> ventas = ventaService.listar();
        return ResponseEntity.ok(ventas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Venta> listarPorId(@PathVariable Long id) {
        return ventaService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardarVenta(@RequestBody Venta venta) {
        try {
            return ventaService.guardarVenta(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscar/cliente")
    public ResponseEntity<List<Venta>> buscarPorNombreCliente(@RequestParam String nombreCliente) {
        List<Venta> ventas = ventaService.buscarPorNombreCliente(nombreCliente);
        return ventas.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ventas);
    }


    @GetMapping("/buscar/fecha")
    public ResponseEntity<List<Venta>> buscarPorFecha(@RequestParam LocalDateTime inicio, @RequestParam LocalDateTime fin) {
        List<Venta> ventas = ventaService.buscarPorFecha(inicio, fin);
        return ventas.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ventas);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.actualizar(id, venta);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!ventaService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ventaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}