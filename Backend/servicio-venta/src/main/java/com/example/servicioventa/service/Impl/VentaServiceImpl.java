package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.Pedido;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }


    @Override
    @Transactional
    public Venta guardarVenta(Venta venta) {
        try {
            Pedido pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());

            if (pedido == null) {
                throw new RuntimeException("El pedido con ID " + venta.getPedidoId() + " no existe.");
            }

            if (!"COMPLETADO".equalsIgnoreCase(pedido.getEstado())) {
                throw new RuntimeException("El pedido con ID " + venta.getPedidoId() + " tiene estado '" + pedido.getEstado() + "' y no puede generar una venta.");
            }

            System.out.println("Creando venta para pedido ID: " + venta.getPedidoId());

            aplicarDescuentoSiCorresponde(venta);

            Venta nuevaVenta = ventaRepository.save(venta);

            System.out.println("Venta guardada con ID: " + nuevaVenta.getId());

            return nuevaVenta;
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El pedido con ID " + venta.getPedidoId() + " no existe en el microservicio de pedidos.");
        }
    }


    @Override
    public Venta actualizar(Venta venta) {
        return null;
    }

    @Override
    public List<Venta> buscarPorNombreCliente(String nombreCliente) {
        return List.of();
    }

    @Override
    public List<Venta> buscarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return List.of();
    }

    @Override
    public Optional<Venta> listarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public void eliminarPorId(Long id) {

    }

    // Metodo de eliminación de pedido directamente desde Feign Client
    public void eliminarPedido(Long pedidoId) {
        try {
            pedidoClient.eliminarPedidoPorId(pedidoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el pedido con ID " + pedidoId, e);
        }
    }

    private void aplicarDescuentoSiCorresponde(Venta venta) {
        if (venta.getPromocion() != null) {
            BigDecimal descuento = venta.getPromocion().getValorDescuento();

            if (venta.getTotal().compareTo(descuento) < 0) {
                throw new RuntimeException("El total de la venta no puede ser menor que el descuento aplicado.");
            }

            venta.setTotal(venta.getTotal().subtract(descuento));
        }
    }
}
