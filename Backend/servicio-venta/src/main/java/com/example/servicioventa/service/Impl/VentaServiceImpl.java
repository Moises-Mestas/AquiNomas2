package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.DetallePedidoDTO;
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

            // Validar si el pedido y el detalle existen
            DetallePedidoDTO detallePedido = (pedido != null) ? pedido.getDetallePedido() : null;
            String nombreCliente = (detallePedido != null) ? detallePedido.getNombreCliente() : "Cliente desconocido";
            String nombreMenu = (detallePedido != null) ? detallePedido.getNombreMenu() : "Menú no disponible";
            int cantidad = (detallePedido != null) ? detallePedido.getCantidad() : 0;
            BigDecimal precioUnitario = (detallePedido != null) ? detallePedido.getPrecioUnitario() : BigDecimal.ZERO;

            return new VentaDTO(
                    venta.getId(),
                    venta.getMetodoPago(),
                    venta.getFechaVenta(),
                    venta.getTotal(),
                    nombreCliente,
                    nombreMenu,
                    cantidad
            );
        }).collect(Collectors.toList());
    }



    @Override
    @Transactional
    public Venta guardarVenta(Venta venta) {
        Pedido pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());

        if (pedido == null) {
            throw new RuntimeException("❌ El pedido con ID " + venta.getPedidoId() + " no existe.");
        }

        if (!"COMPLETADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new RuntimeException("⚠ No se puede procesar la venta. El pedido está en estado '" + pedido.getEstado() + "'.");
        }

        aplicarDescuentoSiCorresponde(venta);
        return ventaRepository.save(venta);
    }


    @Override
    @Transactional
    public Venta actualizar(Venta venta) {
        Optional<Venta> ventaExistenteOpt = ventaRepository.findById(venta.getId());

        if (ventaExistenteOpt.isEmpty()) {
            throw new RuntimeException("❌ La venta con ID " + venta.getId() + " no existe.");
        }

        Venta ventaExistente = ventaExistenteOpt.get();

        // Actualizar los valores editados por el usuario
        ventaExistente.setMetodoPago(venta.getMetodoPago());
        ventaExistente.setFechaVenta(venta.getFechaVenta());

        // Si el precio cambió, recalcular el descuento
        if (venta.getTotal() != null && !venta.getTotal().equals(ventaExistente.getTotal())) {
            ventaExistente.setTotal(venta.getTotal());
            aplicarDescuentoSiCorresponde(ventaExistente);
        }

        return ventaRepository.save(ventaExistente);
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

                if (venta.getTotal().compareTo(descuento) < 0) {
                    throw new RuntimeException("❌ El total de la venta no puede ser menor que el descuento aplicado.");
                }

                // Aplicar el descuento con el nuevo precio actualizado
                venta.setTotal(venta.getTotal().subtract(descuento));
            } else {
                throw new RuntimeException("❌ La promoción con ID " + venta.getPromocionId() + " no existe.");
            }
        }
    }
}