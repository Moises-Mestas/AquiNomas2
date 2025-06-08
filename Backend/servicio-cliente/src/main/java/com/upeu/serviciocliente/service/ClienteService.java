package com.upeu.serviciocliente.service;

import com.upeu.serviciocliente.entity.Cliente;

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
}
