package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> listarPorId(@PathVariable Integer id) {
        Optional<Venta> venta = ventaService.listarPorId(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venta guardar(@RequestBody Venta venta) {
        return ventaService.guardarVenta(venta);
    }

    @PutMapping
    public Venta actualizar(@RequestBody Venta venta) {
        return ventaService.actualizar(venta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        ventaService.eliminarPorId(id);
    }
}