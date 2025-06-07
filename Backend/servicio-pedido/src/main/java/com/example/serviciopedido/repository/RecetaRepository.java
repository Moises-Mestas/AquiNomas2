package com.example.serviciopedido.repository;

import com.example.serviciopedido.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
