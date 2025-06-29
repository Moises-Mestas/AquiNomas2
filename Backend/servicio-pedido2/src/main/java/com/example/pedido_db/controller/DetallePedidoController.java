package com.example.pedido_db.controller;


import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.repository.PedidoRepository;
import com.example.pedido_db.service.DetallePedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/detalle-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;
    private final PedidoRepository pedidoRepository;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService, PedidoRepository pedidoRepository) {
        this.detallePedidoService = detallePedidoService;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public ResponseEntity<List<DetallePedido>> list() {
        List<DetallePedido> detalles = detallePedidoService.listar();

        if (detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(detalles);  // La serialización de "pedido_id" la maneja la entidad
    }


    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> listById(@PathVariable Integer id) {
        Optional<DetallePedido> detallePedido = detallePedidoService.listarPorId(id);

        if (detallePedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detallePedido.get());  // La serialización de "pedido_id" la maneja la entidad
    }






    // Guardar un nuevo detalle de pedido
    @PostMapping
    public ResponseEntity<?> save(@RequestBody DetallePedidoLoteRequest input) {
        List<DetallePedido> guardados = new ArrayList<>();

        try {
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(input.pedidoId);
            if (pedidoOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }
            Pedido pedido = pedidoOpt.get();

            for (DetallePedido item : input.items) {
                item.setPedido(pedido); // ⬅ Asociamos el mismo pedido a todos
                DetallePedido detalleGuardado = detallePedidoService.guardar(item);
                guardados.add(detalleGuardado);
            }

            return ResponseEntity.ok(guardados);
        } catch (InventoryShortageException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar un detalle de pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> update(@PathVariable Integer id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);  // asegura que el detalle tenga el ID correcto
        DetallePedido detalleActualizado = detallePedidoService.actualizar(detallePedido);
        return ResponseEntity.ok(detalleActualizado);
    }

    // Eliminar un detalle de pedido por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        detallePedidoService.eliminar(id);
    }

    // Esta clase es para la petición de un lote de detalle de pedido
    public static class DetallePedidoLoteRequest {
        public Integer pedidoId;
        public List<DetallePedido> items;
    }
}
