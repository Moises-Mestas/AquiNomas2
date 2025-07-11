package com.example.serviciocliente.service.impl;

import com.example.serviciocliente.entity.Cliente;
import com.example.serviciocliente.repository.ClienteRepository;
import com.example.serviciocliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente updateCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void deleteCliente(Integer id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> getClienteById(Integer id) {
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public List<Cliente> findByNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Optional<Cliente> findByDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    @Override
    public List<Cliente> listarClientesRecientes(int dias) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        return clienteRepository.findClientesRecientes(fechaLimite);
    }
}
