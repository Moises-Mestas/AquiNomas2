package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.DetallePedidoDTO;
import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PedidoClient pedidoClient;

    @Autowired
    private PromocionRepository promocionRepository;

    @Override
    public VentaDTO crearVenta(Venta venta) {
        if (existeVentaPorPedidoId(venta.getPedidoId())) {
            throw new IllegalStateException("Ya existe una venta registrada para ese pedido.");
        }

        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
        if (pedido == null || !"COMPLETADO".equalsIgnoreCase(pedido.getEstadoPedido())) {
            throw new IllegalStateException("El pedido debe estar COMPLETADO para registrar la venta.");
        }

        // 🛠️ Asignar precio unitario si falta
        normalizarPreciosPedido(pedido);

        BigDecimal total = calcularTotalPedido(pedido.getDetalles());
        venta.setCliente(pedido.getClienteId());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotal(total);
        aplicarDescuentoSiCorresponde(venta, pedido);

        Venta guardada = ventaRepository.save(venta);
        return new VentaDTO(guardada, pedido);
    }


    @Override
    public VentaDTO actualizarVenta(Integer id, Venta nuevaVenta) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada."));

        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());

        // 🛠️ Asignar precios si no están definidos
        normalizarPreciosPedido(pedido);

        venta.setMetodoPago(nuevaVenta.getMetodoPago());
        venta.setFechaVenta(nuevaVenta.getFechaVenta());
        venta.setPromocion(nuevaVenta.getPromocion());

        BigDecimal total = calcularTotalPedido(pedido.getDetalles());
        venta.setTotal(total);
        aplicarDescuentoSiCorresponde(venta, pedido);

        return new VentaDTO(ventaRepository.save(venta), pedido);
    }


    @Override
    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> obtenerPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public void eliminarPorId(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> listarPorCliente(Integer cliente) {
        return ventaRepository.findByCliente(cliente);
    }

    @Override
    public List<Venta> listarPorRangoFecha(LocalDateTime desde, LocalDateTime hasta) {
        return ventaRepository.findByFechaVentaBetween(desde, hasta);
    }

    @Override
    public List<Venta> listarPorMetodoPago(Venta.MetodoPago metodo) {
        return ventaRepository.findByMetodoPago(metodo);
    }

    @Override
    public List<Venta> listarPorPromocion(Integer promocionId) {
        return ventaRepository.findByPromocionId(promocionId);
    }

    @Override
    public BigDecimal calcularTotalVentasPorCliente(Integer clienteId) {
        return ventaRepository.totalVentasPorCliente(clienteId).orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean existeVentaPorPedidoId(Integer pedidoId) {
        return ventaRepository.existsByPedidoId(pedidoId);
    }

    @Override
    public List<VentaDTO> listarVentas() {
        return ventaRepository.findAll().stream()
                .map(v -> new VentaDTO(v, pedidoClient.obtenerPedidoPorId(v.getPedidoId())))
                .toList();
    }

    @Override
    public List<VentaDTO> buscarVentasPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin).stream()
                .map(v -> new VentaDTO(v, pedidoClient.obtenerPedidoPorId(v.getPedidoId())))
                .toList();
    }

    @Override
    public List<Venta> buscarPorNombreCliente(String nombreCliente) {
        List<PedidoDTO> pedidos = pedidoClient.buscarPedidosPorNombreCliente(nombreCliente);
        List<Integer> pedidoIds = pedidos.stream()
                .map(PedidoDTO::getId)
                .filter(Objects::nonNull)
                .toList();
        return ventaRepository.findByPedidoIdIn(pedidoIds);
    }

    // Utilidades internas

    private BigDecimal calcularTotalPedido(List<DetallePedidoDTO> detalles) {
        return detalles.stream()
                .filter(d -> d.getPrecioUnitario() != null)
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void normalizarPreciosPedido(PedidoDTO pedido) {
        if (pedido.getDetalles() == null) return;

        for (DetallePedidoDTO d : pedido.getDetalles()) {
            if (d.getPrecioUnitario() == null) {
                System.err.println("⚠️ Precio unitario ausente para menú ID " + d.getMenuId() + ". Asignando 0 temporalmente.");
                d.setPrecioUnitario(BigDecimal.ZERO); // O valor por defecto si sabes que corresponde
            }
        }
    }

    private void aplicarDescuentoSiCorresponde(Venta venta, PedidoDTO pedido) {
        Promocion promo = venta.getPromocion();
        if (promo == null || promo.getValorDescuento() == null) return;

        if (promo.getFechaInicio() != null && promo.getFechaFin() != null) {
            LocalDate hoy = LocalDate.now();
            if (hoy.isBefore(promo.getFechaInicio()) || hoy.isAfter(promo.getFechaFin())) return;
        }

        BigDecimal total = venta.getTotal();
        BigDecimal descuento;

        if (promo.getTipoDescuento() == Promocion.TipoDescuento.PORCENTAJE) {
            descuento = total.multiply(promo.getValorDescuento()).divide(BigDecimal.valueOf(100));
        } else {
            descuento = promo.getValorDescuento();
        }

        if (descuento.compareTo(total) < 0) {
            venta.setTotal(total.subtract(descuento));
        }
    }
}
