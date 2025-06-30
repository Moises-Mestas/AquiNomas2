package com.upeu.servicioreporte.service.impl;

import com.upeu.servicioreporte.dto.*;
import com.upeu.servicioreporte.entity.Reporte;
import com.upeu.servicioreporte.feign.*;
import com.upeu.servicioreporte.repository.ReporteRepository;
import com.upeu.servicioreporte.service.ReporteService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    public Reporte actualizarReporte(Long id, Reporte reporteActualizado) {
        Reporte existente = reporteRepository.findById(id.intValue()) // Convertimos Long a Integer
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        // Actualizamos todos los campos excepto ID y fechaCreacion
        existente.setAdministradorId(reporteActualizado.getAdministradorId());
        existente.setVentaId(reporteActualizado.getVentaId());
        existente.setBodegaId(reporteActualizado.getBodegaId());
        existente.setClienteId(reporteActualizado.getClienteId());
        existente.setInventarioCocinaId(reporteActualizado.getInventarioCocinaId());
        existente.setInventarioBarraId(reporteActualizado.getInventarioBarraId());
        existente.setDetallePedidoId(reporteActualizado.getDetallePedidoId());
        existente.setDescripcion(reporteActualizado.getDescripcion());
        existente.setDetalles(reporteActualizado.getDetalles());
        existente.setTipo(reporteActualizado.getTipo());

        // No actualizamos la fecha de creación ni el ID

        return reporteRepository.save(existente);
    }

    @Override
    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id.intValue())) {
            throw new RuntimeException("Reporte no encontrado para eliminar");
        }
        reporteRepository.deleteById(id.intValue());
    }


    @Override
    public List<Map<String, Object>> obtenerProductosMasRentables() {
        List<DetallePedidoDto> detalles = Optional.ofNullable(pedidoDetalleClient.obtenerTodosDetallePedidos())
                .orElse(Collections.emptyList());

        // Map<MenuId, TotalIngresos>
        Map<Integer, BigDecimal> ingresosPorProducto = new HashMap<>();
        Map<Integer, MenuDto> menus = new HashMap<>();

        for (DetallePedidoDto detalle : detalles) {
            MenuDto menu = detalle.getMenu();
            if (menu != null && menu.getId() != null && menu.getPrecio() != null && detalle.getCantidad() != null) {
                Integer menuId = menu.getId();
                BigDecimal subtotal = menu.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad()));

                ingresosPorProducto.merge(menuId, subtotal, BigDecimal::add);
                menus.putIfAbsent(menuId, menu); // Guardar solo una vez
            }
        }

        return ingresosPorProducto.entrySet().stream()
                .sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> datos = new HashMap<>();
                    MenuDto menu = menus.get(entry.getKey());
                    datos.put("menuId", menu.getId());
                    datos.put("nombre", menu.getNombre());
                    datos.put("precioUnitario", menu.getPrecio());
                    datos.put("totalGenerado", entry.getValue());
                    return datos;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> obtenerCantidadVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        Map<String, Object> resultado = new HashMap<>();

        try {
            // ✅ Zona horaria Perú (-05:00)
            ZoneOffset zonaPeru = ZoneOffset.of("-05:00");
            OffsetDateTime inicioOffset = inicio.atOffset(zonaPeru);
            OffsetDateTime finOffset = fin.atOffset(zonaPeru);

            // ✅ Usar formato compatible con OffsetDateTime esperado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            String inicioStr = inicioOffset.format(formatter); // Ej: 2024-05-01T00:00:00-05:00
            String finStr = finOffset.format(formatter);       // Ej: 2024-05-31T23:59:59-05:00

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
            System.out.println("❌ Error al obtener ventas por periodo: " + e.getMessage());
            e.printStackTrace();
            resultado.put("error", "No se pudo calcular las ventas en el periodo especificado.");
        }

        return resultado;
    }


    @Override
    public Map<String, List<Map<String, Object>>> obtenerPlatosBebidasMasMenosPedidos() {
        List<DetallePedidoDto> detallePedidos = pedidoDetalleClient.obtenerTodosDetallePedidos();

        // Agrupar por menuId y contar cuántas veces aparece cada uno
        Map<Integer, Long> pedidosPorMenu = detallePedidos.stream()
                .collect(Collectors.groupingBy(DetallePedidoDto::getMenuId, Collectors.counting()));

        // Map de menuId → MenuDto (para recuperar nombres)
        Map<Integer, MenuDto> menus = detallePedidos.stream()
                .map(DetallePedidoDto::getMenu)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(MenuDto::getId, m -> m, (m1, m2) -> m1));

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
                    map.put("menuId", e.getKey());
                    map.put("nombre", menus.containsKey(e.getKey()) ? menus.get(e.getKey()).getNombre() : "N/A");
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
                    map.put("nombre", menus.containsKey(e.getKey()) ? menus.get(e.getKey()).getNombre() : "N/A");
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


    private MenuDto obtenerMenuPorId(Integer menuId) {
        List<DetallePedidoDto> detalles = pedidoDetalleClient.obtenerTodosDetallePedidos();
        return detalles.stream()
                .map(DetallePedidoDto::getMenu)
                .filter(m -> m != null && Objects.equals(m.getId(), menuId))
                .findFirst()
                .orElse(null);
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


