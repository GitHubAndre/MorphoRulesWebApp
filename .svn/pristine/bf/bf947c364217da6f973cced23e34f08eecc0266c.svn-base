/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.plotino.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import it.cnr.ilc.plotino.model.OntoResult;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

/**
 *
 * @author andrea
 */
public class PDFCreator {

    private Document document;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    private static Font normalBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    private static Font HeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
            Font.NORMAL, BaseColor.BLACK);
    private static Font HeaerTableFont = new Font(Font.FontFamily.TIMES_ROMAN,
            14, Font.NORMAL, BaseColor.BLACK);
    private static Font smallText = new Font(Font.FontFamily.TIMES_ROMAN, 11);

    public PDFCreator(int orientation) {
        if (orientation == 1) {
            document = new Document(PageSize.A4.rotate());
        } else {
            document = new Document(PageSize.A4);
        }
    }

    public Document getDocument() {
        return document;
    }

    public void openDocument() {
        document.open();
    }

    public void closeDocument() {
        document.close();
    }

    public void addMetaData() {
        document.addTitle("Lessico di Plotino");
        document.addSubject("Lessico di Plotino");
        document.addKeywords("Plotino");
        document.addAuthor("Institute for Computational Linguistics");
        document.addCreator("Institute for Computational Linguistics");
    }

    public void addFooterPage() throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generato da "
                + System.getProperty("user.name") + ", " + new Date(),
                smallBold));
        document.add(preface);
        document.newPage();
    }

    public void addQuestion(String entry) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(entry, normalBold));
        addEmptyLine(preface, 1);
        document.add(preface);
    }

    public void addContent() throws DocumentException {
        Chunk chunk = new Chunk("Lessico di Plotino",
                HeaderFont);
        chunk.setBackground(new BaseColor(210, 210, 210));
        document.add(chunk);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void ontoResultTable(java.util.List<OntoResult> res)
            throws DocumentException, MalformedURLException, IOException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell(new Paragraph("Proprietà", HeaerTableFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(239, 239, 239));
        cell.setPadding(10.0f);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Valore",
                HeaerTableFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(239, 239, 239));
        cell.setPadding(10.0f);
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Inferita",
                HeaerTableFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(239, 239, 239));
        cell.setPadding(10.0f);
        table.addCell(cell);
        for (int i = 0; i < res.size(); i++) {
            table.addCell(res.get(i).getRelazione());
            table.addCell(res.get(i).getTermine_target());
            table.addCell(res.get(i).getInferita());
        }
        document.add(table);

    }

}
