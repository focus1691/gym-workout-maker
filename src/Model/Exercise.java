package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercise implements Serializable {
	
	private static final long serialVersionUID = -7559658433616444762L;
	private int id;
	private int number;
	private String title;
	private String bodyPart;
	private String equipment;
	private String reps;
	private String videoLink;
	private String note;
	private ArrayList<Exercise> exercises;
	
	public Exercise() {
		exercises = new ArrayList<Exercise>();
	}
	public Exercise(int id, String bodyPart, String equipment, String videoLink) {
		this.id = id;
		this.bodyPart = bodyPart;
		this.equipment = equipment;
		this.videoLink = videoLink;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public String getBodyPart() {
		return bodyPart;
	}
	public void setBodyPart(String bodyPart) {
		this.bodyPart = bodyPart;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getReps() {
		return reps;
	}
	public void setReps(String reps) {
		this.reps = reps;
	}
	public String getVideoLink() {
		return videoLink;
	}
	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}
	@Override
	public String toString() {
		return "Day " + number + ". " + equipment;
	}
}
