package com.example.servicioventa.service.Impl;

import com.example.servicioventa.dto.DetallePedidoDTO;
import com.example.servicioventa.dto.MenuDTO;
import com.example.servicioventa.dto.PedidoDTO;
import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.feing.PedidoClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PdfGeneratorService {
//
//    private static final String BASE_PATH = System.getProperty("user.dir") + "/comprobantes/";
//    private final PedidoClient pedidoClient;
//
//    public PdfGeneratorService(@Qualifier("com.example.servicioventa.feing.PedidoClient") PedidoClient pedidoClient) {
//        this.pedidoClient = pedidoClient;
//    }
//
//    public byte[] generarPdfYGuardar(ComprobantePago comprobante, String nombreCliente, PedidoDTO pedido) {
//        String nombreArchivo = comprobante.getTipo() + "_" + comprobante.getNumeroComprobante() + ".pdf";
//        String rutaCompleta = BASE_PATH + nombreArchivo;
//
//        File carpeta = new File(BASE_PATH);
//        if (!carpeta.exists()) carpeta.mkdirs();
//
//        try (PDDocument document = new PDDocument()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            try (PDPageContentStream cs = new PDPageContentStream(document, page)) {
//                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
//                cs.setLeading(14.5f);
//                cs.beginText();
//                cs.newLineAtOffset(60, 700);
//
//                escribirContenido(cs, comprobante, nombreCliente, pedido);
//
//                cs.endText();
//            }
//
//            // Guardar en disco
//            document.save(rutaCompleta);
//            File archivo = new File(rutaCompleta);
//            if (archivo.exists()) {
//                System.out.println("\n✅ PDF guardado exitosamente:");
//                System.out.println("📁 Ruta absoluta: " + archivo.getAbsolutePath());
//                System.out.println("📄 Nombre del archivo: " + archivo.getName());
//            } else {
//                System.err.println("❌ PDF no se guardó correctamente.");
//            }
//
//            // Devolver como bytes para la API
//            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//                document.save(baos);
//                return baos.toByteArray();
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException("❌ Error al generar el PDF: " + e.getMessage(), e);
//        }
//    }
//
//    private void escribirContenido(PDPageContentStream cs, ComprobantePago comprobante, String nombreCliente, PedidoDTO pedido) throws IOException {
//        escribirLinea(cs, "=== COMPROBANTE DE PAGO ===");
//        escribirLinea(cs, "Tipo: " + safe(comprobante.getTipo()));
//        escribirLinea(cs, "Serie: " + safe(comprobante.getNumeroSerie()));
//        escribirLinea(cs, "Número: " + safe(comprobante.getNumeroComprobante()));
//        escribirLinea(cs, "Fecha Emisión: " + safe(comprobante.getFechaEmision()));
//        escribirLinea(cs, "");
//
//        escribirLinea(cs, "Cliente: " + safe(nombreCliente));
//        escribirLinea(cs, "Monto Neto: S/ " + comprobante.getMontoNeto());
//        escribirLinea(cs, "IGV: S/ " + comprobante.getIgv());
//        escribirLinea(cs, "Total Venta: S/ " + comprobante.getVenta().getTotal());
//        escribirLinea(cs, "");
//
//        if (comprobante.getTipo() == ComprobantePago.TipoComprobante.FACTURA) {
//            escribirLinea(cs, "Razón Social: " + safe(comprobante.getRazonSocial()));
//            escribirLinea(cs, "Dirección Fiscal: " + safe(comprobante.getDireccionFiscal()));
//            escribirLinea(cs, "");
//        }
//
//        escribirLinea(cs, "🧾 Detalle del Pedido:");
//        for (DetallePedidoDTO d : Optional.ofNullable(pedido.getDetalles()).orElse(List.of())) {
//            String linea = "- " + d.getCantidad() + " x " +
//                    Optional.ofNullable(d.getMenu()).map(MenuDTO::getNombre).orElse("Producto") +
//                    " @ S/ " + d.getPrecioUnitario() +
//                    " = S/ " + d.getSubtotal();
//            escribirLinea(cs, linea);
//        }
//    }
//
//    private void escribirLinea(PDPageContentStream cs, String texto) throws IOException {
//        cs.showText(texto != null ? texto.replaceAll("[\\n\\r]", "") : "");
//        cs.newLine();
//    }
//
//    private String safe(Object valor) {
//        return valor != null ? valor.toString() : "";
//    }

}
