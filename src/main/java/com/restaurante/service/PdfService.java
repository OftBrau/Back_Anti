package com.restaurante.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.restaurante.dto.ComprobanteResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final float PAGE_W = 226f;

    public byte[] generarComprobante(ComprobanteResponse c, String tipo) {
        boolean esCopia = "copia".equals(tipo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(new Rectangle(PAGE_W, 600f), 8, 8, 8, 8);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, baos);
            doc.open();

            Font fTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font fBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7);
            Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 7);
            Font fSmall = FontFactory.getFont(FontFactory.HELVETICA, 6);
            Font fBig = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fMarca = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

            addCentered(doc, "ANTICUCHERIA", fBig);

            String compLabel = c.getComprobante() != null ? c.getComprobante().toUpperCase() : "BOLETA";
            addCentered(doc, compLabel, fTitle);

            String marca = esCopia ? "*** C O P I A ***" : "*** O F I C I A L ***";
            addCentered(doc, marca, fMarca);

            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Venta #" + c.getVentaId(), fNormal));
            if (c.getMesaId() != null) {
                doc.add(new Paragraph("Mesa: " + c.getMesaId(), fNormal));
            }
            doc.add(new Paragraph("Fecha: " + formatFecha(c.getFecha()), fNormal));
            doc.add(new Paragraph("Metodo: " + c.getMetodoPago(), fNormal));

            doc.add(Chunk.NEWLINE);
            addLine(doc, fSmall);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 0.7f, 1.2f, 1.2f});

            table.addCell(hdrCell("Producto", fSmall));
            table.addCell(hdrCell("Cant", fSmall));
            table.addCell(hdrCell("P.U.", fSmall));
            table.addCell(hdrCellRight("Sub.", fSmall));

            for (ComprobanteResponse.ItemComprobante item : c.getItems()) {
                table.addCell(normCell(abrev(item.getProducto(), 16), fNormal));
                table.addCell(normCell(String.valueOf(item.getCantidad()), fNormal));
                table.addCell(normCell(String.format("%.2f", item.getPrecioUnitario()), fNormal));
                table.addCell(normCellRight(String.format("%.2f", item.getSubtotal()), fNormal));
            }
            doc.add(table);

            addLine(doc, fSmall);

            PdfPTable tot = new PdfPTable(2);
            tot.setWidthPercentage(100);
            tot.setWidths(new float[]{1f, 1.2f});
            tot.addCell(normCell("TOTAL", fBold));
            tot.addCell(normCellRight(String.format("S/ %.2f", c.getTotal()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));

            if (c.getMontoRecibido() != null) {
                tot.addCell(normCell("Recibido", fNormal));
                tot.addCell(normCellRight(String.format("S/ %.2f", c.getMontoRecibido()), fNormal));
            }
            if (c.getVuelto() != null && c.getVuelto() > 0) {
                tot.addCell(normCell("Vuelto", fNormal));
                tot.addCell(normCellRight(String.format("S/ %.2f", c.getVuelto()), fNormal));
            }
            doc.add(tot);

            doc.add(Chunk.NEWLINE);
            addCentered(doc, "Gracias por su compra", fSmall);

            doc.close();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
        return baos.toByteArray();
    }

    private void addCentered(Document doc, String text, Font f) throws DocumentException {
        Paragraph p = new Paragraph(text, f);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
    }

    private void addLine(Document doc, Font f) throws DocumentException {
        Paragraph p = new Paragraph("--------------------------------", f);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);
    }

    private PdfPCell hdrCell(String text, Font f) {
        PdfPCell cell = new PdfPCell(new Phrase(text, f));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setPaddingBottom(2);
        return cell;
    }

    private PdfPCell hdrCellRight(String text, Font f) {
        PdfPCell cell = hdrCell(text, f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell normCell(String text, Font f) {
        PdfPCell cell = new PdfPCell(new Phrase(text, f));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(1);
        return cell;
    }

    private PdfPCell normCellRight(String text, Font f) {
        PdfPCell cell = normCell(text, f);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private String abrev(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 1) + "." : s;
    }

    private String formatFecha(LocalDateTime fecha) {
        if (fecha == null) return "";
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
