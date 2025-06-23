package com.example.servicioventa.service;


import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.dto.ResultadoAnalisisPromocionesDTO;
import com.example.servicioventa.dto.VentaDTO;
import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.entity.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface VentaService {

    VentaDTO crearVenta(Venta venta);

    VentaDTO actualizarVenta(Integer id, Venta venta);

    Optional<Venta> obtenerPorId(Integer id);

    void eliminarPorId(Integer id);

    List<Venta> listarPorCliente(Integer clienteId);

//    List<Venta> listarPorRangoFecha(OffsetDateTime desde, OffsetDateTime hasta);

    List<Venta> listarPorMetodoPago(Venta.MetodoPago metodo);

    List<Venta> listarPorPromocion(Integer promocionId);

    BigDecimal calcularTotalVentasPorCliente(Integer clienteId);

    boolean existeVentaPorPedidoId(Integer pedidoId);

    List<VentaDTO> listarVentas();

    List<VentaDTO> buscarVentasPorFecha(OffsetDateTime inicio, OffsetDateTime fin);

    List<VentaDTO> buscarPorNombreCliente(String nombreCliente);

    // Métodos adicionales (asegúrate de implementarlos en VentaServiceImpl):

    PedidoDTO obtenerPedidoPorId(Integer pedidoId);

    List<Promocion> obtenerTodasLasPromociones();

    ResultadoAnalisisPromocionesDTO analizarPromocionesAplicables(PedidoDTO pedido, List<Promocion> promociones);
}