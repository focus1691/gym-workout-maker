package GUI;

import java.util.ArrayList;
import java.util.EventObject;

import Model.Exercise;

public class ExerciseSelectionEvent extends EventObject {
	
	private static final long serialVersionUID = -2310932145585687735L;
	private ArrayList<Exercise> exercises;

	public ExerciseSelectionEvent(Object source) {
		super(source);
	}
	
	public ExerciseSelectionEvent(Object source, ArrayList<Exercise> exercises) {
		super(source);
		this.exercises = exercises;
	}

	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}
}
