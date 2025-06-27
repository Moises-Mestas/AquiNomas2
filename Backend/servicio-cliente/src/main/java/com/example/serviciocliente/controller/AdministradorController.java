package com.example.serviciocliente.controller;

import com.example.serviciocliente.entity.Administrador;
import com.example.serviciocliente.service.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")

public class AdministradorController {

    private final AdministradorService administradorService;

    // Constructor explícito en lugar de Lombok
    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }
    @GetMapping
    public List<Administrador> getAllAdministradores() {
        return administradorService.getAllAdministradores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getAdministradorById(@PathVariable Integer id) {
        return administradorService.getAdministradorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Administrador createAdministrador(@RequestBody Administrador administrador) {
        return administradorService.saveAdministrador(administrador);
    }

    @PutMapping
    public Administrador updateAdministrador(@RequestBody Administrador administrador) {
        return administradorService.updateAdministrador(administrador);
    }

    @DeleteMapping("/{id}")
    public void deleteAdministrador(@PathVariable Integer id) {
        administradorService.deleteAdministrador(id);
    }

    @GetMapping("/buscar-nombre")
    public List<Administrador> findByNombre(@RequestParam String nombre) {
        return administradorService.findByNombre(nombre);
    }
}
