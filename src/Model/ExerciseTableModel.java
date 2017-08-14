package Model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ExerciseTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -5879841935738138147L;
	private ArrayList<Exercise> exercises;
	private String[] colNames = { "Id", "Day", "title", "Group", "Exercise", "Sets and Reps", "Website", "Note" };
	private boolean[][] editableCells;

	public ExerciseTableModel() {
		exercises = new ArrayList<Exercise>();
	}

	public boolean[][] getEditableCells() {
		return editableCells;
	}

	@Override
	public boolean isCellEditable(int row, int column) { // custom
															// isCellEditable
															// function
		return this.editableCells[row][column];
	}

	public void makeCellsEditable(boolean edit) {
		int i, j;
		this.editableCells = new boolean[exercises.size()][colNames.length];

		for (i = 0; i < exercises.size(); i++) {
			for (j = 1; j < colNames.length; j++) {
				this.editableCells[i][j] = edit;
				this.fireTableCellUpdated(i, j);
			}
		}
	}

	public void addExercises(ArrayList<Exercise> exercises) {
		int i;
		for (i = 0; i < exercises.size(); i++) {
			this.exercises.add(exercises.get(i));
		}
		editableCells = new boolean[exercises.size()][colNames.length];
	}

	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
		editableCells = new boolean[exercises.size()][colNames.length];
	}

	public String[] getColNames() {
		return colNames;
	}

	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	public String[] getColumnNames() {
		return colNames;
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return exercises.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return exercises.get(row).getId();
		case 1:
			return exercises.get(row).getNumber();
		case 2:
			return exercises.get(row).getTitle();
		case 3:
			return exercises.get(row).getBodyPart();
		case 4:
			return exercises.get(row).getEquipment();
		case 5:
			return exercises.get(row).getReps();
		case 6:
			return exercises.get(row).getVideoLink();
		case 7:
			return exercises.get(row).getNote();
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {

		Exercise exercise = exercises.get(row);

		if (col == 0)
			return;

		if (col == 1) {
			int exerciseNum = exercises.get(row).getNumber();

			try {
				exerciseNum = Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			exercise.setNumber(exerciseNum);
			exercises.set(row, exercise);
			return;
		}

		switch (col) {
		
		case 2:
			exercise.setTitle((String) value);
			exercises.set(row, exercise);
			break;
		case 3:
			exercise.setBodyPart((String) value);
			exercises.set(row, exercise);
			break;
		case 4:
			exercise.setEquipment((String) value);
			exercises.set(row, exercise);
			break;
		case 5:
			exercise.setReps((String) value);
			exercises.set(row, exercise);
			exercises.set(row, exercise);
			break;
		case 6:
			exercise.setVideoLink((String) value);
			exercises.set(row, exercise);
			break;
		case 7:
			exercise.setNote((String) value);
			exercises.set(row, exercise);
		default:
			return;
		}
	}
}
