package com.example.serviciopedido.controller;

import com.example.serviciopedido.entity.Receta;
import com.example.serviciopedido.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    @Autowired
    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    public List<Receta> listar() {
        return recetaService.listar();
    }

    @GetMapping("/{id}")
    public Receta buscarPorId(@PathVariable Integer id) {
        return recetaService.listarPorId(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
    }

    @PostMapping
    public Receta crear(@RequestBody Receta receta) {
        return recetaService.guardar(receta);
    }

    @PutMapping("/{id}")
    public Receta actualizar(@PathVariable Integer id, @RequestBody Receta receta) {
        receta.setId(id);
        return recetaService.actualizar(receta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        recetaService.eliminar(id);
    }
}
