package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Venta>> listar() {
        List<Venta> ventas = ventaService.listar();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> listarPorId(@PathVariable Long id) {
        return ventaService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> guardar(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.guardarVenta(venta);
        return ResponseEntity.status(201).body(nuevaVenta);
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
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @RequestBody Venta venta) {
        if (!ventaService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Venta ventaActualizada = ventaService.actualizar(venta);
        return ResponseEntity.ok(ventaActualizada);
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