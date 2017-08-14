package Model;

import java.util.ArrayList;

public class WorkoutRoutine {
	
	private int id;
	private String person;
	private ArrayList<Workout> workouts;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public ArrayList<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(ArrayList<Workout> workouts) {
		this.workouts = workouts;
	}
	@Override
	public String toString() {
		return person;
	}
}
