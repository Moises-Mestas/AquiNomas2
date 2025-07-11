package com.example.servicioventa.controller;

import com.example.servicioventa.dto.MenuRequeridoDTO;
import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @PostMapping
    public ResponseEntity<PromocionDTO> crearPromocion(@RequestBody PromocionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promocionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> actualizarPromocion(@PathVariable Long id, @RequestBody PromocionDTO dto) {
        return ResponseEntity.ok(promocionService.actualizar(dto, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> obtenerPorIdPromocion(@PathVariable Long id) {
        return promocionService.obtenerPorId(id)
                .map(promocionService::toDTO) // convierte a DTO enriquecido
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PromocionDTO>> listarTodasPromociones() {
        List<PromocionDTO> lista = promocionService.listarTodas().stream()
                .map(promocionService::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PromocionDTO>> buscarPorNombrePromocion(@RequestParam String nombre) {
        List<PromocionDTO> resultado = promocionService.buscarPorNombre(nombre).stream()
                .map(promocionService::toDTO)
                .toList();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<PromocionDTO>> Promocionesactivas(@RequestParam(required = false) LocalDate fecha) {
        LocalDate dia = (fecha != null) ? fecha : LocalDate.now();
        List<PromocionDTO> activas = promocionService.promocionesActivas(dia).stream()
                .map(promocionService::toDTO)
                .toList();
        return ResponseEntity.ok(activas);
    }

    @GetMapping("/{id}/activa")
    public ResponseEntity<Boolean> PromocionestaActiva(@PathVariable Long id, @RequestParam(required = false) LocalDate fecha) {
        LocalDate dia = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(promocionService.estaActiva(id, dia));
    }

    @GetMapping("/con-cantidad-minima")
    public ResponseEntity<List<PromocionDTO>> PromocionconMinimo() {
        List<PromocionDTO> lista = promocionService.conCantidadMinimaRequerida().stream()
                .map(promocionService::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/por-tipo")
    public ResponseEntity<List<PromocionDTO>> porTipoPromociones(@RequestParam String tipo) {
        List<PromocionDTO> lista = promocionService.buscarPorTipo(tipo).stream()
                .map(promocionService::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }
}
