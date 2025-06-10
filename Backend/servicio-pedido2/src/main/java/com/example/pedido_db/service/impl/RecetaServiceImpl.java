package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Producto;
import com.example.pedido_db.entity.Receta;
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
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @CircuitBreaker(name = "productoCircuitBreaker", fallbackMethod = "fallbackCProductoById")
    public List<Receta> listar() {
        List<Receta> recetas = recetaRepository.findAll();

        // Iterar para cargar el producto de cada receta
        for (Receta receta : recetas) {
            if (receta.getProductoId() != null) {
                try {
                    Producto producto = productoFeign.listById(receta.getProductoId()).getBody();
                    if (producto != null) {
                        receta.setProducto(producto);  // Asegúrate de tener el setter 'setProducto()' en Receta
                    }
                } catch (Exception e) {
                    // En caso de fallo en la llamada al servicio, manejarlo con un valor predeterminado o loguear el error
                    receta.setProducto(new Producto());  // Opcional: Setear un producto vacío en caso de error
                }
            }
        }

        return recetas;
    }

    // Método fallback que se invoca si hay una falla en la llamada al servicio de Producto
    public List<Receta> fallbackCProductoById(Exception ex) {
        // En caso de error, puedes devolver una lista vacía o algún valor predeterminado
        // También podrías loggear el error o realizar otras acciones
        return List.of();  // Devuelve una lista vacía
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
                    if (producto != null) {
                        receta.setProducto(producto);
                    } else {
                        receta.setProducto(new Producto()); // Fallback si no se encuentra el producto
                    }
                } catch (Exception e) {
                    receta.setProducto(new Producto()); // Fallback si ocurre un error en Feign
                }
            }

            return Optional.of(receta);
        }
        return Optional.empty();
    }


    // Método fallback que se invoca si hay una falla en la llamada al servicio de Producto
    public Optional<Receta> fallbackCProductoById(Integer id, Exception ex) {
        // En caso de error, devolver un Optional vacío o alguna lógica de manejo de fallos
        return Optional.empty();  // Retornar Optional vacío en caso de error
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
