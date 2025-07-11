package com.example.servicioventa.service;


import com.example.servicioventa.entity.ComprobantePago;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ComprobantePagoService {
    List<ComprobantePago> listar();
    ComprobantePago actualizar(ComprobantePago comprobantePago);
    Optional<ComprobantePago> obtenerPorId(Long id);
    void eliminarPorId(Long id);
    @Transactional
    ComprobantePago guardarComprobante(Integer ventaId, ComprobantePago.TipoComprobante tipo);

    byte[] generarComprobantePDF(Long comprobanteId) throws IOException;

    List<ComprobantePago> filtrarComprobantes(String tipo, LocalDateTime inicio, LocalDateTime fin);

    List<ComprobantePago> listarPorVenta(Integer ventaId);
}