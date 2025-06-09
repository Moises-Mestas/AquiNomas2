package com.example.pedido_db.controller;


import com.example.pedido_db.entity.Menu;
import com.example.pedido_db.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // Filtrar por rango de precio
    @GetMapping("/filterByPriceRange")
    public ResponseEntity<List<Menu>> filterByPriceRange(
            @RequestParam BigDecimal minPrecio,
            @RequestParam BigDecimal maxPrecio) {
        List<Menu> menus = menuService.listarPorRangoPrecio(minPrecio, maxPrecio);
        if (menus.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(menus);
    }

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
