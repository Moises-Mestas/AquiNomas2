package com.example.servicioventa.controller;

import com.example.servicioventa.dto.PromocionDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    // 🆕 Crear una nueva promoción
    @PostMapping
    public ResponseEntity<PromocionDTO> crearPromocion(@RequestBody PromocionDTO dto) {
        PromocionDTO creada = promocionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // 🔁 Actualizar una promoción
    @PutMapping("/{id}")
    public ResponseEntity<PromocionDTO> actualizarPromocion(@PathVariable Long id, @RequestBody PromocionDTO dto) {
        return promocionService.actualizar(id, dto)
                .map(p -> ResponseEntity.ok(dto)) // si no quieres devolver el objeto actualizado, puedes retornar `dto`
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDTO> obtenerPromocion(@PathVariable Long id) {
        return promocionService.obtenerPorId(id)
                .map(p -> {
                    PromocionDTO dto = new PromocionDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setDescripcion(p.getDescripcion());
                    dto.setTipoDescuento(p.getTipoDescuento().name());
                    dto.setValorDescuento(p.getValorDescuento());
                    dto.setCantidadMinima(p.getCantidadMinima());
                    dto.setMontoMinimo(p.getMontoMinimo());
                    dto.setFechaInicio(p.getFechaInicio());
                    dto.setFechaFin(p.getFechaFin());
                    dto.setMenu(p.getMenu());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 📋 Listar todas las promociones
    @GetMapping
    public ResponseEntity<List<PromocionDTO>> listarPromociones() {
        List<PromocionDTO> lista = promocionService.listarTodas().stream()
                .map(p -> {
                    PromocionDTO dto = new PromocionDTO();
                    dto.setId(p.getId());
                    dto.setNombre(p.getNombre());
                    dto.setDescripcion(p.getDescripcion());
                    dto.setTipoDescuento(p.getTipoDescuento().name());
                    dto.setValorDescuento(p.getValorDescuento());
                    dto.setCantidadMinima(p.getCantidadMinima());
                    dto.setMontoMinimo(p.getMontoMinimo());
                    dto.setFechaInicio(p.getFechaInicio());
                    dto.setFechaFin(p.getFechaFin());
                    dto.setMenu(p.getMenu());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(lista);
    }

    // ❌ Eliminar promoción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // 🔍 Buscar promociones por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Promocion>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(promocionService.buscarPorNombre(nombre));
    }

    // 🟢 Obtener promociones activas a una fecha
    @GetMapping("/activas")
    public ResponseEntity<List<Promocion>> obtenerActivas(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate fecha) {
        return ResponseEntity.ok(promocionService.promocionesActivas(fecha));
    }

    // ⚙️ Validar si una promoción está activa
    @GetMapping("/{id}/activa")
    public ResponseEntity<Boolean> estaActiva(
            @PathVariable Long id,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate fecha) {
        return ResponseEntity.ok(promocionService.estaActiva(id, fecha));
    }

    // 📦 Obtener promociones con cantidad mínima
    @GetMapping("/con-cantidad-minima")
    public ResponseEntity<List<Promocion>> conCantidadMinima() {
        return ResponseEntity.ok(promocionService.conCantidadMinimaRequerida());
    }

    // 🧮 Buscar por tipo de descuento
    @GetMapping("/por-tipo")
    public ResponseEntity<List<Promocion>> buscarPorTipo(@RequestParam String tipo) {
        return ResponseEntity.ok(promocionService.buscarPorTipo(tipo));
    }
}
