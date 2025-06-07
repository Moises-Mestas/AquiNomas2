package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
public class PromocionController {
    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<Promocion>> listarPromociones() {
        return ResponseEntity.ok(promocionService.listar());
    }

    @PostMapping
    public ResponseEntity<Promocion> guardarPromocion(@RequestBody Promocion promocion) {
        return new ResponseEntity<>(promocionService.guardarPromocion(promocion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> actualizarPromocion(@PathVariable Integer id, @RequestBody Promocion promocion) {
        promocion.setId(id);
        return ResponseEntity.ok(promocionService.actualizar(promocion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocion> obtenerPromocionPorId(@PathVariable Integer id) {
        return promocionService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Integer id) {
        promocionService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Búsqueda por motivo
    @GetMapping("/buscar/motivo/{motivo}")
    public ResponseEntity<List<Promocion>> buscarPorMotivo(@PathVariable String motivo) {
        return ResponseEntity.ok(promocionService.buscarPorMotivo(motivo));
    }
}
