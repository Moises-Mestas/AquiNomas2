package com.example.pedido_db.controller;

import com.example.pedido_db.entity.Menu;
import com.example.pedido_db.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> crear(@RequestParam("nombre") String nombre,
                                      @RequestParam("descripcion") String descripcion,
                                      @RequestParam("precio") BigDecimal precio,
                                      @RequestParam("tipo") String tipo,
                                      @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        // Validación de campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || tipo.isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Error si algún campo obligatorio está vacío
        }

        // Verificar si la imagen está vacía (si es opcional)
        String imagenNombre = null;
        if (imagen != null && !imagen.isEmpty()) {
            // Guardar la imagen solo si fue enviada
            String directoryPath = "Backend/Fronted/public/"; // Ruta de la carpeta pública en Frontend
            Path path = Paths.get(directoryPath + imagen.getOriginalFilename());

            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());  // Crear directorios si no existen

            // Guardar la imagen en el directorio
            Files.write(path, imagen.getBytes());  // Guardar la imagen en el directorio

            imagenNombre = imagen.getOriginalFilename();  // Solo guardamos el nombre del archivo
        }

        // Crear el objeto Menu con el nombre de la imagen (si se proporcionó)
        Menu menu = new Menu();
        menu.setNombre(nombre);
        menu.setDescripcion(descripcion);
        menu.setPrecio(precio);
        menu.setTipo(tipo);
        menu.setImagen(imagenNombre);  // Si no se envió imagen, se quedará como null

        // Guardar el menú en la base de datos
        Menu savedMenu = menuService.guardar(menu);
        return ResponseEntity.ok(savedMenu);  // Devolver el menú guardado
    }




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

    @PutMapping("/{id}")
    public ResponseEntity<Menu> actualizar(@PathVariable Integer id,
                                           @RequestParam("nombre") String nombre,
                                           @RequestParam("descripcion") String descripcion,
                                           @RequestParam("precio") BigDecimal precio,
                                           @RequestParam("tipo") String tipo,
                                           @RequestParam(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        Menu menu = menuService.listarPorId(id).orElseThrow(() -> new RuntimeException("Menú no encontrado con id: " + id));

        // Validación de campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || precio == null || tipo.isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Error si algún campo obligatorio está vacío
        }

        // Verificar si se sube una nueva imagen
        if (imagen != null && !imagen.isEmpty()) {
            // Si hay una nueva imagen, guardarla
            String directoryPath = "Backend/Fronted/public/";  // Ruta de la carpeta pública
            Path path = Paths.get(directoryPath + imagen.getOriginalFilename());
            Files.createDirectories(path.getParent());  // Crear el directorio si no existe
            Files.write(path, imagen.getBytes());  // Guardar la imagen en la ruta

            menu.setImagen(imagen.getOriginalFilename());  // Actualizar la ruta de la imagen
        }

        // Actualizar los demás campos del menú
        menu.setNombre(nombre);
        menu.setDescripcion(descripcion);
        menu.setPrecio(precio);
        menu.setTipo(tipo);

        // Guardar el menú actualizado en la base de datos
        Menu updatedMenu = menuService.actualizar(menu);
        return ResponseEntity.ok(updatedMenu);  // Devolver el menú actualizado
    }



    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        menuService.eliminar(id);
    }
}