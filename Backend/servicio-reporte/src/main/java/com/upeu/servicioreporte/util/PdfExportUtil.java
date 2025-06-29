package com.upeu.servicioreporte.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PdfExportUtil {

    public static ByteArrayInputStream exportarProductosMasRentables(List<Map<String, Object>> productos) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Reporte: Productos Más Rentables", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2f, 4f, 2f, 3f});

            // Encabezados
            String[] headers = {"ID", "Nombre", "Precio Unitario", "Total Generado"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(headerCell);
            }

            // Datos
            for (Map<String, Object> producto : productos) {
                tabla.addCell(producto.get("menuId").toString());
                tabla.addCell(producto.get("nombre").toString());
                tabla.addCell(producto.get("precioUnitario").toString());
                tabla.addCell(producto.get("totalGenerado").toString());
            }

            document.add(tabla);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
