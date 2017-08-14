package Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

public class WatermarkedCell implements PdfPCellEvent {
	Font groupFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.WHITE);
    String watermark;
    public WatermarkedCell(String watermark) {
        this.watermark = watermark;
    }

    public void cellLayout(PdfPCell cell, Rectangle position,
        PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
        ColumnText.showTextAligned(canvas, Element.ALIGN_TOP,
            new Phrase(watermark, groupFont),
            (position.getLeft() + 7 + position.getRight()) / 2,
            (position.getBottom() - 9 + position.getTop() + 2) / 2, 0);
    }
}