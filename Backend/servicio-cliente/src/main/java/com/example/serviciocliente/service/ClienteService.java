package com.example.serviciocliente.service;

import com.example.serviciocliente.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    Cliente saveCliente(Cliente cliente);
    Cliente updateCliente(Cliente cliente);
    void deleteCliente(Integer id);
    Optional<Cliente> getClienteById(Integer id);
    List<Cliente> getAllClientes();
    List<Cliente> findByNombre(String nombre);
    Optional<Cliente> findByDni(String dni);
    List<Cliente> listarClientesRecientes(int dias);
}
