package com.upeu.servicioreporte.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.upeu.servicioreporte.dto.ReporteGeneralDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ListaReportesPdf {

    public static File exportar(List<ReporteGeneralDto> reportes) throws IOException, DocumentException {
        Document document = new Document();
        File pdfFile = File.createTempFile("lista_reportes", ".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        Paragraph title = new Paragraph("Reporte General de Registros", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f, 2f, 2f, 3f, 4f, 3f});

        addHeader(table, headerFont, "ID", "Cliente", "Admin", "Tipo", "Descripción", "Detalles", "Fecha");

        for (ReporteGeneralDto dto : reportes) {
            table.addCell(new Phrase(String.valueOf(dto.getId()), cellFont));
            table.addCell(new Phrase(dto.getCliente(), cellFont));
            table.addCell(new Phrase(dto.getAdministrador(), cellFont));
            table.addCell(new Phrase(dto.getTipo(), cellFont));
            table.addCell(new Phrase(dto.getDescripcion(), cellFont));
            table.addCell(new Phrase(dto.getDetalles(), cellFont));
            table.addCell(new Phrase(dto.getFechaCreacion(), cellFont));
        }

        document.add(table);
        document.close();
        return pdfFile;
    }

    private static void addHeader(PdfPTable table, Font font, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);
        }
    }
}
