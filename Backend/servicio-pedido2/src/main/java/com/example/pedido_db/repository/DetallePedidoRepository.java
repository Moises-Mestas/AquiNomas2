package com.example.pedido_db.repository;

import com.example.pedido_db.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
