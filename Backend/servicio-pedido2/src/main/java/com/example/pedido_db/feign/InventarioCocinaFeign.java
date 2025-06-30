package com.example.pedido_db.feign;


import com.example.pedido_db.dto.InventarioCocina;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


@FeignClient(name = "servicio-inventario")
public interface InventarioCocinaFeign {

    @GetMapping("/inventario-cocina")
    @CircuitBreaker(name = "inventarioCircuitBreaker", fallbackMethod = "fallbackInventarioCocinaList")
    ResponseEntity<List<InventarioCocina>> listarInventarios();

    @PutMapping("/inventario-cocina/{id}")
    ResponseEntity<InventarioCocina> updateInventarioCocina(
            @PathVariable("id") Integer id,
            @RequestBody InventarioCocina inventarioCocina
    );


    // FallBack
    default ResponseEntity<List<InventarioCocina>> fallbackInventarioCocinaList(Throwable throwable) {
        System.err.println("❌ ENTRÓ AL FALLBACK del InventarioCocinaFeign: " + throwable.getMessage());
        throw new RuntimeException("Error crítico al contactar con servicio de inventario", throwable);
    }



    // ------------------ Fallback ------------------
// ------------------ Fallback ------------------
    default ResponseEntity<InventarioCocina> fallbackInventarioCocinaById(Integer id, Throwable e) {
        InventarioCocina fallback = new InventarioCocina();
        fallback.setId(id);
        fallback.setBodegaId(null); // Añadido porque es parte de tu clase
        fallback.setProductoId(null);
        fallback.setCantidadDisponible(BigDecimal.ZERO);
        fallback.setUnidadMedida("kg"); // Cambiado a un valor explícito por defecto
        return ResponseEntity.ok(fallback);
    }
}