package com.example.serviciopedido.feign;

import com.example.serviciopedido.dto.Cliente;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-cliente", path = "/clientes")
public interface ClienteFeign {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackClienteById")
    ResponseEntity<Cliente> listById(@PathVariable Integer id);

    // Método fallback para cuando el servicio cliente no responde
    default ResponseEntity<Cliente> fallbackClienteById(Integer id, Throwable e) {
        // Crear un Cliente con los valores vacíos o predeterminados
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre(""); // Cadena vacía
        cliente.setApellido(""); // Cadena vacía
        cliente.setDni(""); // Cadena vacía
        cliente.setTelefono(""); // Cadena vacía
        cliente.setEmail(""); // Cadena vacía
        cliente.setDireccion(""); // Cadena vacía
        cliente.setFechaRegistro(null); // `null` para timestamp

        // Devolver el cliente con los datos vacíos
        return ResponseEntity.ok(cliente);
    }
}
