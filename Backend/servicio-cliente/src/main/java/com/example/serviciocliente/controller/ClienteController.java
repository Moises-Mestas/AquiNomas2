package com.example.serviciocliente.controller;

import com.example.serviciocliente.entity.Cliente;
import com.example.serviciocliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")

public class ClienteController {

    private final ClienteService clienteService;

    // Constructor explícito
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer id) {
        return clienteService.getClienteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }

    @PutMapping
    public Cliente updateCliente(@RequestBody Cliente cliente) {
        return clienteService.updateCliente(cliente);
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable Integer id) {
        clienteService.deleteCliente(id);
    }

    @GetMapping("/buscar-nombre")
    public List<Cliente> findByNombre(@RequestParam String nombre) {
        return clienteService.findByNombre(nombre);
    }

    @GetMapping("/buscar-dni")
    public ResponseEntity<Cliente> findByDni(@RequestParam String dni) {
        return clienteService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recientes")
    public List<Cliente> listarClientesRecientes(@RequestParam(defaultValue = "30") int dias) {
        return clienteService.listarClientesRecientes(dias);
    }
}
