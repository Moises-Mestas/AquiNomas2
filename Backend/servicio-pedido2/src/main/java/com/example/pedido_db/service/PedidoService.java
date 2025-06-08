package com.example.pedido_db.service;

import com.example.pedido_db.entity.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    // Método para listar todos los pedidos
    List<Pedido> listar();

    // Método para obtener un pedido por su ID
    Optional<Pedido> listarPorId(Integer id);

    // Método para guardar un nuevo pedido
    Pedido guardar(Pedido pedido);

    // Método para actualizar un pedido existente
    Pedido actualizar(Pedido pedido);

    // Método para eliminar un pedido por su ID
    void eliminar(Integer id);
}
