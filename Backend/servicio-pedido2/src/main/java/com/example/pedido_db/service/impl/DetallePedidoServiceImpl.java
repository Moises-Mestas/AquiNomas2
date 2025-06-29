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
import jakarta.transaction.Transactional;
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


    // Método para listar todos los detalles de pedido
    public List<DetallePedido> listar() {
        // Traemos todos los detalles desde la base de datos
        List<DetallePedido> detallePedidos = detallePedidoRepository.findAll();

        // Cargar los detalles para cada pedido
        for (DetallePedido detallePedido : detallePedidos) {
            // No es necesario buscar por ID de detallePedido, ya está cargado en el objeto Pedido
            Pedido pedido = detallePedido.getPedido();

            // Verificamos si el detallePedido tiene clienteId y lo asignamos
            if (pedido.getClienteId() != null) {
                try {
                    Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();
                    if (cliente != null) {
                        pedido.setCliente(cliente); // Asignamos el cliente al detalle del pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + pedido.getClienteId());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error al cargar cliente con ID: " + pedido.getClienteId(), e);
                }
            }

            // Asignamos el detallePedido al pedido
            detallePedido.setPedido(pedido);

            // Aseguramos de que el id del pedido esté disponible
            detallePedido.getPedido().setId(pedido.getId());
        }

        return detallePedidos; // Devolvemos la lista de pedidos con sus detalles y clientes cargados
    }


    @Override
    public Optional<DetallePedido> listarPorId(Integer id) {
        // Obtener el detalle de pedido por ID desde el repositorio
        Optional<DetallePedido> detallePedidoOptional = detallePedidoRepository.findById(id);

        if (detallePedidoOptional.isPresent()) {
            DetallePedido detallePedido = detallePedidoOptional.get();

            // Cargar los detalles del pedido
            Pedido pedido = detallePedido.getPedido();

            // Verificamos si el detallePedido tiene clienteId y lo asignamos
            if (pedido.getClienteId() != null) {
                try {
                    Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();
                    if (cliente != null) {
                        pedido.setCliente(cliente); // Asignamos el cliente al detalle del pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + pedido.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de llamada al clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + pedido.getClienteId(), e);
                }
            }

            // Aseguramos de que el id del pedido esté disponible
            detallePedido.getPedido().setId(pedido.getId());

            // Asignamos el detallePedido al pedido
            detallePedido.setPedido(pedido);
        }

        return detallePedidoOptional; // Devolvemos el detalle de pedido si existe, o un Optional vacío si no se encontró
    }


    @Override
    @Transactional
    public DetallePedido guardar(DetallePedido detallePedido) {
        Integer menuId = detallePedido.getMenu().getId();
        List<Receta> recetas = recetaRepository.findByMenuId(menuId);

        // Verificar disponibilidad
        for (Receta receta : recetas) {
            Integer productoId = receta.getProductoId();
            BigDecimal cantidadReceta = receta.getCantidad();
            BigDecimal cantidadTotal = cantidadReceta.multiply(new BigDecimal(detallePedido.getCantidad()));
            BigDecimal cantidadDisponible = receta.getCantidadDisponible();

            if (cantidadDisponible.compareTo(cantidadTotal) < 0) {
                throw new RuntimeException("Stock del ingrediente agotado. Producto ID: " + productoId + " no tiene suficiente cantidad disponible.");
            }
        }

        // Guardar el detalle del pedido
        DetallePedido detalleGuardado = detallePedidoRepository.save(detallePedido);

        // Actualizar stock en receta y inventario cocina
        for (Receta receta : recetas) {
            Integer productoId = receta.getProductoId();
            BigDecimal cantidadReceta = receta.getCantidad();
            BigDecimal cantidadTotal = cantidadReceta.multiply(new BigDecimal(detallePedido.getCantidad()));

            // 🔸 ACTUALIZAR RECETA
            BigDecimal nuevaCantidadDisponible = receta.getCantidadDisponible().subtract(cantidadTotal);
            receta.setCantidadDisponible(nuevaCantidadDisponible);
            recetaRepository.save(receta);

            // 🔸 BUSCAR INVENTARIO COCINA
            ResponseEntity<List<InventarioCocina>> response = inventarioCocinaFeign.listarInventarios();
            List<InventarioCocina> inventarios = response.getBody();

            Optional<InventarioCocina> inventarioOpt = inventarios.stream()
                    .filter(inv -> inv.getProductoId() != null && inv.getProductoId().equals(productoId))
                    .findFirst();

            if (inventarioOpt.isPresent()) {
                InventarioCocina inventario = inventarioOpt.get();

                BigDecimal cantidadDescontar = receta.convertirUnidad(
                        receta.getCantidad(),
                        receta.getUnidadMedida(),
                        inventario.getUnidadMedida()
                ).multiply(new BigDecimal(detallePedido.getCantidad()));

                BigDecimal nuevoStock = inventario.getCantidadDisponible().subtract(cantidadDescontar);
                inventario.setCantidadDisponible(nuevoStock);

                inventarioCocinaFeign.updateInventarioCocina(inventario.getId(), inventario);
            } else {
                throw new RuntimeException("No se encontró inventario para el producto ID: " + productoId);
            }
        }

        return detalleGuardado;
    }



    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        // Paso 1: Verificar si el detalle del pedido existe en la base de datos
        Optional<DetallePedido> detalleExistente = detallePedidoRepository.findById(detallePedido.getId());

        if (!detalleExistente.isPresent()) {
            // Si no existe, lanzamos un error
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + detallePedido.getId());
        }

        // Paso 2: Consultar las recetas asociadas a ese menu_id
        Integer menuId = detallePedido.getMenu().getId();
        List<Receta> recetas = recetaRepository.findByMenuId(menuId);

        // Paso 3: Verificar disponibilidad de stock para todos los ingredientes
        for (Receta receta : recetas) {
            Integer productoId = receta.getProductoId();
            BigDecimal cantidadReceta = receta.getCantidad();

            // Paso 4: Calcular la cantidad total de ese ingrediente para el pedido
            BigDecimal cantidadTotal = cantidadReceta.multiply(new BigDecimal(detallePedido.getCantidad()));

            // Verificar si hay suficiente stock en receta
            BigDecimal cantidadDisponible = receta.getCantidadDisponible();

            if (cantidadDisponible.compareTo(cantidadTotal) < 0) {
                // Si el stock no es suficiente, lanzamos un error y no guardamos el pedido
                throw new RuntimeException("Stock del ingrediente agotado. Producto ID: " + productoId + " no tiene suficiente cantidad disponible.");
            }
        }

        // Paso 5: Actualizar la cantidad disponible de cada receta
        for (Receta receta : recetas) {
            Integer productoId = receta.getProductoId();
            BigDecimal cantidadReceta = receta.getCantidad();

            // Calcular la cantidad total de ese ingrediente
            BigDecimal cantidadTotal = cantidadReceta.multiply(new BigDecimal(detallePedido.getCantidad()));

            // Actualizamos la cantidad disponible en receta
            BigDecimal nuevaCantidadDisponible = receta.getCantidadDisponible().subtract(cantidadTotal);
            receta.setCantidadDisponible(nuevaCantidadDisponible);

            // Guardamos la receta actualizada
            recetaRepository.save(receta);
        }

        // Paso 6: Guardar los cambios en el detallePedido actualizado
        DetallePedido detalleGuardado = detallePedidoRepository.save(detallePedido);

        return detalleGuardado; // Devolvemos el detalle de pedido actualizado
    }

    @Override
    public void eliminar(Integer id) {
        // Primero verificar si el detalle del pedido existe
        Optional<DetallePedido> detalleExistente = detallePedidoRepository.findById(id);

        if (!detalleExistente.isPresent()) {
            // Si no existe, lanzamos un error
            throw new RuntimeException("Detalle de pedido no encontrado con ID: " + id);
        }

        // Si el detalle de pedido existe, lo eliminamos
        detallePedidoRepository.deleteById(id);
    }
}