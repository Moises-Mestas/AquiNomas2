package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.ClienteDTO;
import com.example.servicioventa.dto.DetallePedidoDTO;
import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.feing.PedidoClient;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.ComprobantePagoService;

import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private final PedidoClient pedidoClient;

    public ComprobanteServiceImpl(ComprobantePagoRepository comprobanteRepository, VentaRepository ventaRepository, @Qualifier("com.example.servicioventa.feing.PedidoClient") PedidoClient pedidoClient) {
        this.comprobanteRepository = comprobanteRepository;
        this.ventaRepository = ventaRepository;
        this.pedidoClient = pedidoClient;
    }

    @Transactional
    @Override
    public ComprobantePago guardarComprobante(Integer ventaId, ComprobantePago.TipoComprobante tipo) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("❌ La venta con ID " + ventaId + " no existe."));

        PedidoDTO pedido = pedidoClient .obtenerPedidoPorId(venta.getPedidoId());
        ClienteDTO cliente = pedido.getCliente();

        // 🔒 Validación para factura
        if (tipo == ComprobantePago.TipoComprobante.FACTURA) {
            if (cliente == null || cliente.getRuc() == null || cliente.getRuc().isBlank()) {
                throw new IllegalArgumentException("⚠️ No se puede emitir una FACTURA sin RUC válido del cliente.");
            }
        }

        BigDecimal montoNeto = venta.getTotal().divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
        BigDecimal igv = venta.getTotal().subtract(montoNeto);

        ComprobantePago comprobante = new ComprobantePago();
        comprobante.setVenta(venta);
        comprobante.setTipo(tipo);
        comprobante.setNumeroSerie(tipo == ComprobantePago.TipoComprobante.FACTURA ? "F001" : "B001");
        comprobante.setNumeroComprobante(UUID.randomUUID().toString().substring(0, 8));
        comprobante.setFechaEmision(LocalDateTime.now());
        comprobante.setMontoNeto(montoNeto);
        comprobante.setIgv(igv);

        // 🧾 Solo si es FACTURA, se registran datos fiscales
        if (tipo == ComprobantePago.TipoComprobante.FACTURA) {
            comprobante.setRazonSocial(cliente.getNombre());
            comprobante.setDireccionFiscal(cliente.getDireccion());
        }
        return comprobanteRepository.save(comprobante);
    }

    @Override
    public byte[] generarComprobantePDF(Long comprobanteId) throws IOException {
        ComprobantePago comprobante = comprobanteRepository.findById(comprobanteId)
                .orElseThrow(() -> new RuntimeException("❌ El comprobante con ID " + comprobanteId + " no existe."));

        Venta venta = comprobante.getVenta();
        PedidoDTO pedido = pedidoClient.obtenerPedidoPorId(venta.getPedidoId());
        ClienteDTO cliente = pedido.getCliente();

        String nombreCliente = (cliente != null)
                ? cliente.getNombre() + " " + cliente.getApellido()
                : "Cliente desconocido";
        String nombreArchivo = comprobante.getTipo() + "_" + comprobante.getNumeroComprobante() + ".pdf";
        String rutaCarpeta = System.getProperty("user.dir") + "/comprobantes/";
        String rutaCompleta = rutaCarpeta + nombreArchivo;

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists() && carpeta.mkdirs()) {
            System.out.println("📁 Carpeta creada en: " + carpeta.getAbsolutePath());
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                cs.setLeading(14.5f);

                boolean textoIniciado = false;
                try {
                    cs.beginText();
                    textoIniciado = true;
                    cs.newLineAtOffset(60, 740);

                    // 🏢 Encabezado de la empresa
                    escribir(cs, "AQUÍNOMÁS");
                    escribir(cs, "RUC: 20481234567");
                    escribir(cs, "Av. Principal 123, Juliaca - Puno");
                    escribir(cs, "Telf: (051) 321-0000");
                    escribir(cs, "----------------------------------------------");

                    // 📄 Datos del comprobante
                    escribir(cs, "COMPROBANTE DE PAGO - " + comprobante.getTipo());
                    escribir(cs, "N°: " + comprobante.getNumeroSerie() + "-" + comprobante.getNumeroComprobante());
                    escribir(cs, "Fecha de Emisión: " + comprobante.getFechaEmision());
                    escribir(cs, "----------------------------------------------");

                    // 👤 Cliente
                    escribir(cs, "Cliente: " + safe(nombreCliente));
                    if (cliente != null && comprobante.getTipo() == ComprobantePago.TipoComprobante.FACTURA) {
                        escribir(cs, "RUC/DNI: " + Optional.ofNullable(cliente.getRuc()).orElse("-"));
                        escribir(cs, "Dirección: " + Optional.ofNullable(cliente.getDireccion()).orElse("-"));
                    }
                    escribir(cs, "----------------------------------------------");

                    // 🛒 Detalle del pedido
                    escribir(cs, String.format("%-5s %-18s %9s %9s", "Cant", "Producto", "P.Unit", "Subtotal"));
                    escribir(cs, "----------------------------------------------");

                    for (DetallePedidoDTO d : Optional.ofNullable(pedido.getDetalles()).orElse(List.of())) {
                        if (d.getPrecioUnitario() == null && d.getMenu() != null) {
                            d.setPrecioUnitario(d.getMenu().getPrecio());
                        }

                        BigDecimal unitario = Optional.ofNullable(d.getPrecioUnitario()).orElse(BigDecimal.ZERO);
                        BigDecimal subtotal = d.getSubtotal();
                        String nombreProducto = Optional.ofNullable(d.getMenu()).map(MenuDTO::getNombre).orElse("Producto");

                        escribir(cs, String.format("%-5d %-18s S/%7.2f S/%8.2f",
                                d.getCantidad(),
                                recortar(nombreProducto, 18),
                                unitario,
                                subtotal));
                    }

                    // 💰 Montos finales
                    escribir(cs, "----------------------------------------------");
                    escribir(cs, String.format("%-28s S/%7.2f", "Monto Neto:", comprobante.getMontoNeto()));
                    escribir(cs, String.format("%-28s S/%7.2f", "IGV (18%):", comprobante.getIgv()));
                    escribir(cs, String.format("%-28s S/%7.2f", "TOTAL A PAGAR:", venta.getTotal()));
                    escribir(cs, "----------------------------------------------");
                    escribir(cs, "");
                    escribir(cs, "_____________________________");
                    escribir(cs, "Firma del cliente: " + safe(nombreCliente));

                } catch (Exception e) {
                    System.err.println("⚠️ Error durante la escritura del comprobante: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (textoIniciado) {
                        try {
                            cs.endText();
                        } catch (IOException e) {
                            System.err.println("⚠️ Error al cerrar el bloque de texto: " + e.getMessage());
                        }
                    }
                }
            }

            document.save(rutaCompleta);
            System.out.println("✅ PDF guardado en: " + new File(rutaCompleta).getAbsolutePath());

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    private void escribir(PDPageContentStream cs, String texto) throws IOException {
        cs.showText(texto != null ? texto.replaceAll("[\\n\\r]", "") : "");
        cs.newLine();
    }

    private String safe(Object valor) {
        return valor != null ? valor.toString() : "";
    }

    private String recortar(String texto, int max) {
        return texto.length() > max ? texto.substring(0, max - 1) + "…" : texto;
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
