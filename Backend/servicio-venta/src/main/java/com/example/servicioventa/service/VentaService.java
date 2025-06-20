package com.example.servicioventa.service;


import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Venta;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {

    // 🚀 Crear una nueva venta (solo si el pedido está COMPLETADO y no existe venta previa)
    VentaDTO crearVenta(Venta venta);

    // ♻️ Actualizar una venta existente
    VentaDTO actualizarVenta(Integer id, Venta venta);

    Venta guardar(Venta venta);

    // 📌 Obtener venta por ID
    Optional<Venta> obtenerPorId(Integer id);

    // 📚 Listar todas las ventas
    List<Venta> listarTodas();

    // ❌ Eliminar venta por ID
    void eliminarPorId(Integer id);

    // 🔍 Buscar ventas por cliente
    List<Venta> listarPorCliente(Integer clienteId);

    // 📅 Buscar ventas entre fechas
    List<Venta> listarPorRangoFecha(LocalDateTime desde, LocalDateTime hasta);

    // 💳 Buscar por metodo de pago
    List<Venta> listarPorMetodoPago(Venta.MetodoPago metodo);

    // 🧾 Buscar ventas por promoción
    List<Venta> listarPorPromocion(Integer promocionId);

    // 📈 Obtener total de ventas por cliente
    BigDecimal calcularTotalVentasPorCliente(Integer clienteId);

    // 🔄 Verifica si ya existe una venta para un pedido
    boolean existeVentaPorPedidoId(Integer pedidoId);

    // 📋 Listar ventas enriquecidas
    List<VentaDTO> listarVentas();

    List<VentaDTO> buscarVentasPorFecha(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> buscarPorNombreCliente(String nombreCliente);
}
