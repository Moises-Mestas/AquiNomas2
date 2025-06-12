package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.Pedido;
import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final PromocionRepository promocionRepository;
    private final PedidoClient pedidoClient;

    public VentaServiceImpl(VentaRepository ventaRepository, PromocionRepository promocionRepository, PedidoClient pedidoClient) {
        this.ventaRepository = ventaRepository;
        this.promocionRepository = promocionRepository;
        this.pedidoClient = pedidoClient;
    }

    @Override
    public List<VentaDTO> listar() {
        List<Venta> ventas = ventaRepository.findAll();

        return ventas.stream().map(venta -> {
            Pedido pedido = null;
            try {
                pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
            } catch (FeignException e) {
                System.out.println("Error al obtener el pedido ID " + venta.getPedidoId() + ": " + e.getMessage());
            }

            // Validación para evitar errores
            String nombreCliente = (pedido != null && pedido.getDetallePedido() != null) ? pedido.getDetallePedido().getNombreCliente() : "Cliente desconocido";
            String nombreMenu = (pedido != null && pedido.getDetallePedido() != null) ? pedido.getDetallePedido().getNombreMenu() : "Menú no disponible";
            int cantidad = (pedido != null && pedido.getDetallePedido() != null) ? pedido.getDetallePedido().getCantidad() : 0;

            String metodoPagoStr = (venta.getMetodoPago() != null) ? venta.getMetodoPago().name() : "SIN DEFINIR";

            return new VentaDTO(
                    venta.getId(),
                    metodoPagoStr,
                    venta.getFechaVenta(),
                    venta.getTotal(),
                    nombreCliente,
                    nombreMenu,
                    cantidad,
                    venta.getPedidoId()
            );
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ResponseEntity<?> guardarVenta(Venta venta) {
        try {
            Pedido pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());

            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ El pedido con ID " + venta.getPedidoId() + " no existe.");
            }

            if (!"COMPLETADO".equalsIgnoreCase(pedido.getEstado())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("⚠ No se puede procesar la venta. El pedido está en estado '" + pedido.getEstado() + "'.");
            }

            // ✅ Calcular el total basado en el detalle del pedido
            BigDecimal totalCalculado = pedido.getDetallePedido().getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(pedido.getDetallePedido().getCantidad()));

            venta.setTotal(totalCalculado);

            aplicarDescuentoSiCorresponde(venta);

            Venta nuevaVenta = ventaRepository.save(venta);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ El pedido con ID " + venta.getPedidoId() + " no existe en el microservicio de pedidos.");
        }
    }



    @Override
    @Transactional
    public ResponseEntity<?> actualizar(Long id, Venta venta) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ El ID de la venta no puede ser nulo.");
        }

        Optional<Venta> ventaExistenteOpt = ventaRepository.findById(id);

        if (ventaExistenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ La venta con ID " + id + " no existe.");
        }

        Venta ventaExistente = ventaExistenteOpt.get();

        ventaExistente.setMetodoPago(venta.getMetodoPago());
        ventaExistente.setFechaVenta(venta.getFechaVenta());

        // ✅ Obtener los datos originales del pedido para calcular el total base
        Pedido pedido = pedidoClient.obtenerPedidoPorId(ventaExistente.getPedidoId());
        BigDecimal totalBase = pedido.getDetallePedido().getPrecioUnitario()
                .multiply(BigDecimal.valueOf(pedido.getDetallePedido().getCantidad()));

        // ✅ Si la promoción se eliminó, restauramos el total original
        if (venta.getPromocionId() == null) {
            ventaExistente.setPromocionId(null);
            ventaExistente.setTotal(totalBase);
        }
        // ✅ Si se asignó una promoción o cambió, aplicamos el nuevo descuento
        else if (!Objects.equals(ventaExistente.getPromocionId(), venta.getPromocionId())) {
            ventaExistente.setPromocionId(venta.getPromocionId());
            aplicarDescuentoSiCorresponde(ventaExistente);
        }

        Venta ventaActualizada = ventaRepository.save(ventaExistente);
        return ResponseEntity.ok(ventaActualizada);
    }


    @Override
    public List<Venta> buscarPorNombreCliente(String nombreCliente) {
        List<Pedido> pedidos = pedidoClient.buscarPedidosPorNombreCliente(nombreCliente);
        List<Long> pedidoIds = pedidos.stream().map(Pedido::getId).toList();
        return ventaRepository.findByPedidoIdIn(pedidoIds);
    }

    @Override
    public List<Venta> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
    }

    @Override
    public Optional<Venta> listarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        ventaRepository.deleteById(id);
    }

    public void eliminarPedido(Long pedidoId) {
        try {
            pedidoClient.eliminarPedidoPorId(pedidoId);
        } catch (Exception e) {
            throw new RuntimeException("❌ Error al eliminar el pedido con ID " + pedidoId, e);
        }
    }

    private void aplicarDescuentoSiCorresponde(Venta venta) {
        if (venta.getPromocionId() != null) {
            Optional<Promocion> promocionOpt = promocionRepository.findById(venta.getPromocionId());

            if (promocionOpt.isPresent()) {
                BigDecimal descuento = promocionOpt.get().getValorDescuento();

                // ✅ Evitar error si venta.getTotal() es null
                if (venta.getTotal() == null) {
                    venta.setTotal(BigDecimal.ZERO);
                }

                if (venta.getTotal().compareTo(descuento) < 0) {
                    throw new RuntimeException("❌ El total de la venta no puede ser menor que el descuento aplicado.");
                }

                venta.setTotal(venta.getTotal().subtract(descuento));
            } else {
                throw new RuntimeException("❌ La promoción con ID " + venta.getPromocionId() + " no existe.");
            }
        }
    }

}