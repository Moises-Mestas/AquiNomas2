package com.example.pedido_db.feign;


import com.example.pedido_db.dto.InventarioCocina;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "servicio-inventario") // Nombre del microservicio
public interface InventarioCocinaFeign {

    // Cliente Feign para obtener los datos del inventario de cocina con Circuit Breaker
    @GetMapping("/inventario_cocina/{id}")
    @CircuitBreaker(name = "inventarioCircuitBreaker", fallbackMethod = "fallbackInventarioCocinaById")
    ResponseEntity<InventarioCocina> listById(@PathVariable Integer id);

    // Fallback method for InventarioCocina
    default ResponseEntity<InventarioCocina> fallbackInventarioCocinaById(Integer id, Throwable e) {
        // Crear una respuesta ficticia de InventarioCocina en caso de fallo
        InventarioCocina inventarioCocina = new InventarioCocina();
        inventarioCocina.setId(id);
        inventarioCocina.setProductoId(null); // No disponible
        inventarioCocina.setCantidadDisponible(BigDecimal.ZERO);
        inventarioCocina.setUnidadMedida(InventarioCocina.UnidadMedida.kg); // Valor por defecto

        return ResponseEntity.ok(inventarioCocina);
    }
}

