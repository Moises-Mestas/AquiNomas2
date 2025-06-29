package com.upeu.servicioreporte.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PlatosBebidasMasMenosPedidosPdf {

    public static File exportar(List<Map<String, Object>> masPedidos, List<Map<String, Object>> menosPedidos) throws IOException, DocumentException {
        Document document = new Document();
        File pdfFile = File.createTempFile("reporte_platos_bebidas", ".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font tableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph title = new Paragraph("Reporte: Platos y Bebidas Más y Menos Pedidos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Sección: Más pedidos
        Paragraph sub1 = new Paragraph("Top 5 Más Pedidos", titleFont);
        sub1.setSpacingBefore(10);
        sub1.setSpacingAfter(10);
        document.add(sub1);

        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.setWidths(new int[]{2, 1});

        addTableHeader(table1, tableHeader, "Nombre del Plato o Bebida", "Cantidad Pedida");

        for (Map<String, Object> item : masPedidos) {
            table1.addCell(new Phrase(String.valueOf(item.get("nombre")), cellFont));
            table1.addCell(new Phrase(String.valueOf(item.get("cantidad")), cellFont));
        }

        document.add(table1);

        // Sección: Menos pedidos
        Paragraph sub2 = new Paragraph("Top 5 Menos Pedidos", titleFont);
        sub2.setSpacingBefore(20);
        sub2.setSpacingAfter(10);
        document.add(sub2);

        PdfPTable table2 = new PdfPTable(2);
        table2.setWidthPercentage(100);
        table2.setWidths(new int[]{2, 1});

        addTableHeader(table2, tableHeader, "Nombre del Plato o Bebida", "Cantidad Pedida");

        for (Map<String, Object> item : menosPedidos) {
            table2.addCell(new Phrase(String.valueOf(item.get("nombre")), cellFont));
            table2.addCell(new Phrase(String.valueOf(item.get("cantidad")), cellFont));
        }

        document.add(table2);
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
