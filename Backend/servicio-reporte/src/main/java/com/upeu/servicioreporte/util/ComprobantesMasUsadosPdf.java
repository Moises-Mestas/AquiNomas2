package com.upeu.servicioreporte.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ComprobantesMasUsadosPdf {

    public static File exportar(List<Map<String, Object>> datos) throws IOException, DocumentException {
        Document document = new Document();
        File pdfFile = File.createTempFile("reporte_comprobantes", ".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        // Estilos
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font tableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Título
        Paragraph title = new Paragraph("Reporte: Comprobantes Más Usados", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Tabla
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 1});

        addTableHeader(table, tableHeader, "Tipo de Comprobante", "Cantidad de Usos");

        for (Map<String, Object> item : datos) {
            table.addCell(new Phrase(String.valueOf(item.get("comprobante")), cellFont));
            table.addCell(new Phrase(String.valueOf(item.get("cantidad")), cellFont));
        }

        document.add(table);
        document.close();

        return pdfFile;
    }

    private static void addTableHeader(PdfPTable table, Font headerFont, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);
        }
    }
}
