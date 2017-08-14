package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
	
	private Connection con;
	private final String dbName = "WorkoutBuilder";
	private String url = "jdbc:mysql://localhost:3306/";
	private final String user = "root";
	private final String password = "";
	public Database() {
		createDatabase();
		createExerciseTable();
	}
	
	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection (this.url, this.user, this.password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createDatabase() {
		connect();
		final String sql = "CREATE DATABASE IF NOT EXISTS " + dbName;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
			url += dbName + "?verifyServerCertificate=false&useSSL=true";
		} catch (SQLException e) {
			url = "jdbc:mysql://localhost:3308/";
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	

	
	public void createExerciseTable() {
		connect();
		final String sql = "CREATE TABLE IF NOT EXISTS Exercise " +
		                   "(exerciseId INTEGER NOT NULL AUTO_INCREMENT, " +
		                   " bodyPart VARCHAR(255), " +
		                   " equipment VARCHAR(255), " +
		                   " videoLink VARCHAR(255), " + 
		                   " PRIMARY KEY (exerciseId))";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	public boolean isExerciseExists(int exerciseId) {
		connect();
		final String sql = "SELECT * FROM Exercise WHERE exerciseId = ?";
		
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, exerciseId);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return false;
	}
	
	public int insertExercise(String bodyPart, String equipment, String videoLink) {
		connect();
		int id = 0;
		final String sql = "INSERT INTO Exercise (bodyPart, equipment, videoLink) VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, bodyPart);
			ps.setString(2, equipment);
			ps.setString(3, videoLink);
			ps.executeUpdate();
	        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return id;
	}
	
	public void updateExercise(int exerciseId, int exerciseNum, String bodyPart, String equipment, String videoLink) {
		connect();
		final String sql = "UPDATE Exercise SET exerciseNumber = ?, bodyPart = ?, equipment = ?, videoLink = ? WHERE exerciseId = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, exerciseNum);
			ps.setString(2, bodyPart);
			ps.setString(3, equipment);
			ps.setString(4, videoLink);
			ps.setInt(5, exerciseId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
	
	public ArrayList<Exercise> getExercises() {
		
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		connect();
		final String sql = "SELECT exerciseId, bodyPart, equipment, videoLink FROM Exercise";
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement(sql);
			ResultSet results = ps.executeQuery(sql);
			while (results.next()) {
				Exercise exercise = new Exercise();
				exercise.setId(results.getInt("exerciseId"));
				exercise.setBodyPart(results.getString("bodyPart"));
				exercise.setEquipment(results.getString("equipment"));
				exercise.setVideoLink(results.getString("videoLink"));
				exercises.add(exercise);
			}
			if (results != null) {
				results.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return exercises;
	}
}
