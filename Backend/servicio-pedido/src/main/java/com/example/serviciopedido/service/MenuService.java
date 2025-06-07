package com.example.serviciopedido.service;

import com.example.serviciopedido.entity.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> listar();
    Optional<Menu> listarPorId(Integer id);
    Menu guardar(Menu menu);
    Menu actualizar(Menu menu);
    void eliminar(Integer id);
}
