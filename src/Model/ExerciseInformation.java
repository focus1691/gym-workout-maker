package Model;

public enum ExerciseInformation {
	GROUP("Group"),
	EXERCISE("Exercise"),
	SETSANDREPS("Sets and Reps");
	
	private String text;
	
	private ExerciseInformation(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
