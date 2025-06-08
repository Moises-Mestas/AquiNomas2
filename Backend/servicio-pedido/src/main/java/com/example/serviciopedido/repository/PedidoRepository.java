package com.example.serviciopedido.repository;

import com.example.serviciopedido.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
