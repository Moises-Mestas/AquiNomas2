package com.example.pedido_db.service.impl;


import com.example.pedido_db.entity.Menu;
import com.example.pedido_db.repository.MenuRepository;
import com.example.pedido_db.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> listar() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> listarPorId(Integer id) {
        return menuRepository.findById(id);
    }

    @Override
    public Menu guardar(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu actualizar(Menu menu) {
        if (menu.getId() != null && menuRepository.existsById(menu.getId())) {
            return menuRepository.save(menu);
        }
        throw new RuntimeException("Menú no encontrado");
    }

    @Override
    public void eliminar(Integer id) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
        } else {
            throw new RuntimeException("Menú no encontrado");
        }
    }
}
