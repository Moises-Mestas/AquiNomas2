package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.InventarioCocina;
import com.example.pedido_db.dto.Producto;
import com.example.pedido_db.entity.Receta;
import com.example.pedido_db.feign.InventarioCocinaFeign;
import com.example.pedido_db.feign.ProductoFeign;
import com.example.pedido_db.repository.RecetaRepository;
import com.example.pedido_db.service.DetallePedidoService;
import com.example.pedido_db.service.RecetaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ProductoFeign productoFeign;  // Feign client to fetch Producto data
    @Autowired
    private InventarioCocinaFeign inventarioCocinaFeign;


    @CircuitBreaker(name = "productoCircuitBreaker", fallbackMethod = "fallbackCProductoById")
    public List<Receta> listar() {
        List<Receta> recetas = recetaRepository.findAll();

        for (Receta receta : recetas) {
            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();

                    // Log para ver la respuesta de Feign
                    System.out.println("Receta ID: " + receta.getId() + " -> Producto recuperado: " + producto);

                    if (producto != null && producto.getId() != null && producto.getNombre() != null) {
                        receta.setProducto(producto);
                    } else {
                        receta.setProducto(new Producto());  // Producto vacío si no es válido
                    }
                } catch (Exception e) {
                    System.err.println("Error recuperando producto para receta ID: " + receta.getId() + " -> " + e.getMessage());
                    receta.setProducto(new Producto());  // Producto vacío en caso de error
                }
            }
        }

        return recetas;
    }





    @CircuitBreaker(name = "productoCircuitBreaker", fallbackMethod = "fallbackCProductoById")
    @Override
    public Optional<Receta> listarPorId(Integer id) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();

                    // Verificar si el producto es nulo o incompleto
                    if (producto != null && producto.getId() != null && producto.getNombre() != null) {
                        receta.setProducto(producto);
                    } else {
                        // Si el producto es nulo o incompleto, asignar un producto vacío
                        receta.setProducto(new Producto());  // Producto vacío o con valores predeterminados
                    }
                } catch (Exception e) {
                    // En caso de fallo en la llamada al servicio, asignar un producto vacío o loguear el error
                    receta.setProducto(new Producto()); // Fallback si ocurre un error en Feign
                }
            }

            return Optional.of(receta);
        }
        return Optional.empty();
    }


    @Override
    @Transactional
    public Receta guardar(Receta receta) {
        if (receta.getProductoId() == null) {
            throw new RuntimeException("ProductoId no puede ser nulo en receta");
        }

        ResponseEntity<List<InventarioCocina>> response = inventarioCocinaFeign.listarInventarios();
        List<InventarioCocina> inventarios = response.getBody();

        System.out.println("🧾 Inventarios obtenidos del FeignClient:");
        inventarios.forEach(inv -> System.out.println("➡️ " + inv));


        if (inventarios == null || inventarios.isEmpty()) {
            throw new RuntimeException("No se pudieron recuperar inventarios del servicio remoto.");
        }

        // Buscar todos los inventarios con ese productoId
        List<InventarioCocina> coincidencias = inventarios.stream()
                .filter(inv -> Objects.equals(inv.getProductoId(), receta.getProductoId()))
                .toList();
        System.out.println("🔍 Buscando coincidencias para productoId: " + receta.getProductoId());

        if (coincidencias.isEmpty()) {
            throw new RuntimeException("No se encontró inventario para producto ID: " + receta.getProductoId());
        }

        // Tomar el primero o aplicar lógica (ej. sumar todos)
        InventarioCocina inventarioSeleccionado = coincidencias.get(0);

        if (inventarioSeleccionado.getCantidadDisponible() == null) {
            throw new RuntimeException("Inventario sin cantidad_disponible para producto ID: " + receta.getProductoId());
        }

        receta.setCantidadDisponible(inventarioSeleccionado.getCantidadDisponible());

        Receta recetaGuardada = recetaRepository.save(receta);

        // (Opcional) actualizar solo ese inventario
// NO ACTUALIZAR INVENTARIO EN ESTA ETAPA


        return recetaGuardada;
    }


    @Override
    @Transactional
    public Receta actualizar(Receta receta) {
        if (receta.getId() == null || !recetaRepository.existsById(receta.getId())) {
            throw new RuntimeException("Receta no encontrada");
        }

        if (receta.getProductoId() == null) {
            throw new RuntimeException("ProductoId no puede ser nulo en receta");
        }

        ResponseEntity<List<InventarioCocina>> response = inventarioCocinaFeign.listarInventarios();
        List<InventarioCocina> inventarios = response.getBody();


        System.out.println("🧾 Inventarios obtenidos (actualizar):");
        inventarios.forEach(inv -> System.out.println("➡️ " + inv));

        if (inventarios == null || inventarios.isEmpty()) {
            throw new RuntimeException("No hay inventarios disponibles.");
        }

        List<InventarioCocina> coincidencias = inventarios.stream()
                .filter(inv -> Objects.equals(inv.getProductoId(), receta.getProductoId()))
                .toList();

        System.out.println("🔍 Buscando coincidencias para productoId (actualizar): " + receta.getProductoId());

        if (coincidencias.isEmpty()) {
            throw new RuntimeException("No se encontró inventario para producto ID: " + receta.getProductoId());
        }

        InventarioCocina inventarioSeleccionado = coincidencias.get(0);

        if (inventarioSeleccionado.getCantidadDisponible() == null) {
            throw new RuntimeException("Inventario sin cantidad_disponible para producto ID: " + receta.getProductoId());
        }

        receta.setCantidadDisponible(inventarioSeleccionado.getCantidadDisponible());

        Receta recetaActualizada = recetaRepository.save(receta);

// Restar la cantidad usada en la receta
        BigDecimal cantidadRestante = inventarioSeleccionado.getCantidadDisponible().subtract(receta.getCantidad());

// Asignar la nueva cantidad restante al inventario
        inventarioSeleccionado.setCantidadDisponible(cantidadRestante);

// Actualizar el inventario
        inventarioCocinaFeign.updateInventarioCocina(inventarioSeleccionado.getId(), inventarioSeleccionado);


        return recetaActualizada;
    }

    @Override
    @Transactional
    public void sincronizarDesdeInventario(Integer productoId) {
        if (productoId == null) {
            throw new RuntimeException("ProductoId no puede ser nulo");
        }

        Receta receta = recetaRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada para producto ID: " + productoId));

        ResponseEntity<List<InventarioCocina>> response = inventarioCocinaFeign.listarInventarios();
        List<InventarioCocina> inventarios = response.getBody();

        System.out.println("🧾 Inventarios obtenidos (sincronizar):");
        inventarios.forEach(inv -> System.out.println("➡️ " + inv));

        if (inventarios == null || inventarios.isEmpty()) {
            throw new RuntimeException("No hay inventarios disponibles.");
        }

        List<InventarioCocina> coincidencias = inventarios.stream()
                .filter(inv -> Objects.equals(inv.getProductoId(), productoId))
                .toList();

        System.out.println("🔍 Buscando coincidencias para productoId (sincronizar): " + productoId);

        if (coincidencias.isEmpty()) {
            throw new RuntimeException("No se encontró inventario para producto ID: " + productoId);
        }

        InventarioCocina inventarioSeleccionado = coincidencias.get(0);

        if (inventarioSeleccionado.getCantidadDisponible() == null) {
            throw new RuntimeException("Inventario sin cantidad_disponible para producto ID: " + productoId);
        }

        // Convertimos el stock del inventario a la unidad de la receta (sin modificar el inventario)
        BigDecimal stockConvertido = receta.convertirUnidad(
                inventarioSeleccionado.getCantidadDisponible(),
                inventarioSeleccionado.getUnidadMedida(),
                receta.getUnidadMedida()
        );

        receta.setCantidadDisponible(stockConvertido);
        recetaRepository.save(receta); // Solo se actualiza la receta, no el inventario
    }






    @Override
    public void eliminar(Integer id) {
        if (recetaRepository.existsById(id)) {
            recetaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Receta no encontrada");
        }
    }
}