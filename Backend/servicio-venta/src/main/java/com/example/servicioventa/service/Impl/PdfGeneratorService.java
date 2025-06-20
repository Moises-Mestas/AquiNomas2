package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.ComprobantePago;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfGeneratorService {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/comprobantes/";

    public void generarPdf(ComprobantePago comprobante) {
        System.out.println("📌 Intentando generar PDF para comprobante: " + comprobante.getNumeroComprobante());

        String rutaDinamica = BASE_PATH + comprobante.getTipo() + "-" + comprobante.getNumeroComprobante() + ".pdf";
        System.out.println("📌 Ruta donde se generará el PDF: " + rutaDinamica);

        try {
            File carpetaComprobantes = new File(BASE_PATH);
            if (!carpetaComprobantes.exists()) {
                carpetaComprobantes.mkdirs();
                System.out.println("📌 Carpeta comprobantes creada correctamente.");
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.setLeading(14.5f); // ✅ Espaciado entre líneas
            contentStream.newLineAtOffset(100, 700);

            contentStream.showText("=== COMPROBANTE DE PAGO ===");
            contentStream.newLine();
            contentStream.showText("Tipo: " + comprobante.getTipo());
            contentStream.newLine();
            contentStream.showText("Número de Serie: " + comprobante.getNumeroSerie());
            contentStream.newLine();
            contentStream.showText("Número Comprobante: " + comprobante.getNumeroComprobante());
            contentStream.newLine();
            contentStream.showText("Fecha de Emisión: " + comprobante.getFechaEmision());
            contentStream.newLine();
            contentStream.showText("IGV: S/ " + comprobante.getIgv());
            contentStream.newLine();
            contentStream.showText("Monto Neto: S/ " + comprobante.getMontoNeto());
            contentStream.newLine();

            // ✅ Agregar datos fiscales solo si es una factura
            if (comprobante.getTipo() == ComprobantePago.TipoComprobante.FACTURA) {
                contentStream.showText("Razón Social: " + comprobante.getRazonSocial());
                contentStream.newLine();
                contentStream.showText("Dirección Fiscal: " + comprobante.getDireccionFiscal());
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            document.save(rutaDinamica);
            document.close();

            System.out.println("✅ PDF generado correctamente en: " + rutaDinamica);
        } catch (IOException e) {
            System.out.println("❌ Error al generar el PDF: " + e.getMessage());
            throw new RuntimeException("❌ Error al generar el PDF: " + e.getMessage());
        }
    }
}
