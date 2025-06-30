package com.example.servicioventa.controller;

import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.dto.ResultadoAnalisisPromocionesDTO;
import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;
    @Qualifier("com.example.servicioventa.feing.PedidoClient")
    @Autowired
    private PedidoClient pedidoClient;

    @PostMapping
    public ResponseEntity<VentaDTO> crearVenta(@RequestBody Venta venta) {
        VentaDTO creada = ventaService.crearVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizarVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        VentaDTO actualizada = ventaService.actualizarVenta(id, venta);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Integer id) {
        VentaDTO dto = ventaService.obtenerPorId(id);
        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/promociones-aplicables/{pedidoId}")
    public ResponseEntity<?> obtenerPromocionesAplicables(@PathVariable Integer pedidoId) {
        PedidoDTO pedido = ventaService.obtenerPedidoPorId(pedidoId);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Pedido no encontrado para ID: " + pedidoId);
        }

        List<Promocion> promociones = ventaService.obtenerTodasLasPromociones();
        if (promociones == null || promociones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("⚠️ No hay promociones registradas actualmente.");
        }

        ResultadoAnalisisPromocionesDTO resultado = ventaService.analizarPromocionesAplicables(pedido, promociones);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/promocion/{promocionId}")
    public ResponseEntity<List<Venta>> porPromocion(@PathVariable Integer promocionId) {
        return ResponseEntity.ok(ventaService.listarPorPromocion(promocionId));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<VentaDTO>> porFechas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        // Convierte a OffsetDateTime con zona -05:00
        ZoneOffset zonaPeru = ZoneOffset.of("-05:00");
        OffsetDateTime inicioOffset = inicio.atOffset(zonaPeru);
        OffsetDateTime finOffset = fin.atOffset(zonaPeru);

        return ResponseEntity.ok(ventaService.buscarVentasPorFecha(inicioOffset, finOffset));
    }

    @GetMapping("/metodo-pago")
    public ResponseEntity<List<Venta>> listarPorMetodoPago(@RequestParam("metodo") Venta.MetodoPago metodo) {
        List<Venta> resultado = ventaService.listarPorMetodoPago(metodo);
        return resultado.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(resultado);
    }

    @GetMapping("/pedidos/cliente")
    public ResponseEntity<List<PedidoDTO>> buscarPedidosPorCliente(@RequestParam String nombre) {
        List<PedidoDTO> pedidos = ventaService.filtrarPedidosPorNombreCliente(nombre);
        return pedidos.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pedidos);
    }

    @GetMapping("/pedidos/no-vendidos")
    public ResponseEntity<List<PedidoDTO>> pedidosSinVentaPorCliente(@RequestParam String nombre) {
        List<PedidoDTO> filtrados = ventaService.listarPedidosNoVendidosPorCliente(nombre);
        return filtrados.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(filtrados);
    }
}