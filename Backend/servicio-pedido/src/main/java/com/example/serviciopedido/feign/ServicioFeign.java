package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.Administrador;
import com.example.serviciopedido.dto.Cliente;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(name = "servicio-cliente", path = "/servicios")
public interface ServicioFeign {

    @GetMapping("/administradores/{id}")
    @CircuitBreaker(name = "administradorCircuitBreaker", fallbackMethod = "fallbackAdministradorById")
    ResponseEntity<Administrador> getAdministradorById(@PathVariable Integer id);

    @GetMapping("/clientes/{id}")
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackClienteById")
    ResponseEntity<Cliente> getClienteById(@PathVariable Integer id);

    // Método fallback para cuando el servicio administrador no responde
    default ResponseEntity<Administrador> fallbackAdministradorById(Integer id, Throwable e) {
        Administrador administrador = new Administrador();
        administrador.setId(id);
        administrador.setNombre("Nombre no disponible");
        administrador.setEmail("Email no disponible");
        administrador.setFechaCreacion(null); // Fecha de fallback actual

        return ResponseEntity.ok(administrador);
    }

    // Método fallback para cuando el servicio cliente no responde
    default ResponseEntity<Cliente> fallbackClienteById(Integer id, Throwable e) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre(""); // Cadena vacía
        cliente.setApellido(""); // Cadena vacía
        cliente.setDni(""); // Cadena vacía
        cliente.setTelefono(""); // Cadena vacía
        cliente.setEmail(""); // Cadena vacía
        cliente.setDireccion(""); // Cadena vacía
        cliente.setFechaRegistro(null); // Fecha de fallback actual

        return ResponseEntity.ok(cliente);
    }
}
