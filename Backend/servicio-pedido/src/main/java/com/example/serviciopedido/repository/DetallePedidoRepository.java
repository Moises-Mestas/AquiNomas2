package com.example.serviciopedido.repository;

import com.example.serviciopedido.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    // Aquí también puedes agregar consultas personalizadas si es necesario
}
