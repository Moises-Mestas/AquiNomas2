package com.example.pedido_db.feign;

import com.example.pedido_db.dto.Producto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-proveedor") // Nombre del microservicio
public interface ProductoFeign {

    // Cliente Feign para obtener los datos del cliente con Circuit Breaker
    @GetMapping("/productos/{id}")
    @CircuitBreaker(name = "productoCircuitBreaker", fallbackMethod = "fallbackCProductoById")
    ResponseEntity<Producto> listById(@PathVariable Integer id);


}
