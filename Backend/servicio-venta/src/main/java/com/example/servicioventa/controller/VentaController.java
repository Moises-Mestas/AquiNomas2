package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @PostMapping
    public ResponseEntity<Venta> guardarVenta(@RequestBody Venta venta) {
        return new ResponseEntity<>(ventaService.guardarVenta(venta), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizarVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        venta.setId(id);
        return ResponseEntity.ok(ventaService.actualizar(venta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Integer id) {
        return ventaService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Integer id) {
        ventaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Búsqueda por método de pago
    @GetMapping("/buscar/metodo-pago/{metodoPago}")
    public ResponseEntity<List<Venta>> buscarPorMetodoPago(@PathVariable String metodoPago) {
        return ResponseEntity.ok(ventaService.buscarPorMetodoPago(metodoPago));
    }

    // Búsqueda por clienteId
    @GetMapping("/buscar/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> buscarPorClienteId(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(ventaService.buscarPorClienteId(clienteId));
    }
}
