package com.example.serviciopedido.repository;

import com.example.serviciopedido.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
