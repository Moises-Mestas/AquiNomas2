package com.upeu.servicioreporte.service.impl;

import com.upeu.servicioreporte.dto.*;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.*;
import com.upeu.servicioreporte.repository.ReporteRepository;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final VentaClient ventaClient;
    private final InventarioClient inventarioClient;
    private final ClienteAdministradorClient clienteAdministradorClient;
    private final PedidoDetalleClient pedidoDetalleClient;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                              ClienteAdministradorClient clienteAdministradorClient,
                              VentaClient ventaClient,
                              PedidoDetalleClient pedidoDetalleClient,
                              InventarioClient inventarioClient) {
        this.reporteRepository = reporteRepository;
        this.clienteAdministradorClient = clienteAdministradorClient;
        this.ventaClient = ventaClient;
        this.pedidoDetalleClient = pedidoDetalleClient;
        this.inventarioClient = inventarioClient;
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
        List<VentaDto> ventas;
        try {
            ventas = ventaClient.obtenerTodasVentas();
        } catch (Exception e) {
            System.out.println("Error al obtener ventas: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }

        Map<Integer, Long> frecuenciaClientes = new HashMap<>();

        for (VentaDto venta : ventas) {
            try {
                if (venta.getPedidoId() == null) continue;
                PedidoDto pedido = pedidoDetalleClient.obtenerPedidoPorId(venta.getPedidoId());
                if (pedido != null && pedido.getClienteId() != null) {
                    Integer clienteId = pedido.getClienteId();
                    frecuenciaClientes.put(clienteId, frecuenciaClientes.getOrDefault(clienteId, 0L) + 1);
                }
            } catch (Exception e) {
                System.out.println("Error al procesar venta con ID " + venta.getId() + ": " + e.getMessage());
            }
        }

        return frecuenciaClientes.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    try {
                        ClienteDto cliente = clienteAdministradorClient.obtenerClientePorId(entry.getKey());
                        map.put("cliente", cliente);
                    } catch (Exception e) {
                        System.out.println("Error al obtener cliente con ID " + entry.getKey() + ": " + e.getMessage());
                        map.put("cliente", null);
                    }
                    map.put("frecuencia", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        List<VentaDto> ventas = ventaClient.obtenerVentasPorFecha(inicio.toString(), fin.toString());

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalVentas", ventas.size());
        resultado.put("fechaInicio", inicio);
        resultado.put("fechaFin", fin);

        BigDecimal totalVentas = ventas.stream()
                .map(VentaDto::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        resultado.put("totalVentas", totalVentas);
        return resultado;
    }


    @Override
    public List<Map<String, Object>> obtenerInventariosMasUsados() {
        // Traer inventarios de cocina, barra y bodegas
        List<InventarioCocinaDto> inventariosCocina = inventarioClient.obtenerInventariosCocina();
        List<InventarioBarraDto> inventariosBarra = inventarioClient.obtenerInventariosBarra();
        List<BodegaDto> bodegas = inventarioClient.obtenerTodasLasBodegas();

        Map<Integer, Double> usoPorProducto = new HashMap<>();

        // Procesar inventarios de cocina
        for (InventarioCocinaDto cocina : inventariosCocina) {
            Integer id = cocina.getId(); // Aquí tomamos el ID directamente
            BigDecimal cantidad = cocina.getCantidadDisponible(); // La cantidad disponible es de tipo BigDecimal
            if (id != null && cantidad != null) {
                usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
            }
        }

        // Procesar inventarios de barra
        for (InventarioBarraDto barra : inventariosBarra) {
            Integer id = barra.getId(); // Tomamos el ID
            BigDecimal cantidad = barra.getCantidadDisponible(); // Cantidad disponible de tipo BigDecimal
            if (id != null && cantidad != null) {
                usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
            }
        }

        // Procesar bodegas (si es necesario)
        for (BodegaDto bodega : bodegas) {
            Integer id = bodega.getId(); // Tomamos el ID de la bodega
            BigDecimal cantidad = bodega.getCantidad(); // Ahora usamos el campo cantidad
            if (id != null && cantidad != null) {
                usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
            }
        }

        // Ordenar los productos más usados
        return usoPorProducto.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productoId", entry.getKey()); // Usamos el ID aquí
                    map.put("cantidadUsada", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }



    @Override
    public Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos() {
        List<DetallePedidoDto> detallePedidos = pedidoDetalleClient.obtenerTodosDetallePedidos();

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
        List<BodegaDto> bodegas = inventarioClient.obtenerTodasLasBodegas();
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
