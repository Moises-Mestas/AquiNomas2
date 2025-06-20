package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.ComprobantePagoService;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ComprobanteServiceImpl implements ComprobantePagoService {

    private final ComprobantePagoRepository comprobanteRepository;
    private final VentaRepository ventaRepository;

    public ComprobanteServiceImpl(ComprobantePagoRepository comprobanteRepository, VentaRepository ventaRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.ventaRepository = ventaRepository;
    }

    @Transactional
    @Override
    public ComprobantePago guardarComprobante(Integer ventaId, ComprobantePago.TipoComprobante tipo) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        if (ventaOpt.isEmpty()) {
            throw new RuntimeException("❌ La venta con ID " + ventaId + " no existe.");
        }

        Venta venta = ventaOpt.get();

        // ✅ Calcular montos basado en el total de la venta
        BigDecimal montoNeto = venta.getTotal().divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
        BigDecimal igv = venta.getTotal().subtract(montoNeto);

        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setVenta(venta);
        comprobante.setTipo(tipo);
        comprobante.setNumeroSerie("B001");
        comprobante.setNumeroComprobante(UUID.randomUUID().toString().substring(0, 8));
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setMontoNeto(montoNeto);
        comprobante.setIgv(igv);

        return comprobanteRepository.save(comprobante);
    }

    @Override
    public byte[] generarComprobantePDF(Long comprobanteId) throws IOException {
        Optional<ComprobantePago> comprobanteOpt = comprobanteRepository.findById(comprobanteId);
//        if (comprobanteOpt.isEmpty()) {
//            throw new RuntimeException("❌ El comprobante con ID " + comprobanteId + " no existe.");
//        }
//
//        ComprobantePago comprobante = comprobanteOpt.get();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(outputStream);
//        PdfDocument pdfDoc = new PdfDocument(writer);
//        Document document = new Document(pdfDoc);
//
//        document.add(new Paragraph("Comprobante de Venta"));
//        document.add(new Paragraph("Tipo: " + comprobante.getTipo()));
//        document.add(new Paragraph("Número Serie: " + comprobante.getNumeroSerie()));
//        document.add(new Paragraph("Número Comprobante: " + comprobante.getNumeroComprobante()));
//        document.add(new Paragraph("Fecha Emisión: " + comprobante.getFechaEmision()));
//        document.add(new Paragraph("Total Venta: " + comprobante.getVenta().getTotal()));
//        document.add(new Paragraph("Monto Neto: " + comprobante.getMontoNeto()));
//        document.add(new Paragraph("IGV: " + comprobante.getIgv()));
//
//        document.close();
//
//        // ✅ Guardar PDF en carpeta
//        String filePath = "C:/Users/NELSON/Documents/Comprobantes/comprobante_" + comprobanteId + ".pdf";
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(outputStream.toByteArray());
//        }

        return null;
    }

    @Override
    public List<ComprobantePago> listar() {
        return comprobanteRepository.findAll();
    }

    @Override
    public Optional<ComprobantePago> obtenerPorId(Long id) {
        return comprobanteRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        comprobanteRepository.deleteById(id);
    }

    @Override
    public ComprobantePago guardarComprobante(Long ventaId, ComprobantePago.TipoComprobante tipo) {
        return null;
    }

    @Override
    public ComprobantePago actualizar(ComprobantePago comprobantePago) {
        return comprobanteRepository.save(comprobantePago);
    }

    @Override
    public List<ComprobantePago> listarPorVenta(Integer ventaId) {
        Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
        return ventaOpt.map(comprobanteRepository::findByVenta).orElseGet(List::of);
    }

    @Override
    public List<ComprobantePago> filtrarComprobantes(String tipo, LocalDateTime inicio, LocalDateTime fin) {
        try {
            ComprobantePago.TipoComprobante tipoEnum = tipo != null ? ComprobantePago.TipoComprobante.valueOf(tipo.toUpperCase()) : null;

            if (tipoEnum != null) {
                return comprobanteRepository.findByTipo(tipoEnum);
            } else if (inicio != null && fin != null) {
                return comprobanteRepository.findByFechaEmisionBetween(inicio, fin);
            }
            return comprobanteRepository.findAll();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("❌ Tipo de comprobante inválido: " + tipo);
        }
    }
}
