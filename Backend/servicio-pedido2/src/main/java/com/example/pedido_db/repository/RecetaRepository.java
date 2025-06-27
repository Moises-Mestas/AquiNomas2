package com.example.pedido_db.repository;

import com.example.pedido_db.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    List<Receta> findByMenuId(Integer menuId);
    Optional<Receta> findByProductoId(Integer productoId);

}
