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
    private InventarioCocinaFeign inventarioCocinaFeign;  // Feign client to fetch Producto data


    @Autowired
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @CircuitBreaker(name = "recetaCircuitBreaker", fallbackMethod = "fallbackRecetaListar")
    public List<Receta> listar() {
        List<Receta> recetas = recetaRepository.findAll();

        for (Receta receta : recetas) {

            // Obtener producto
            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();
                    if (producto != null) {
                        receta.setProducto(producto);
                    }
                } catch (Exception e) {
                    receta.setProducto(new Producto()); // Producto por defecto o loguear el error
                }
            }

            // Obtener inventario de cocina
            if (receta.getInventarioCocinaId() != null) {
                try {
                    InventarioCocina inventario = inventarioCocinaFeign.listById(receta.getInventarioCocinaId()).getBody();
                    if (inventario != null) {
                        receta.setInventarioCocina(inventario);
                    }
                } catch (Exception e) {
                    receta.setInventarioCocina(new InventarioCocina()); // Inventario por defecto o loguear
                }
            }
        }

        return recetas;
    }



    @CircuitBreaker(name = "recetaCircuitBreaker", fallbackMethod = "fallbackRecetaPorId")
    @Override
    public Optional<Receta> listarPorId(Integer id) {
        Optional<Receta> recetaOpt = recetaRepository.findById(id);

        if (recetaOpt.isPresent()) {
            Receta receta = recetaOpt.get();

            // Obtener producto
            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();
                    if (producto != null) {
                        receta.setProducto(producto);
                    } else {
                        receta.setProducto(new Producto());
                    }
                } catch (Exception e) {
                    receta.setProducto(new Producto()); // Fallback si ocurre un error en Feign
                }
            }

            // Obtener inventario de cocina
            if (receta.getInventarioCocinaId() != null) {
                try {
                    InventarioCocina inventario = inventarioCocinaFeign.listById(receta.getInventarioCocinaId()).getBody();
                    if (inventario != null) {
                        receta.setInventarioCocina(inventario);
                    } else {
                        receta.setInventarioCocina(new InventarioCocina());
                    }
                } catch (Exception e) {
                    receta.setInventarioCocina(new InventarioCocina()); // Fallback si ocurre un error
                }
            }

            return Optional.of(receta);
        }

        return Optional.empty();
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
