package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.*;
import com.example.servicioventa.entity.MenuPromocion;
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
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        if (venta == null || venta.getPedidoId() == null) {
            throw new IllegalArgumentException("❌ La venta o el pedido no pueden ser nulos.");
        }

        if (existeVentaPorPedidoId(venta.getPedidoId())) {
            throw new IllegalStateException("⚠️ El pedido ya tiene una venta registrada.");
        }

        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
        validarPedido(pedido);

        // 1️⃣ Obtener promociones completas por ID
        List<Promocion> promocionesSolicitadas = Optional.ofNullable(venta.getPromociones()).orElse(List.of());

        List<Promocion> promocionesCompletas = promocionesSolicitadas.stream()
                .map(p -> promocionRepository.findById(p.getId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        // 2️⃣ Analizar y validar promociones aplicables
        ResultadoAnalisisPromocionesDTO resultado = analizarPromocionesAplicables(pedido, promocionesCompletas);

        if (!resultado.getPromocionesRechazadas().isEmpty()) {
            String error = "❌ Promociones inválidas para el pedido:\n" +
                    String.join("\n", resultado.getPromocionesRechazadas());
            throw new IllegalArgumentException(error);
        }

        venta.setPromociones(resultado.getPromocionesAplicables());

        // 3️⃣ Guardar y retornar la venta procesada
        Venta guardada = procesarVenta(venta, pedido);
        return new VentaDTO(guardada, pedido);
    }

    @Override
    public VentaDTO actualizarVenta(Integer id, Venta nuevaVenta) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Venta no encontrada."));

        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());

        venta.setMetodoPago(nuevaVenta.getMetodoPago());
        venta.setFechaVenta(nuevaVenta.getFechaVenta());
        venta.setPromociones(nuevaVenta.getPromociones());

        validarLimitePromociones(nuevaVenta);
        Venta actualizada = procesarVenta(venta, pedido);

        return new VentaDTO(actualizada, pedido);
    }

    public List<Promocion> obtenerPromocionesAplicables(Integer pedidoId) {
        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(pedidoId);
        List<Promocion> promocionesDisponibles = promocionRepository.findAll(); // o desde Feign

        return promocionesDisponibles.stream()
                .filter(promo -> cumpleCondiciones(pedido, promo))
                .collect(Collectors.toList());
    }

    @Override
    public ResultadoAnalisisPromocionesDTO analizarPromocionesAplicables(PedidoDTO pedido, List<Promocion> promociones) {
        if (pedido == null) {
            System.out.println("❌ Pedido nulo recibido. No se puede analizar promociones.");
            throw new IllegalArgumentException("El pedido es nulo. No se puede analizar promociones.");
        }

        if (promociones == null || promociones.isEmpty()) {
            System.out.println("⚠️ Lista de promociones vacía. No hay nada que evaluar.");
            return new ResultadoAnalisisPromocionesDTO(List.of(), List.of("⚠️ No hay promociones registradas para evaluar."));
        }

        List<Promocion> aplicables = new ArrayList<>();
        List<String> rechazadas = new ArrayList<>();

        BigDecimal total = pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO;

        Map<Integer, Integer> resumenMenu = pedido.getDetalles() != null
                ? pedido.getDetalles().stream()
                .filter(d -> d.getMenuId() != null && d.getCantidad() != null)
                .collect(Collectors.toMap(
                        DetallePedidoDTO::getMenuId,
                        DetallePedidoDTO::getCantidad,
                        Integer::sum
                ))
                : new HashMap<>();

        System.out.println("→ Analizando " + promociones.size() + " promociones para pedido ID " + pedido.getId());

        for (Promocion promo : promociones) {
            boolean tieneCondiciones = false;
            boolean aplica = true;
            StringBuilder motivo = new StringBuilder();

            System.out.println("🔎 Evaluando promoción ID " + promo.getId() + ": " + promo.getNombre());

            // 🔸 Monto mínimo
            if (promo.getMontoMinimo() != null) {
                tieneCondiciones = true;
                if (total.compareTo(promo.getMontoMinimo()) < 0) {
                    aplica = false;
                    motivo.append("Total (S/").append(total)
                            .append(") menor al mínimo requerido (S/")
                            .append(promo.getMontoMinimo()).append("). ");
                }
            }

            // 🔸 Menú requerido
            if (promo.getMenu() != null && !promo.getMenu().isEmpty()) {
                tieneCondiciones = true;
                for (MenuPromocion req : promo.getMenu()) {
                    int compradas = resumenMenu.getOrDefault(req.getMenuId(), 0);
                    if (compradas < req.getCantidadRequerida()) {
                        aplica = false;
                        motivo.append("Se requieren ")
                                .append(req.getCantidadRequerida())
                                .append(" unidades de menú ID ")
                                .append(req.getMenuId())
                                .append(", pero solo hay ")
                                .append(compradas).append(". ");
                    }
                }
            }

            // 🎯 Resultado
            if (!tieneCondiciones) {
                aplicables.add(promo);
                System.out.println("✅ Promo ID " + promo.getId() + " no tiene condiciones. Se aplica por defecto.");
            } else if (aplica) {
                aplicables.add(promo);
                System.out.println("✅ Promo ID " + promo.getId() + " aplica correctamente.");
            } else {
                String motivoTexto = motivo.toString().trim();
                rechazadas.add("⚠️ Promo ID " + promo.getId() + ": " + motivoTexto);
                System.out.println("❌ Promo ID " + promo.getId() + " rechazada: " + motivoTexto);
            }
        }

        System.out.println("✅ Promociones aplicables: " + aplicables.stream().map(Promocion::getId).toList());
        System.out.println("❌ Promociones rechazadas: " + rechazadas.size());

        return new ResultadoAnalisisPromocionesDTO(aplicables, rechazadas);
    }

    private Venta procesarVenta(Venta venta, PedidoDTO pedido) {
        normalizarPreciosPedido(pedido);

        BigDecimal totalPedido = calcularTotalPedido(pedido.getDetalles());
        venta.setTotal(totalPedido);

        aplicarDescuentos(venta); // Aquí ya aplica sobre el total bruto

        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(OffsetDateTime.now());
        }

        if (venta.getCliente() == null && pedido.getCliente() != null) {
            venta.setCliente(pedido.getCliente().getId());
        }

        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> obtenerPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public List<Venta> listarPorCliente(Integer cliente) {
        return ventaRepository.findByCliente(cliente);
    }

//    @Override
//    public List<Venta> listarPorRangoFecha(OffsetDateTime desde, OffsetDateTime hasta) {
//        return ventaRepository.findByFechaVentaBetween(desde, hasta);
//    }

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
    public List<VentaDTO> buscarVentasPorFecha(OffsetDateTime inicio, OffsetDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin)
                .stream()
                .map(v -> new VentaDTO(v, pedidoClient.obtenerPedidoPorId(v.getPedidoId())))
                .toList();
    }

    @Override
    public List<VentaDTO> buscarPorNombreCliente(String nombreCliente) {
        // 1. Traer los pedidos del cliente
        List<PedidoDTO> pedidos = pedidoClient.buscarPedidosPorNombreCliente(nombreCliente);

        if (pedidos == null || pedidos.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Obtener los IDs de los pedidos
        List<Integer> pedidoIds = pedidos.stream()
                .map(PedidoDTO::getId)
                .filter(Objects::nonNull)
                .toList();

        // 3. Buscar las ventas cuyos pedidos coincidan
        List<Venta> ventas = ventaRepository.findByPedidoIdIn(pedidoIds);

        // 4. Envolver en DTO y devolver
        return ventas.stream()
                .map(v -> new VentaDTO(v,
                        pedidos.stream()
                                .filter(p -> p.getId().equals(v.getPedidoId()))
                                .findFirst()
                                .orElse(null)))
                .toList();
    }

    @Override
    public PedidoDTO obtenerPedidoPorId(Integer pedidoId) {
        try {
            PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(pedidoId);
            if (pedido == null) {
                throw new IllegalArgumentException("No se encontró el pedido con ID " + pedidoId);
            }

            // Enriquecer detalles del menú si están disponibles
            if (pedido.getDetalles() != null) {
                for (DetallePedidoDTO d : pedido.getDetalles()) {
                    if (d.getMenu() != null) {
                        d.setNombreMenu(d.getMenu().getNombre());
                        d.setDescripcionMenu(d.getMenu().getDescripcion());
                        d.setPrecioUnitario(d.getMenu().getPrecio());
                    }
                }
            }

            return pedido;
        } catch (Exception e) {
            throw new IllegalStateException("Error al obtener el pedido con ID " + pedidoId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Promocion> obtenerTodasLasPromociones() {
        return promocionRepository.findAll();
    }

    // Utilidades internas

    private void validarPedido(PedidoDTO pedido) {
        if (pedido == null) throw new IllegalArgumentException("Pedido no encontrado.");
        if (!"COMPLETADO".equalsIgnoreCase(pedido.getEstadoPedido())) {
            throw new IllegalStateException("El pedido debe estar COMPLETADO para registrar la venta.");
        }
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalStateException("El pedido no contiene detalles. No se puede registrar una venta vacía.");
        }
    }

    private BigDecimal calcularTotalPedido(List<DetallePedidoDTO> detalles) {
        return detalles.stream()
                .filter(d -> d.getPrecioUnitario() != null)
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void normalizarPreciosPedido(PedidoDTO pedido) {
        if (pedido == null || pedido.getDetalles() == null) {
            System.out.println("⚠️ Pedido o detalles nulos. No se puede normalizar precios.");
            return;
        }

        for (DetallePedidoDTO d : pedido.getDetalles()) {
            if (d.getPrecioUnitario() == null) {
                // 1️⃣ Recuperar desde el objeto embebido si está disponible
                if (d.getMenu() != null && d.getMenu().getPrecio() != null) {
                    d.setPrecioUnitario(d.getMenu().getPrecio());
                    System.out.println("✅ Precio asignado desde objeto menú para detalle ID " + d.getId());
                }

                // 2️⃣ Recuperar usando menuId a través de Feign
                else if (d.getMenuId() != null) {
                    try {
                        MenuDTO menu = pedidoClient.obtenerMenuPorId(d.getMenuId());
                        if (menu != null && menu.getPrecio() != null) {
                            d.setPrecioUnitario(menu.getPrecio());
                            d.setNombreMenu(menu.getNombre()); // opcional
                            d.setDescripcionMenu(menu.getDescripcion());
                            System.out.println("✅ Precio obtenido vía Feign para menú ID " + d.getMenuId());
                        } else {
                            d.setPrecioUnitario(BigDecimal.ZERO);
                            System.out.println("⚠️ Menú ID " + d.getMenuId() + " sin precio válido.");
                        }
                    } catch (Exception e) {
                        d.setPrecioUnitario(BigDecimal.ZERO);
                        System.out.println("❌ Error al consultar menú ID " + d.getMenuId() + ": " + e.getMessage());
                    }
                }

                // 3️⃣ Sin información de menú
                else {
                    d.setPrecioUnitario(BigDecimal.ZERO);
                    System.out.println("⚠️ Detalle sin menu ni menuId. Se asigna precio cero al detalle ID " + d.getId());
                }
            }
        }
    }

    private boolean cumpleCondiciones(PedidoDTO pedido, Promocion promo) {
        BigDecimal total = pedido.getTotal();
        Map<Integer, Integer> resumenMenu = pedido.getDetalles().stream()
                .collect(Collectors.toMap(
                        DetallePedidoDTO::getMenuId,
                        DetallePedidoDTO::getCantidad,
                        Integer::sum
                ));

        if (promo.getMontoMinimo() != null && total.compareTo(promo.getMontoMinimo()) < 0)
            return false;

        if (promo.getMenu() != null && !promo.getMenu().isEmpty()) {
            for (MenuPromocion req : promo.getMenu()) {
                int cantidad = resumenMenu.getOrDefault(req.getMenuId(), 0);
                if (cantidad < req.getCantidadRequerida()) return false;
            }
        }

        return true;
    }

    private void validarLimitePromociones(Venta venta) {
        int max = venta.getMaximoPromocionesPermitidas() != null
                ? venta.getMaximoPromocionesPermitidas()
                : 1;

        int actual = (venta.getPromociones() != null) ? venta.getPromociones().size() : 0;

        if (actual > max) {
            throw new IllegalArgumentException("Se excedió el máximo de promociones permitidas (" + max + "). Proporcionadas: " + actual);
        }
    }

    private void aplicarDescuentos(Venta venta) {
        List<Promocion> promociones = venta.getPromociones();
        if (promociones == null || promociones.isEmpty()) return;

        BigDecimal totalOriginal = venta.getTotal();
        BigDecimal totalDescuento = BigDecimal.ZERO;
        LocalDate hoy = LocalDate.now();

        for (Promocion promo : promociones) {
            if (promo.getValorDescuento() == null) continue;
            if (promo.getFechaInicio() != null && promo.getFechaFin() != null) {
                if (hoy.isBefore(promo.getFechaInicio()) || hoy.isAfter(promo.getFechaFin())) continue;
            }

            BigDecimal descuento = promo.getTipoDescuento() == Promocion.TipoDescuento.PORCENTAJE
                    ? totalOriginal.multiply(promo.getValorDescuento()).divide(BigDecimal.valueOf(100))
                    : promo.getValorDescuento();

            totalDescuento = totalDescuento.add(descuento);
        }

        venta.setTotal(totalDescuento.compareTo(totalOriginal) >= 0
                ? BigDecimal.ZERO
                : totalOriginal.subtract(totalDescuento));
    }
}
