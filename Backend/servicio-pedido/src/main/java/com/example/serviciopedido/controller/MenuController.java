package com.example.serviciopedido.controller;

import com.example.serviciopedido.entity.Menu;
import com.example.serviciopedido.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Menu> listar() {
        return menuService.listar();
    }

    @GetMapping("/{id}")
    public Menu buscarPorId(@PathVariable Integer id) {
        return menuService.listarPorId(id)
                .orElseThrow(() -> new RuntimeException("Menú no encontrado con id: " + id));
    }

    @PostMapping
    public Menu crear(@RequestBody Menu menu) {
        return menuService.guardar(menu);
    }

    @PutMapping("/{id}")
    public Menu actualizar(@PathVariable Integer id, @RequestBody Menu menu) {
        menu.setId(id);
        return menuService.actualizar(menu);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        menuService.eliminar(id);
    }
}
