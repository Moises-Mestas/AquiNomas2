package com.upeu.servicioreporte.service.impl;

import com.upeu.servicioreporte.dto.*;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.*;
import com.upeu.servicioreporte.repository.ReporteRepository;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final ClienteClient clienteClient;
    private final VentaClient ventaClient;
    private final InventarioClient inventarioClient;
    private final DetallePedidoClient detallePedidoClient;
    private final BodegaClient bodegaClient;
    private final AdministradorClient administradorClient;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                              ClienteClient clienteClient,
                              VentaClient ventaClient,
                              InventarioClient inventarioClient,
                              DetallePedidoClient detallePedidoClient,
                              BodegaClient bodegaClient,
                              AdministradorClient administradorClient) {
        this.reporteRepository = reporteRepository;
        this.clienteClient = clienteClient;
        this.ventaClient = ventaClient;
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

        Map<Integer, Long> frecuenciaClientes = ventas.stream()
                .collect(Collectors.groupingBy(VentaDto::getClienteId, Collectors.counting()));

        List<Map.Entry<Integer, Long>> ordenados = frecuenciaClientes.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : ordenados) {
            ClienteDto cliente = clienteClient.obtenerClientePorId(entry.getKey());
            Map<String, Object> map = new HashMap<>();
            map.put("cliente", cliente);
            map.put("frecuencia", entry.getValue());
            resultado.add(map);
        }
        return resultado;
    }

    @Override
    public Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        List<VentaDto> ventas = ventaClient.obtenerVentasPorFecha(inicio, fin);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("inicio", inicio);
        resultado.put("fin", fin);
        resultado.put("cantidadVentas", ventas.size());

        Double totalVentas = ventas.stream()
                .mapToDouble(VentaDto::getTotal)
                .sum();
        resultado.put("totalVentas", totalVentas);

        return resultado;
    }

    @Override
    public List<Map<String, Object>> obtenerInventariosMasUsados() {
        List<InventarioDto> inventarios = inventarioClient.obtenerTodosInventarios();

        Map<String, Double> usoPorProducto = new HashMap<>();

        for (InventarioDto inv : inventarios) {
            String productoId = inv.getProductoId();
            Double cantidad = inv.getCantidad().doubleValue();

            usoPorProducto.put(productoId, usoPorProducto.getOrDefault(productoId, 0.0) + cantidad);
        }

        List<Map.Entry<String, Double>> ordenados = usoPorProducto.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Map.Entry<String, Double> entry : ordenados) {
            Map<String, Object> map = new HashMap<>();
            map.put("productoId", entry.getKey());
            map.put("cantidadUsada", entry.getValue());
            resultado.add(map);
        }
        return resultado;
    }

    @Override
    public Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos() {
        List<DetallePedidoDto> detallePedidos = detallePedidoClient.obtenerTodosDetallePedidos();

        Map<String, Long> pedidosPorProducto = detallePedidos.stream()
                .collect(Collectors.groupingBy(DetallePedidoDto::getProductoId, Collectors.counting()));

        List<Map.Entry<String, Long>> ordenados = pedidosPorProducto.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<Map<String, Object>> masPedidos = ordenados.stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("productoId", e.getKey());
                    m.put("cantidad", e.getValue());
                    return m;
                }).collect(Collectors.toList());

        List<Map<String, Object>> menosPedidos = ordenados.stream()
                .limit(5)
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("productoId", e.getKey());
                    m.put("cantidad", e.getValue());
                    return m;
                }).collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> resultado = new HashMap<>();
        resultado.put("masPedidos", masPedidos);
        resultado.put("menosPedidos", menosPedidos);

        return resultado;
    }

    @Override
    public Map<String, Object> obtenerCostoCantidadPorInsumo(Integer insumoId) {
        List<BodegaDto> bodegas = bodegaClient.obtenerTodasLasBodegas();

        List<BodegaDto> insumoFiltrado = bodegas.stream()
                .filter(b -> b.getProductoId().equals(insumoId.toString()))
                .collect(Collectors.toList());

        Double cantidadTotal = insumoFiltrado.stream()
                .mapToDouble(b -> b.getCantidad().doubleValue())
                .sum();

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("insumoId", insumoId);
        resultado.put("cantidadTotal", cantidadTotal);
        // Aquí podrías agregar costo si lo tienes disponible

        return resultado;
    }

    @Override
    public Map<String, Long> obtenerComprobantesMasUsados() {
        List<VentaDto> ventas = ventaClient.obtenerTodasVentas();

        Map<String, Long> frecuenciaComprobantes = ventas.stream()
                .collect(Collectors.groupingBy(VentaDto::getTipoComprobante, Collectors.counting()));

        return frecuenciaComprobantes;
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
