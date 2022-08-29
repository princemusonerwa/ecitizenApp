package com.minaloc.gov.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.minaloc.gov.domain.Complain;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.slf4j.*;

public class GenerateComplainReport {

    public static final Logger logger = LoggerFactory.getLogger(GenerateComplainReport.class);

    public static ByteArrayInputStream complainsReport(List<Complain> complains) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 1, 5, 5 });

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Ikibazo", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Icyakozwe", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Icyakorwa", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Umwanzuro", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Complain complain : complains) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(complain.getId().toString()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(complain.getIkibazo()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(complain.getIcyakozwe()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPaddingLeft(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(complain.getIcyakorwa()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(complain.getUmwanzuro()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();
        } catch (DocumentException ex) {
            logger.error("Error occurred: {0}", ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
