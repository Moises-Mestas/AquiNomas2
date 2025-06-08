package com.example.pedido_db.repository;

import com.example.pedido_db.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
