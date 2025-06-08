package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.AdministradorDTO;
import com.example.serviciopedido.dto.ClienteDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente")
public interface ServicioClienteFeignClient {

    // Método para obtener Administrador por ID
    @GetMapping("/administradores/{id}")
    @CircuitBreaker(name = "administradorCircuitBreaker", fallbackMethod = "fallbackAdministradorById")
    ResponseEntity<AdministradorDTO> getAdministradorById(@PathVariable("id") Integer id);

    // Fallback para Administrador cuando el servicio no responde
    default ResponseEntity<AdministradorDTO> fallbackAdministradorById(Integer id, Throwable e) {
        System.out.println("Error al obtener administrador con id: " + id + ", causa: " + e.getMessage());
        AdministradorDTO administrador = new AdministradorDTO();
        administrador.setId(id);
        administrador.setNombre("Administrador no disponible");
        administrador.setEmail("noemail@dominio.com");
        return ResponseEntity.ok(administrador);
    }

    // Método para obtener Cliente por ID
    @GetMapping("/clientes/{id}")
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackClienteById")
    ResponseEntity<ClienteDTO> getClienteById(@PathVariable("id") Integer id);

    // Fallback para Cliente cuando el servicio no responde
    default ResponseEntity<ClienteDTO> fallbackClienteById(Integer id, Throwable e) {
        System.out.println("Error al obtener cliente con id: " + id + ", causa: " + e.getMessage());
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id);
        cliente.setNombre("Cliente no disponible");
        cliente.setApellido("Apellido no disponible");
        cliente.setDni("DNI no encontrado");
        cliente.setTelefono("Teléfono no disponible");
        cliente.setEmail("Email no disponible");
        cliente.setDireccion("Dirección no disponible");
        return ResponseEntity.ok(cliente);
    }
}
