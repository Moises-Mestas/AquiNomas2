package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.AdministradorDTO;
import com.example.serviciopedido.dto.ClienteDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente")
public interface ServicioClienteFeignClient {

    // Método para obtener Administrador por ID
    @CircuitBreaker(name = "administradorCircuitBreaker", fallbackMethod = "getAdministradorByIdFallback")
    @GetMapping("/administradores/{id}")
    AdministradorDTO getAdministradorById(@PathVariable("id") Integer id);

    // Fallback para Administrador
    default AdministradorDTO getAdministradorByIdFallback(Integer id, Throwable t) {
        System.out.println("Error al obtener administrador con id: " + id + ", causa: " + t.getMessage());
        return new AdministradorDTO(id, "Administrador No Disponible", "noemail@dominio.com", null);
    }

    // Método para obtener Cliente por ID
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "getClienteByIdFallback")
    @GetMapping("/clientes/{id}")
    ClienteDTO getClienteById(@PathVariable("id") Integer id);

    // Fallback para Cliente
    default ClienteDTO getClienteByIdFallback(Integer id, Throwable t) {
        System.out.println("Error al obtener cliente con id: " + id + ", causa: " + t.getMessage());
        return new ClienteDTO(id, "Cliente No Disponible", "No Disponible", "00000000", "0000000", "noemail@dominio.com", "Sin Dirección", null);
    }
}
