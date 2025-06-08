package com.example.serviciopedido.service;

import com.example.serviciopedido.entity.DetallePedido;

import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    List<DetallePedido> listar();
    Optional<DetallePedido> listarPorId(Integer id);
    DetallePedido guardar(DetallePedido detallePedido);
    DetallePedido actualizar(DetallePedido detallePedido);
    void eliminar(Integer id);
}
