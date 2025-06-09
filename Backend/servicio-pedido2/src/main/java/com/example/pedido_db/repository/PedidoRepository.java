package com.example.pedido_db.repository;

import com.example.pedido_db.entity.EstadoPedido;
import com.example.pedido_db.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEstadoPedido(EstadoPedido estadoPedido);

}
