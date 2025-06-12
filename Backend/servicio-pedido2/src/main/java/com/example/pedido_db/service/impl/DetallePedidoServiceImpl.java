package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.InventarioCocina;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.entity.Receta;
import com.example.pedido_db.feign.InventarioCocinaFeign;
import com.example.pedido_db.repository.DetallePedidoRepository;
import com.example.pedido_db.repository.RecetaRepository;
import com.example.pedido_db.service.DetallePedidoService;
import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.feign.ClienteFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ClienteFeign clienteFeign;
    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private InventarioCocinaFeign inventarioCocinaFeign;


    public List<DetallePedido> listar() {
        // Traemos todos los pedidos desde la base de datos
        List<DetallePedido> detallePedidos = detallePedidoRepository.findAll();

        // Cargar los detalles para cada pedido
        for (DetallePedido detallePedido : detallePedidos) {
            // No es necesario buscar por ID de detallePedido, ya está cargado en el objeto Pedido
           Pedido Pedido = detallePedido.getPedido();

            // Verificamos si el detallePedido tiene clienteId y lo asignamos
            if (Pedido.getClienteId() != null) {
                try {
                    Cliente cliente = clienteFeign.listById(Pedido.getClienteId()).getBody();
                    if (cliente != null) {
                        Pedido.setCliente(cliente); // Asignamos el cliente al detalle del pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + Pedido.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de llamada al clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + Pedido.getClienteId(), e);
                }
            }

            // Asignamos el detallePedido al pedido
            detallePedido.setPedido(Pedido);
        }

        return detallePedidos; // Devolvemos la lista de pedidos con sus detalles y clientes cargados
    }


    @Override
    public Optional<DetallePedido> listarPorId(Integer id) {
        // Obtener el detalle de pedido por ID desde el repositorio
        Optional<DetallePedido> detallePedidoOptional = detallePedidoRepository.findById(id);

        // Verificamos si el detallePedido existe
        if (detallePedidoOptional.isPresent()) {
            DetallePedido detallePedido = detallePedidoOptional.get();

            // Cargar los detalles del pedido
            Pedido Pedido = detallePedido.getPedido();

            // Verificamos si el detallePedido tiene clienteId y lo asignamos
            if (Pedido.getClienteId() != null) {
                try {
                    Cliente cliente = clienteFeign.listById(Pedido.getClienteId()).getBody();
                    if (cliente != null) {
                        Pedido.setCliente(cliente); // Asignamos el cliente al detalle del pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + Pedido.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de llamada al clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + Pedido.getClienteId(), e);
                }
            }

            // Asignamos el detallePedido al pedido
            detallePedido.setPedido(Pedido);
        }

        // Devolver el detalle de pedido si existe, o un Optional vacío si no se encontró
        return detallePedidoOptional;
    }


    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        // Paso 1: Guardar el detalle del pedido en la base de datos
        DetallePedido detalleGuardado = detallePedidoRepository.save(detallePedido);
        Integer menuId = detallePedido.getMenu().getId();

        // Paso 2: Consultar las recetas asociadas a ese menu_id
        List<Receta> recetas = recetaRepository.findByMenuId(menuId);

        // Paso 3: Para cada receta, obtener el producto_id y la cantidad
        for (Receta receta : recetas) {
            Integer productoId = receta.getProductoId();
            BigDecimal cantidadReceta = receta.getCantidad();

            // Paso 4: Calcular la cantidad total de ese ingrediente
            BigDecimal cantidadTotal = cantidadReceta.multiply(new BigDecimal(detallePedido.getCantidad()));

            // Paso 5: Restar esa cantidad de cantidad_disponible en la tabla receta
            BigDecimal nuevaCantidadDisponible = receta.getCantidadDisponible().subtract(cantidadTotal);

            if (nuevaCantidadDisponible.compareTo(BigDecimal.ZERO) >= 0) {
                // Actualizamos la cantidad disponible de la receta
                receta.setCantidadDisponible(nuevaCantidadDisponible);
                recetaRepository.save(receta); // Guardamos la receta actualizada
            } else {
                throw new RuntimeException("No hay suficiente cantidad de ingrediente para la receta ID: " + receta.getId());
            }
        }
        return detalleGuardado; // Devolvemos el detalle de pedido guardado
    }






    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void eliminar(Integer id) {
        detallePedidoRepository.deleteById(id);
    }
}
