package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringJoiner;

import javax.swing.JOptionPane;

import GUI.WorkoutPanel;
import Model.Database;
import Model.Exercise;
import Model.Workout;

public class Controller {

	private Database database;
	private ArrayList<Exercise> exercises;

	public Controller() {
		database = new Database();
		setExercises(database.getExercises());
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public void putExercisesInTree(WorkoutPanel treePanel, ArrayList<Exercise> exercises) {
		if (treePanel.addExerciseNodes(exercises) == false) {
			JOptionPane.showMessageDialog(null, "Select a workout", "Fatal Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void putWorkoutInTree(WorkoutPanel treePanel, int week) {
		Workout workout = new Workout();
		workout.setWeek(week);
		treePanel.addWorkoutNode(workout);
	}

	public void createPDF(ArrayList<Workout> workouts, String src, String dest) {
		new WorkoutPDF(workouts, src, dest);
	}

	public void saveData(ArrayList<Workout> workouts, String path) {

		int i, j, k;
		String row;

		try {

			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));

			for (i = 0; i < workouts.size(); i++) {
				StringJoiner outerJoiner = new StringJoiner("--");
				outerJoiner.add(String.valueOf(workouts.get(i).getWeek()));
				row = outerJoiner.toString();
				writer.println(row);

				for (j = 0; j < workouts.get(i).getExercises().size(); j++) {
					StringJoiner innerJoiner = new StringJoiner("::");
					Exercise exercise = workouts.get(i).getExercises().get(j);
					innerJoiner.add(String.valueOf(exercise.getId())).add(String.valueOf(exercise.getNumber()))
							.add(exercise.getTitle() == null || exercise.getTitle().isEmpty() ? " " : exercise.getTitle())
							.add(exercise.getBodyPart()).add(exercise.getEquipment()).add(exercise.getReps())
							.add(exercise.getVideoLink() == null || exercise.getVideoLink().isEmpty() ? " " : exercise.getVideoLink())
							.add(exercise.getNote()  == null || exercise.getNote().isEmpty() ? " " : exercise.getNote());
					row = innerJoiner.toString();
					writer.println(row);

					for (k = 0; k < workouts.get(i).getExercises().get(j).getExercises().size(); k++) {
						StringJoiner innerofInnerJoiner = new StringJoiner("++");
						Exercise childExercise = workouts.get(i).getExercises().get(j).getExercises().get(k);
						innerofInnerJoiner.add(String.valueOf(childExercise.getId()))
								.add(String.valueOf(childExercise.getNumber()))
								.add(childExercise.getTitle() == null || childExercise.getTitle().isEmpty() ? " " : childExercise.getTitle())
								.add(childExercise.getBodyPart())
								.add(childExercise.getEquipment()).add(childExercise.getReps())
								.add(childExercise.getVideoLink() == null || childExercise.getVideoLink().isEmpty() ? " " : childExercise.getVideoLink())
								.add(childExercise.getNote()  == null || childExercise.getNote().isEmpty() ? " " : childExercise.getNote());
						row = innerofInnerJoiner.toString();
						writer.println(row);
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Workout> loadData(String path) {
		ArrayList<Workout> workouts = new ArrayList<Workout>();

		try {
			FileReader file = new FileReader(path);
			BufferedReader fileReader = new BufferedReader(file);
			String line = fileReader.readLine();
			String[] workoutParts = line.split("--");

			while (workoutParts.length == 1 && line != null) { // Check if the line is a workout (delimiter IS --)
				int week = Integer.parseInt(workoutParts[0]);
				Workout workout = new Workout();
				workout.setWeek(week);
				workouts.add(workout);

				ArrayList<Exercise> exercises = new ArrayList<Exercise>();

				line = fileReader.readLine();
				String[] exerciseParts = line.split("::");
				workoutParts = line.split("--");

				while ((exerciseParts.length == 7 || exerciseParts.length == 8) && line != null) { // Check if the line is an exercise (delimiter IS --)
					ArrayList<Exercise> childExercises = new ArrayList<Exercise>();
					int id = Integer.parseInt(exerciseParts[0]);
					int exerciseNum = Integer.parseInt(exerciseParts[1]);
					String title = exerciseParts[2];
					String body = exerciseParts[3];
					String equipment = exerciseParts[4];
					String reps = exerciseParts[5];
					String videoLink = exerciseParts[6];
					String note = "";
					if (exerciseParts.length > 7) {
						note = exerciseParts[7];
					}
					
					//String note = exerciseParts[7] == null ? "" : exerciseParts[7];
					//String note = exerciseParts[7];
					Exercise exercise = new Exercise();
					exercise.setId(id);
					exercise.setNumber(exerciseNum);
					exercise.setTitle(title);
					exercise.setBodyPart(body);
					exercise.setEquipment(equipment);
					exercise.setReps(reps);
					exercise.setVideoLink(videoLink);
					exercise.setNote(note);
					exercises.add(exercise);
					line = fileReader.readLine();
					if (line != null) {
						String[] childExerciseParts = line.split("\\++");

						while ((childExerciseParts.length == 7 || childExerciseParts.length == 8) && line != null) { // Check if the line is superset/giant set exercise (delimiter IS ++)
							int childId = Integer.parseInt(childExerciseParts[0]);
							int childExerciseNum = Integer.parseInt(childExerciseParts[1]);
							String childTitle = childExerciseParts[2];
							String childBody = childExerciseParts[3];
							String childEquipment = childExerciseParts[4];
							String childReps = childExerciseParts[5];
							String childVideoLink = childExerciseParts[6];
							String childNote = "";
							if (childExerciseParts.length > 7) {
								childNote = childExerciseParts[7];
							}
							//String childNote = childExerciseParts[7];
							//String childNote = childExerciseParts[7] == null ? "" : childExerciseParts[7];
							//if (childNote == null) childNote = "";
							Exercise childExercise = new Exercise();
							childExercise.setId(childId);
							childExercise.setNumber(childExerciseNum);
							childExercise.setTitle(childTitle);
							childExercise.setBodyPart(childBody);
							childExercise.setEquipment(childEquipment);
							childExercise.setReps(childReps);
							childExercise.setVideoLink(childVideoLink);
							childExercise.setNote(childNote);
							childExercises.add(childExercise);
							line = fileReader.readLine();
							if (line != null) {
								childExerciseParts = line.split("\\++");
							}
						}
					}
					exercise.setExercises(childExercises);

					if (line != null) {
						exerciseParts = line.split("::");
						workoutParts = line.split("--");
					}
				}
				workout.setExercises(exercises);
			}
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workouts;
	}

	public ArrayList<Exercise> getExercises(String query) {

		int i;

		ArrayList<Exercise> exercises = database.getExercises();
		ArrayList<Exercise> matchedExercises = new ArrayList<Exercise>();

		for (i = 0; i < exercises.size(); i++) {
			if (exercises.get(i).getEquipment().toLowerCase().contains(query.toLowerCase())) {
				matchedExercises.add(exercises.get(i));
			}
		}
		return matchedExercises;
	}

	public void saveExercise(String bodyPart, String equipment, String site) {
		int id = database.insertExercise(bodyPart, equipment, site);
		Exercise exercise = new Exercise(id, bodyPart, equipment, site);
		exercises.add(exercise);
	}

	public ArrayList<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}
}
