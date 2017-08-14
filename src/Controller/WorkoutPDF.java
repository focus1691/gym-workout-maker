package Controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import Model.Exercise;
import Model.Workout;

public class WorkoutPDF {
	
	private final Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.WHITE);
	private final Font groupFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
	private final Font exerciseFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);
	private final Font repsFont = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, new BaseColor(92, 92, 91));
	private final Font siteFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.UNDERLINE, new BaseColor(91, 151, 211));
	private final Font noteFont = new Font(Font.FontFamily.COURIER, 7, Font.NORMAL, new BaseColor(168, 167, 166));
	private final Font noteBoldFont = new Font(Font.FontFamily.COURIER, 7, Font.BOLD, new BaseColor(168, 167, 166));

	private final BaseColor grey = new BaseColor(241, 240, 238);
	private final BaseColor darkGrey = new BaseColor(175, 175, 175);

	private final BaseColor lightBlue = new BaseColor(0, 174, 239);

	private final int colHeight = 30;

	private ArrayList<Workout> workouts;

	public WorkoutPDF(ArrayList<Workout> workouts, String src, String dest) {
		this.workouts = workouts;
		try {
			File fileSrc = new File(src);
	        File fileDest = new File(dest);
	        fileDest.getParentFile().mkdirs();
			Document document = new Document(PageSize.A5);
			PdfWriter.getInstance(document, new FileOutputStream(dest));
			document.open();
			addMetaData(document);
			//createContents(document);
			createTable(document);
			document.close();
			insertPageNumber(dest, src);
			fileDest.delete();
			fileDest.getParentFile().delete();
			Desktop.getDesktop().open(fileSrc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void insertPageNumber(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte pagecontent;
        for (int i = 0; i < n; ) {
            pagecontent = stamper.getOverContent(++i);
            ColumnText.showTextAligned(pagecontent, Element.ALIGN_CENTER,
                    new Phrase(String.format("Page %s of %s", i, n)), 200, 25, 0);
        }
        stamper.close();
        reader.close();
    }

	private void addMetaData(Document document) {
		document.addTitle("Workout Builder");
		document.addSubject("Workout Schedule");
		document.addKeywords("Exercise, Fitness, Workout");
		document.addAuthor("Joshua");
		document.addCreator("Joe");
	}

	public PdfPCell getCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}
	
	private void createTable(Document document) throws DocumentException {
		
		int i, j, k, multisetIdx;

		for (i = 0; i < workouts.size(); i++) { // loop through the workouts
			
			Workout workout = workouts.get(i);

			for (j = 0; j < workout.getExercises().size(); j++) { // loop through the exercises
				
				int num = 1;
				
				PdfPTable table = new PdfPTable(new float[] { 2, 9, 4 }); // Init the table with 2 rows
				
				table.setWidthPercentage(100);
				
				Exercise exercise = workout.getExercises().get(j);
				int day = exercise.getNumber();
				int week = workout.getWeek();
				
				document.add(header(week, day, exercise.getTitle())); // Add the header to the new page
				
				if (workout.getExercises().get(j).getExercises().size() >= 1) { // Check if this exercise has exercises (superset/giant set)
					table.addCell(titleCell(workout.getExercises().get(j).getExercises().size())); // Add a title for superset/giant set
				}
				
				table.addCell(numberCell(String.valueOf(num++))); // Add the number (blue circle)
				table.addCell(setCell(workout.getExercises().get(j), workout.getExercises().get(j).getVideoLink())); // Write the number of sets to the right of the number
				
				if (!workout.getExercises().get(j).getVideoLink().equals("") && workout.getExercises().get(j).getVideoLink() != null) {
					table.addCell(websiteCell(workout.getExercises().get(j).getVideoLink()));
				} else {
					table.addCell(emptyCell());
				}
				
				for (multisetIdx = 0; multisetIdx < workout.getExercises().get(j).getExercises().size(); multisetIdx++) { // loop through superset/giant set exercises for this exercise
					table.addCell(emptyCell()); // Add an empty cell to get the same spacing on this superset/giant set exercise
					table.addCell(childSetCell(workout.getExercises().get(j).getExercises().get(multisetIdx)));// Write the superset/giant set exercises below the set
					
					if (!workout.getExercises().get(j).getExercises().get(multisetIdx).getVideoLink().equals("") &&
							workout.getExercises().get(j).getExercises().get(multisetIdx).getVideoLink() != null) {
						table.addCell(websiteChildCell(workout.getExercises().get(j).getExercises().get(multisetIdx).getVideoLink()));
					} else {
						table.addCell(emptyCell());
					}
				}
				
				table.addCell(noteCell(workout.getExercises().get(j)));
				table.addCell(borderCell());
				
				for (k = j + 1; k < workout.getExercises().size(); k++) {
					
					int nextDay = workouts.get(i).getExercises().get(k).getNumber();
					
					if (day == nextDay) {
					
						if (workout.getExercises().get(k).getExercises().size() >= 1) { // Check if this exercise has exercises (superset/giant set)
							table.addCell(titleCell(workout.getExercises().get(k).getExercises().size())); // Add a title for superset/giant set
						}
						
						table.addCell(numberCell(String.valueOf(num++))); // Add the number (blue circle)
						table.addCell(setCell(workouts.get(i).getExercises().get(k), workout.getExercises().get(k).getVideoLink())); // Write the number of sets to the right of the number
		
						if (!workout.getExercises().get(k).getVideoLink().equals("") && workout.getExercises().get(k).getVideoLink() != null) {
							table.addCell(websiteChildCell(workout.getExercises().get(k).getVideoLink()));
						} else {
							table.addCell(emptyCell());
						}
						
						for (multisetIdx = 0; multisetIdx < workouts.get(i).getExercises().get(k).getExercises().size(); multisetIdx++) { // loop through superset/giant set exercises for this exercise
							table.addCell(emptyCell()); // Add an empty cell to get the same spacing on this superset/giant set exercise
							table.addCell(childSetCell(workout.getExercises().get(k).getExercises().get(multisetIdx)));// Write the superset/giant set exercises below the set
							
							if (!workout.getExercises().get(k).getExercises().get(multisetIdx).getVideoLink().equals("") && workout.getExercises().get(multisetIdx).getVideoLink() != null) {
								table.addCell(websiteChildCell(workout.getExercises().get(k).getExercises().get(multisetIdx).getVideoLink()));
							} else {
								table.addCell(emptyCell());
							}
						}
						table.addCell(noteCell(workout.getExercises().get(k)));
						table.addCell(borderCell());
						
						j = k;
					}
				}
				num = 1;
				document.add(table);
				document.newPage();
			} // end of exercises for this workout
		} // end of workouts
	}

	public PdfPTable header(int week, int day, String bodyPart) {
		PdfPTable table = new PdfPTable(new float[] { 8, 1, 1 });
		table.setWidthPercentage(100);

		table.addCell(headerCell(week, day, bodyPart));
		table.addCell(logoCell());
		
		return table;
	}
	
	public PdfPTable header(String title) {
		PdfPTable table = new PdfPTable(new float[] { 8, 1, 1 });
		table.setWidthPercentage(100);

		table.addCell(headerCell(title));
		table.addCell(logoCell());
		
		return table;
	}
	
	public PdfPCell headerCell(int week, int day, String bodyPart) {
		PdfPCell headerCell = new PdfPCell(new Phrase(
				("Week " + String.valueOf(week) + "/day " + String.valueOf(day + ": " + bodyPart))
						.toUpperCase(),
				headerFont));
		headerCell.setColspan(2);
		headerCell.setBackgroundColor(lightBlue);
		headerCell.setPaddingLeft(5);
		alignLeft(headerCell);
		setColHeight(headerCell);
		headerCell.setBorder(PdfPCell.NO_BORDER);
		
		return headerCell;
	}
	
	public PdfPCell headerCell(String title) {
		PdfPCell headerCell = new PdfPCell(new Phrase(title.toUpperCase(), headerFont));
		headerCell.setColspan(2);
		headerCell.setBackgroundColor(lightBlue);
		headerCell.setPaddingLeft(5);
		alignLeft(headerCell);
		setColHeight(headerCell);
		headerCell.setBorder(PdfPCell.NO_BORDER);
		
		return headerCell;
	}

	public PdfPCell logoCell() {
		PdfPCell logoCell = new PdfPCell();
		Image logo = null;

		try {
			logo = Image.getInstance("img/logoa.png");
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		logoCell = new PdfPCell(logo, true);
		logoCell.setColspan(1);

		logoCell.setPaddingRight(5);

		logoCell.setBackgroundColor(lightBlue);

		logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		alignRight(logoCell);

		removeBorder(logoCell);

		return logoCell;
	}

	public PdfPCell numberCell(String number) {

		Image img = null;
		PdfPCell numberCell;

		try {
			img = Image.getInstance("img/circ.png");
		} catch (BadElementException | IOException e) {
			e.printStackTrace();
		}
		numberCell = new PdfPCell(img, true);
		
		numberCell.setCellEvent(new WatermarkedCell(number));
		alignLeft(numberCell);
		alignCenter(numberCell);
		numberCell.setVerticalAlignment(Element.ALIGN_TOP);

		removeBorder(numberCell);
		numberCell.setPaddingTop(2);
		numberCell.setPaddingLeft(10);
		numberCell.setBackgroundColor(grey);

		return numberCell;
	}

	public PdfPCell websiteCell(String site) {

		Anchor anchor = new Anchor("Video Link", siteFont);
		anchor.setReference(site);
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(anchor);

		PdfPCell siteCell = new PdfPCell(paragraph);
		
		alignRight(siteCell);

		siteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		removeBorder(siteCell);
		siteCell.setPaddingRight(15);

		siteCell.setBackgroundColor(grey);

		return siteCell;
	}
	
	public PdfPCell websiteChildCell(String site) {

		Anchor anchor = new Anchor("Video Link", siteFont);
		anchor.setReference(site);
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(anchor);

		PdfPCell siteCell = new PdfPCell(paragraph);
		
		alignRight(siteCell);

		siteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		removeBorder(siteCell);
		siteCell.setPaddingRight(15);
		siteCell.setPaddingTop(10);

		siteCell.setBackgroundColor(grey);

		return siteCell;
	}

	public PdfPCell titleCell(int size) {
		
		String title = "SUPERSET:";
		
		if (size > 1) title = "GIANT SET:";

		PdfPCell setTitle = new PdfPCell(new Paragraph(title, groupFont));
		setTitle.setColspan(3);
		setTitle.setPaddingLeft(13);
		removeBorder(setTitle);
		setTitle.setBackgroundColor(grey);

		return setTitle;
	}

	public PdfPCell setCell(Exercise exercise, String videoLink) {
		PdfPCell setCell = new PdfPCell();
		Paragraph bodyParagraph = new Paragraph(exercise.getEquipment().toUpperCase(), groupFont);
		setCell.addElement(bodyParagraph);
		setCell.addElement(new Paragraph(exercise.getReps(), repsFont));
		
		setCell.setBackgroundColor(grey);
		setCell.setVerticalAlignment(Element.ALIGN_TOP);
		removeBorder(setCell);
		
		return setCell;
	}
	
	public PdfPCell emptyCell() {
		PdfPCell emptyCell = new PdfPCell();
		alignLeft(emptyCell);
		alignCenter(emptyCell);

		removeBorder(emptyCell);
		emptyCell.setPaddingTop(2);
		emptyCell.setPaddingLeft(10);
		emptyCell.setBackgroundColor(grey);
		emptyCell.setVerticalAlignment(Element.ALIGN_TOP);

		return emptyCell;
	}

	public PdfPCell childSetCell(Exercise exercise) {
		
		PdfPCell setCell = new PdfPCell();

		setCell.addElement(new Paragraph(exercise.getEquipment().toUpperCase(), groupFont));
		setCell.addElement(new Paragraph(exercise.getReps(), repsFont));

		setCell.setPaddingTop(10);
		
		setCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		removeBorder(setCell);
		setCell.setBackgroundColor(grey);

		return setCell;
	}

	public PdfPCell noteCell(Exercise exercise) {

		PdfPCell noteCell = new PdfPCell();
		noteCell.setColspan(2);
		noteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		if (exercise.getNote() != null && !exercise.getNote().isEmpty()) {
		
			Chunk note = new Chunk("Note: ", noteBoldFont);
			Chunk noteText = new Chunk(exercise.getNote(), noteFont);
			Phrase comb = new Paragraph();
			comb.add(note);
			comb.add(noteText);
			
			noteCell.addElement(comb);
		} else {
			noteCell.addElement(new Paragraph("\n"));
		}

		removeBorder(noteCell);
		addBorderBottom(noteCell);

		noteCell.setPaddingLeft(13);
		noteCell.setPaddingTop(0);

		noteCell.setBackgroundColor(grey);

		return noteCell;
	}
	
	public PdfPCell borderCell() {
		PdfPCell cell = new PdfPCell();
		
		cell.setColspan(2);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		removeBorder(cell);

		addBorderBottom(cell);

		cell.setPaddingLeft(13);
		cell.setPaddingBottom(5);

		cell.setBackgroundColor(grey);
		return cell;
	}

	public Image getWatermarkedImage(PdfContentByte cb, Image img, String watermark) throws DocumentException {
		float width = img.getScaledWidth();
		float height = img.getScaledHeight();
		PdfTemplate template = cb.createTemplate(width, height);
		template.addImage(img, width, 0, 0, height, 0, 0);
		ColumnText.showTextAligned(template, Element.ALIGN_CENTER, new Phrase(watermark, exerciseFont), width / 2,
				height / 2, 30);
		return Image.getInstance(template);
	}

	public void alignLeft(PdfPCell cell) {
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	public void alignRight(PdfPCell cell) {
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	public void alignCenter(PdfPCell cell) {
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}

	public void setColHeight(PdfPCell cell) {
		cell.setMinimumHeight(colHeight);
	}

	public void addBorderBottom(PdfPCell cell) {
		cell.setBorderWidthBottom(0.5f);
		cell.setUseBorderPadding(true);
		cell.setBorderColorBottom(darkGrey);
	}

	public void removeBorder(PdfPCell cell) {
		cell.setBorder(Rectangle.NO_BORDER);
	}
}