package com.example.pedido_db.repository;

import com.example.pedido_db.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
