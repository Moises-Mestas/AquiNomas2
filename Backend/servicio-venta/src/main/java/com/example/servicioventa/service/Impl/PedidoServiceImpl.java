package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.Pedido;
import com.example.servicioventa.feing.PedidoClient;

public class PedidoServiceImpl {
    private final PedidoClient pedidoClient;

    public PedidoServiceImpl(PedidoClient pedidoClient) {
        this.pedidoClient = pedidoClient;
    }

    public Pedido obtenerPedido(Long pedidoId) {
        return pedidoClient.obtenerPedidoPorId(pedidoId);
    }
}
