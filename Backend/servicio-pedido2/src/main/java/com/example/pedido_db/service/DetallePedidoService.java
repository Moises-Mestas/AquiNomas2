package com.example.pedido_db.service;


import com.example.pedido_db.entity.DetallePedido;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {

    // Método para listar todos los detalles de pedido
    List<DetallePedido> listar();

    // Método para obtener un detalle de pedido por su ID
    Optional<DetallePedido> listarPorId(Integer id);

    // Método para guardar un nuevo detalle de pedido
    DetallePedido guardar(DetallePedido detallePedido);

    // Método para actualizar un detalle de pedido existente
    DetallePedido actualizar(DetallePedido detallePedido);

    // Método para eliminar un detalle de pedido por su ID
    void eliminar(Integer id);
}
