package com.example.servicioventa.controller;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    public ResponseEntity<List<Promocion>> listar() {
        return ResponseEntity.ok(promocionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocion> listarPorId(@PathVariable Long id) {
        return promocionService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Promocion> guardar(@RequestBody Promocion promocion) {
        Promocion nuevaPromocion = promocionService.guardar(promocion);
        return ResponseEntity.status(201).body(nuevaPromocion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> actualizar(@PathVariable Long id, @RequestBody Promocion promocion) {
        if (promocion.getId() == null) {
            promocion.setId(id); // Asigna el ID recibido en la URL
        }

        if (!promocionService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Promocion promocionActualizada = promocionService.actualizar(promocion);
        return ResponseEntity.ok(promocionActualizada);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Promocion>> buscarPorMotivo(@RequestParam String motivo) {
        List<Promocion> promociones = promocionService.buscarPorMotivo(motivo);
        return promociones.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(promociones);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!promocionService.listarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        promocionService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
}