package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.Administrador;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(name = "servicio-cliente", path = "/administradores")
public interface AdministradorFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "administradorCircuitBreaker", fallbackMethod = "fallbackAdministradorById")
    ResponseEntity<Administrador> getById(@PathVariable Integer id);

    // Método fallback para cuando el servicio administrador no responde
    default ResponseEntity<Administrador> fallbackAdministradorById(Integer id, Throwable e) {
        // Crear un Administrador con los valores vacíos o predeterminados
        Administrador administrador = new Administrador();
        administrador.setId(id);
        administrador.setNombre("Nombre no disponible");
        administrador.setEmail("Email no disponible");
        administrador.setFechaCreacion(null); // Si es necesario, se puede poner la fecha actual

        // Devolver el Administrador con los valores predeterminados
        return ResponseEntity.ok(administrador);
    }
}
