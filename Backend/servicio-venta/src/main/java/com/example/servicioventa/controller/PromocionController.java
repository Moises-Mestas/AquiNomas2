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

    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public List<Promocion> listar() {
        return promocionService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promocion> listarPorId(@PathVariable Integer id) {
        Optional<Promocion> promocion = promocionService.listarPorId(id);
        return promocion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Promocion guardar(@RequestBody Promocion promocion) {
        return promocionService.guardar(promocion);
    }

    @PutMapping
    public Promocion actualizar(@RequestBody Promocion promocion) {
        return promocionService.actualizar(promocion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        promocionService.eliminarPorId(id);
    }
}