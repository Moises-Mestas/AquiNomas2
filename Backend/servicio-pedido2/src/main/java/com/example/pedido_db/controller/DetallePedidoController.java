package com.example.pedido_db.controller;


import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    // Listar todos los detalles de pedido
    @GetMapping
    public ResponseEntity<List<DetallePedido>> list() {
        List<DetallePedido> detalles = detallePedidoService.listar();
        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(detalles);
    }

    // Guardar un nuevo detalle de pedido
    @PostMapping
    public ResponseEntity<?> save(@RequestBody DetallePedido detallePedido) {
        try {
            DetallePedido detalleGuardado = detallePedidoService.guardar(detallePedido);
            return ResponseEntity.ok(detalleGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar un detalle de pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> update(@PathVariable Integer id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);  // asegura que el detalle tenga el ID correcto
        DetallePedido detalleActualizado = detallePedidoService.actualizar(detallePedido);
        return ResponseEntity.ok(detalleActualizado);
    }

    // Obtener detalle de pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> listById(@PathVariable Integer id) {
        Optional<DetallePedido> detallePedido = detallePedidoService.listarPorId(id);
        if (detallePedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detallePedido.get());
    }

    // Eliminar un detalle de pedido por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        detallePedidoService.eliminar(id);
    }
}
