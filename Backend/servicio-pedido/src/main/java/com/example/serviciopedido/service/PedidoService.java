package com.example.serviciopedido.service;

import com.example.serviciopedido.entity.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> listar();
    Optional<Pedido> listarPorId(Integer id);
    Pedido guardar(Pedido pedido);
    Pedido actualizar(Pedido pedido);
    void eliminar(Integer id);
}
