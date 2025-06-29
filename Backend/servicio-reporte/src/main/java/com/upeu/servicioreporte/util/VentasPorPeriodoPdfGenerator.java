package com.upeu.servicioreporte.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class VentasPorPeriodoPdfGenerator {

    public static void generarPdf(Map<String, Object> datos, OutputStream outputStream) throws Exception {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, outputStream);
        documento.open();

        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
        Font textoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);

        Paragraph titulo = new Paragraph("Reporte de Ventas por Periodo", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10);

        tabla.addCell(getCelda("Fecha de Inicio:", negrita));
        tabla.addCell(getCelda(datos.get("fechaInicio").toString(), textoFont));

        tabla.addCell(getCelda("Fecha de Fin:", negrita));
        tabla.addCell(getCelda(datos.get("fechaFin").toString(), textoFont));

        tabla.addCell(getCelda("Cantidad de Ventas:", negrita));
        tabla.addCell(getCelda(datos.get("cantidadVentas").toString(), textoFont));

        tabla.addCell(getCelda("Monto Total:", negrita));
        tabla.addCell(getCelda(datos.get("montoTotal").toString(), textoFont));

        documento.add(tabla);
        documento.close();
    }

    private static PdfPCell getCelda(String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setPadding(8);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setBorderColor(BaseColor.LIGHT_GRAY);
        return celda;
    }
}
