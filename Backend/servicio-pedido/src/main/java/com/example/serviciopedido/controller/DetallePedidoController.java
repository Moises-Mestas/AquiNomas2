package com.example.serviciopedido.controller;

import com.example.serviciopedido.entity.DetallePedido;
import com.example.serviciopedido.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> listar() {
        return detallePedidoService.listar();
    }

    @GetMapping("/{id}")
    public DetallePedido buscarPorId(@PathVariable Integer id) {
        return detallePedidoService.listarPorId(id)
                .orElseThrow(() -> new RuntimeException("Detalle de pedido no encontrado con id: " + id));
    }

    @PostMapping
    public DetallePedido crear(@RequestBody DetallePedido detallePedido) {
        return detallePedidoService.guardar(detallePedido);
    }

    @PutMapping("/{id}")
    public DetallePedido actualizar(@PathVariable Integer id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);
        return detallePedidoService.actualizar(detallePedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        detallePedidoService.eliminar(id);
    }
}
