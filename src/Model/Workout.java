package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {
	
	private static final long serialVersionUID = 2332568160866620715L;
	private int week;
	private ArrayList<Exercise> exercises;
	
	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "Week " + week;
	}

	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}
}
