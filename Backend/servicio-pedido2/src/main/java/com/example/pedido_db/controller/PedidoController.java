package com.example.pedido_db.controller;

import com.example.pedido_db.entity.EstadoPedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

//    @Autowired
//    private PedidoService pedidoService;
//
//    // Endpoint para obtener el historial de pedidos de un cliente
//    @GetMapping("/historyByCliente")
//    public ResponseEntity<List<Pedido>> getPedidoHistoryByCliente(@RequestParam Integer clienteId) {
//        List<Pedido> pedidos = pedidoService.getHistoryByCliente(clienteId);
//        if (pedidos.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(pedidos);
//    }



    // Filtrar pedidos por estado
    @GetMapping("/filter")
    public ResponseEntity<List<Pedido>> filterByStatus(@RequestParam EstadoPedido estadoPedido) {
        List<Pedido> pedidos = pedidoService.listarPorEstado(estadoPedido);
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos); // Retorna los pedidos con estado filtrado
    }




    @GetMapping
    public ResponseEntity<List<Pedido>> list() {
        List<Pedido> pedidos = pedidoService.listar();
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }
    // Guardar un nuevo pedido
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Pedido pedido) {
        try {
            Pedido pedidoGuardado = pedidoService.guardar(pedido);
            return ResponseEntity.ok(pedidoGuardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Actualizar un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable Integer id, @RequestBody Pedido pedido) {
        pedido.setId(id);  // asegura que el pedido tenga
        Pedido pedidoActualizado = pedidoService.actualizar(pedido);
        return ResponseEntity.ok(pedidoActualizado);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Pedido> listById(@PathVariable Integer id) {
        Optional<Pedido> pedido = pedidoService.listarPorId(id);
        if (pedido.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido.get());
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        pedidoService.eliminar(id);
    }


}