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

@FeignClient(name = "servicio-inventario") // Nombre del microservicio
public interface InventarioCocinaFeign {

    // Cliente Feign para obtener los datos del inventario de cocina con Circuit Breaker
    @GetMapping("/inventario_cocina/{productoId}")
    ResponseEntity<InventarioCocina> obtenerInventarioPorProductoId(@PathVariable Integer productoId);

    @PutMapping("/inventario_cocina/{productoId}")
    void actualizarInventario(@PathVariable Integer productoId, @RequestBody InventarioCocina inventario);

    // Fallback method for InventarioCocina
    default ResponseEntity<InventarioCocina> fallbackInventarioCocinaById(Integer id, Throwable e) {
        // Crear una respuesta ficticia de InventarioCocina en caso de fallo
        InventarioCocina inventarioCocina = new InventarioCocina();
        inventarioCocina.setId(id);
        inventarioCocina.setProductoId(id);  // Aquí podrías setear un valor por defecto si lo necesitas
        inventarioCocina.setCantidadDisponible(BigDecimal.ZERO);  // Valor por defecto
        inventarioCocina.setUnidadMedida(InventarioCocina.UnidadMedida.KG);  // Valor por defecto

        // Retornar la respuesta de InventarioCocina con valores por defecto
        return ResponseEntity.ok(inventarioCocina);
    }
}

