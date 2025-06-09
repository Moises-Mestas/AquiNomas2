package com.upeu.servicioreporte.service.impl;

import com.upeu.servicioreporte.dto.*;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.*;
import com.upeu.servicioreporte.repository.ReporteRepository;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final ClienteClient clienteClient;
    private final VentaClient ventaClient;
    private final PedidoClient pedidoClient;
    private final InventarioClient inventarioClient;
    private final DetallePedidoClient detallePedidoClient;
    private final BodegaClient bodegaClient;
    private final AdministradorClient administradorClient;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                              ClienteClient clienteClient,
                              VentaClient ventaClient,
                              PedidoClient pedidoClient,
                              InventarioClient inventarioClient,
                              DetallePedidoClient detallePedidoClient,
                              BodegaClient bodegaClient,
                              AdministradorClient administradorClient) {
        this.reporteRepository = reporteRepository;
        this.clienteClient = clienteClient;
        this.ventaClient = ventaClient;
        this.pedidoClient = pedidoClient;
        this.inventarioClient = inventarioClient;
        this.detallePedidoClient = detallePedidoClient;
        this.bodegaClient = bodegaClient;
        this.administradorClient = administradorClient;
    }

    @Override
    public List<Reporte> findAll() {
        return reporteRepository.findAll();
    }

    @Override
    public Optional<Reporte> findById(Integer id) {
        return reporteRepository.findById(id);
    }

    @Override
    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    @Override
    public void deleteById(Integer id) {
        reporteRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> obtenerClientesMasFrecuentes() {
        List<VentaDto> ventas = ventaClient.obtenerTodasVentas();

        Map<Integer, Long> frecuenciaClientes = new HashMap<>();

        for (VentaDto venta : ventas) {
            PedidoDto pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
            if (pedido != null && pedido.getClienteId() != null) {
                Integer clienteId = pedido.getClienteId();
                frecuenciaClientes.put(clienteId, frecuenciaClientes.getOrDefault(clienteId, 0L) + 1);
            }
        }

        return frecuenciaClientes.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    ClienteDto cliente = clienteClient.obtenerClientePorId(entry.getKey());
                    map.put("cliente", cliente);
                    map.put("frecuencia", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        List<VentaDto> ventas = ventaClient.obtenerVentasPorFecha(inicio, fin);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("inicio", inicio);
        resultado.put("fin", fin);
        resultado.put("cantidadVentas", ventas.size());

        BigDecimal totalVentas = ventas.stream()
                .map(VentaDto::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        resultado.put("totalVentas", totalVentas);
        return resultado;
    }

    @Override
    public List<Map<String, Object>> obtenerInventariosMasUsados() {
        List<InventarioDto> inventarios = inventarioClient.obtenerTodosInventarios();
        Map<String, Double> usoPorProducto = new HashMap<>();

        for (InventarioDto inv : inventarios) {
            String productoId = String.valueOf(inv.getProductoId()); // convertir a String
            BigDecimal cantidad = inv.getCantidadDisponible();

            if (productoId != null && cantidad != null) {
                usoPorProducto.put(productoId,
                        usoPorProducto.getOrDefault(productoId, 0.0) + cantidad.doubleValue());
            }
        }

        return usoPorProducto.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productoId", entry.getKey());
                    map.put("cantidadUsada", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos() {
        List<DetallePedidoDto> detallePedidos = detallePedidoClient.obtenerTodosDetallePedidos();

        Map<Integer, Long> pedidosPorProducto = detallePedidos.stream()
                .collect(Collectors.groupingBy(DetallePedidoDto::getProductoId, Collectors.counting()));

        List<Map.Entry<Integer, Long>> ordenados = pedidosPorProducto.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<Map<String, Object>> masPedidos = ordenados.stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productoId", e.getKey().toString());
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> menosPedidos = ordenados.stream()
                .limit(5)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productoId", e.getKey().toString());
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> resultado = new HashMap<>();
        resultado.put("masPedidos", masPedidos);
        resultado.put("menosPedidos", menosPedidos);
        return resultado;
    }



    @Override
    public Map<String, Object> obtenerCostoCantidadPorInsumo(Integer insumoId) {
        List<BodegaDto> bodegas = bodegaClient.obtenerTodasLasBodegas();
        Double cantidadTotal = bodegas.stream()
                .filter(b -> b.getProductoId().equals(insumoId.toString()))
                .mapToDouble(b -> b.getCantidad().doubleValue())
                .sum();

        return Map.of(
                "insumoId", insumoId,
                "cantidadTotal", cantidadTotal
        );
    }

    @Override
    public Map<String, Long> obtenerComprobantesMasUsados() {
        List<VentaDto> ventas = ventaClient.obtenerTodasVentas();
        return ventas.stream()
                .collect(Collectors.groupingBy(VentaDto::getMetodoPago, Collectors.counting()));
    }

    @Override
    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
}
