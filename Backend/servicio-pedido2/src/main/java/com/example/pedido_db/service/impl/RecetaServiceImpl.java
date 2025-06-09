package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Producto;
import com.example.pedido_db.entity.Receta;
import com.example.pedido_db.feign.ProductoFeign;
import com.example.pedido_db.repository.RecetaRepository;
import com.example.pedido_db.service.DetallePedidoService;
import com.example.pedido_db.service.RecetaService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private ProductoFeign productoFeign;  // Feign client to fetch Producto data


    @Autowired
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @CircuitBreaker(name = "productoCircuitBreaker", fallbackMethod = "fallbackProducto")
    public List<Receta> listar() {
        List<Receta> recetas = recetaRepository.findAll();

        if (recetas.isEmpty()) {
            System.out.println("No se encontraron recetas en la base de datos.");
            return recetas;
        }

        System.out.println("Recetas obtenidas: " + recetas);

        for (Receta receta : recetas) {
            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();
                    receta.setProducto(producto);
                } catch (Exception e) {
                    System.out.println("Error al obtener producto con ID " + receta.getProductoId() + ": " + e.getMessage());
                    receta.setProducto(null); // Evita que falle la ejecución
                }
            }
        }

        return recetas;
    }


    public List<Receta> fallbackProducto(Throwable throwable) {
        System.out.println("Fallback activado por error: " + throwable.getMessage());

        // Puedes retornar recetas sin producto o una lista vacía según lo que prefieras
        return recetaRepository.findAll().stream()
                .map(receta -> {
                    receta.setProducto(null);
                    return receta;
                })
                .toList();
    }




    @Override
    public Optional<Receta> listarPorId(Integer id) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        // Verificar si la receta existe
        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Si el productoId no es null, cargar el producto mediante Feign
            if (receta.getProductoId() != null) {
                Producto producto = productoFeign.listById(receta.getProductoId()).getBody();
                receta.setProducto(producto);  // Establecer el producto relacionado
            }

            return Optional.of(receta);  // Retornar la receta con el producto cargado
        } else {
            return Optional.empty();  // Si no se encuentra la receta, retornar Optional vacío
        }
    }

    @Override
    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    @Override
    public Receta actualizar(Receta receta) {
        if (receta.getId() != null && recetaRepository.existsById(receta.getId())) {
            return recetaRepository.save(receta);
        }
        throw new RuntimeException("Receta no encontrada");
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
