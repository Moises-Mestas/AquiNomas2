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
        List<DetallePedidoDto> todosDetalles;

        try {
            ventas = ventaClient.obtenerTodasVentas();
            todosDetalles = pedidoDetalleClient.obtenerTodosDetallePedidos();
        } catch (Exception e) {
            System.out.println("Error al obtener ventas o detalles: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }

        Map<Integer, Long> frecuenciaClientes = new HashMap<>();
        System.out.println("VENTAS TOTALES: " + ventas.size());

        for (VentaDto venta : ventas) {
            try {
                System.out.println("Procesando venta ID: " + venta.getId());

                if (venta.getPedidoId() == null) {
                    System.out.println("PedidoId NULL en venta " + venta.getId());
                    continue;
                }

                PedidoDto pedido = pedidoDetalleClient.obtenerPedidoPorId(venta.getPedidoId());
                if (pedido == null || pedido.getClienteId() == null) {
                    System.out.println("Pedido o clienteId nulo para venta: " + venta.getId());
                    continue;
                }

                // Filtramos los detalles que pertenecen a este pedido
                List<DetallePedidoDto> detalles = todosDetalles.stream()
                        .filter(d -> d.getPedidoId().equals(pedido.getId()))
                        .collect(Collectors.toList());

                pedido.setDetalles(detalles); // Asignamos los detalles al pedido

                Integer clienteId = pedido.getClienteId();
                System.out.println("ClienteId encontrado: " + clienteId);
                frecuenciaClientes.put(clienteId, frecuenciaClientes.getOrDefault(clienteId, 0L) + 1);

            } catch (Exception e) {
                System.out.println("Error al procesar venta con ID " + venta.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("FRECUENCIA FINAL: " + frecuenciaClientes);

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
        Map<String, Object> resultado = new HashMap<>();

        try {
            // ✅ Formatear fechas correctamente con segundos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String inicioStr = inicio.format(formatter);
            String finStr = fin.format(formatter);

            // ✅ Llamar al Feign client con fechas formateadas correctamente
            List<VentaDto> ventas = Optional.ofNullable(
                    ventaClient.obtenerVentasPorFecha(inicioStr, finStr)
            ).orElse(Collections.emptyList());

            BigDecimal totalMontoVentas = ventas.stream()
                    .map(VentaDto::getTotal)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            resultado.put("cantidadVentas", ventas.size());
            resultado.put("montoTotal", totalMontoVentas);
            resultado.put("fechaInicio", inicioStr);
            resultado.put("fechaFin", finStr);

        } catch (Exception e) {
            System.out.println("Error al obtener ventas por periodo: " + e.getMessage());
            e.printStackTrace();
            resultado.put("error", "No se pudo calcular las ventas en el periodo especificado.");
        }

        return resultado;
    }




    @Override
    public List<Map<String, Object>> obtenerInventariosMasUsados() {
        // Manejo de posibles listas nulas
        List<InventarioCocinaDto> inventariosCocina = Optional.ofNullable(inventarioClient.obtenerInventariosCocina())
                .orElse(Collections.emptyList());
        List<InventarioBarraDto> inventariosBarra = Optional.ofNullable(inventarioClient.obtenerInventariosBarra())
                .orElse(Collections.emptyList());
        List<BodegaDto> bodegas = Optional.ofNullable(inventarioClient.obtenerTodasLasBodegas())
                .orElse(Collections.emptyList());

        Map<Integer, Double> usoPorProducto = new HashMap<>();

        try {
            // Procesar inventarios de cocina
            for (InventarioCocinaDto cocina : inventariosCocina) {
                Integer id = cocina.getId();
                BigDecimal cantidad = cocina.getCantidadDisponible();
                if (id != null && cantidad != null) {
                    usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
                }
            }

            // Procesar inventarios de barra
            for (InventarioBarraDto barra : inventariosBarra) {
                Integer id = barra.getId();
                BigDecimal cantidad = barra.getCantidadDisponible();
                if (id != null && cantidad != null) {
                    usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
                }
            }

            // Procesar bodegas
            for (BodegaDto bodega : bodegas) {
                Integer id = bodega.getId();
                BigDecimal cantidad = bodega.getCantidad();
                if (id != null && cantidad != null) {
                    usoPorProducto.put(id, usoPorProducto.getOrDefault(id, 0.0) + cantidad.doubleValue());
                }
            }

        } catch (Exception e) {
            System.out.println("Error al procesar inventarios: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }

        System.out.println("Productos con cantidades acumuladas: " + usoPorProducto.size());

        // Ordenar los productos más usados
        return usoPorProducto.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
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
        List<DetallePedidoDto> detallePedidos = pedidoDetalleClient.obtenerTodosDetallePedidos();

        // Agrupar por menuId y contar cuántas veces aparece cada uno
        Map<Integer, Long> pedidosPorMenu = detallePedidos.stream()
                .collect(Collectors.groupingBy(DetallePedidoDto::getMenuId, Collectors.counting()));

        // Ordenar por cantidad ascendente
        List<Map.Entry<Integer, Long>> ordenados = pedidosPorMenu.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        // Top 5 más pedidos
        List<Map<String, Object>> masPedidos = ordenados.stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menuId", e.getKey());  // ID del producto (menu)
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        // Top 5 menos pedidos
        List<Map<String, Object>> menosPedidos = ordenados.stream()
                .limit(5)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("menuId", e.getKey());
                    map.put("cantidad", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        // Devolver el resultado
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
        try {
            return reporteRepository.save(reporte);
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprime el error completo en consola
            throw new RuntimeException("Error al guardar el reporte: " + e.getMessage());
        }
    }

    @Override
    public List<Reporte> listarReportes() {
        return reporteRepository.findAll();
    }
}
