package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang.SerializationUtils;

import Model.Exercise;
import Model.Workout;

public class WorkoutPanel extends JPanel {

	private static final long serialVersionUID = 4209629740600380007L;
	private JTree tree;
	private DefaultMutableTreeNode top = new DefaultMutableTreeNode("Workouts");
	private DefaultMutableTreeNode copyNode;
	private JPopupMenu menu = new JPopupMenu();
	private JMenuItem delete = new JMenuItem("Delete"); // Delete option
	private JMenuItem edit = new JMenuItem("Edit"); // Delete option
	private JMenuItem copy = new JMenuItem("Copy"); // Copy option
	private JMenuItem paste = new JMenuItem("Paste"); // Paste option
	private ExerciseSelectionListener listener;

	public WorkoutPanel() {
		tree = new JTree(top);
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON_OR_INSERT);
		tree.setTransferHandler(new TreeDragAndDrop());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		menu.add(copy);
		menu.add(paste);
		menu.add(edit);
		menu.add(delete);
		
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				if (node.getUserObject() instanceof Exercise) {
					 model.removeNodeFromParent(node);
				}
				else if (node.getUserObject() instanceof Workout) {
					model.removeNodeFromParent(node);
				}
			}
		});
		
		copy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node.getUserObject() instanceof Workout) {
					//copyNode = deepClone2(node);
					copyNode = deepClone(node);
				}
			}
		});
		
		paste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (copyNode != null) {
					if (copyNode.getUserObject() instanceof Workout) {
						addWorkout(copyNode);
					}
				}
			}
		});
		
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				if (node.getUserObject() instanceof Workout) {
					new EditWorkout(node);
				} else {
					JOptionPane.showMessageDialog(null, "You can only edit workouts.", "Edit Warning", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
				if (e.getButton() == MouseEvent.BUTTON3) {
					showPopup(e, menu);
				}
				
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
					} else if (e.getClickCount() == 2) {
						getExercisesFromWorkoutNode();
					}
				}
			}
		};
		tree.addMouseListener(ml);

		setLayout(new BorderLayout());
		add(new JScrollPane(tree), BorderLayout.CENTER);
	}
	
	private DefaultMutableTreeNode deepClone(DefaultMutableTreeNode source) {
	    DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) SerializationUtils.clone(source);
	    /*
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
	    int i, j;
	    Workout workout = (Workout) newNode.getUserObject();
	    for (i = 0; i < workout.getExercises().size(); i++) {
	    	Exercise exercise = (Exercise) SerializationUtils.clone(workout.getExercises().get(i));
	    	ArrayList<Exercise> childExercises = new ArrayList<Exercise>();
	    	DefaultMutableTreeNode exerciseNode = new DefaultMutableTreeNode(exercise);
	    	newNode.add(exerciseNode);
	    	exercises.add(exercise);
	    	
	    	for (j = 0; j < exercise.getExercises().size(); j++) {
	    		Exercise childExercise = (Exercise) SerializationUtils.clone(workout.getExercises().get(i).getExercises().get(j));
	    		childExercises.add(childExercise);
	    		DefaultMutableTreeNode childExerciseNode = new DefaultMutableTreeNode(childExercise);
	    		exerciseNode.add(childExerciseNode);
	    	}
	    	exercise.setExercises(childExercises);
	    }
	    workout.setExercises(exercises);
	    */
	    return newNode;
	  }
	
	public void getExercisesFromWorkoutNode() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node != null && node.getUserObject() instanceof Workout) {
			ArrayList<Exercise> exercises = new ArrayList<Exercise>();
			while ((node.getNextNode()) != null
					&& node.getNextNode().getUserObject() instanceof Exercise) {
				node = node.getNextNode();
				Exercise exercise = (Exercise) node.getUserObject();
				exercises.add(exercise);
			}
			ExerciseSelectionEvent event = new ExerciseSelectionEvent(this, exercises);

			if (listener != null) {
				listener.exerciseSelectionEventOccurred(event);
			}
		}
	}
	
    private void showPopup(MouseEvent ae, JPopupMenu menu) {
        
        // Get the event source
        JTree b=(JTree)ae.getSource();
        
        // Get the location of the point 'on the screen'
        TreePath p = b.getPathForLocation(ae.getX(), ae.getY());
        
        tree.setSelectionPath(p);
        
        menu.show(this,ae.getX(),ae.getY());
    }
	
	public void addWorkoutNode(Workout workout) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.add(new DefaultMutableTreeNode(workout));
		model.reload(root);
	}
	
	public void addWorkout(DefaultMutableTreeNode workout) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.add(workout);
		model.reload(root);
	}
	
	public void addWorkouts(ArrayList<Workout> workouts) {
		
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		
		int i, j, k;
		
		for (i = 0; i < workouts.size(); i++) {
			DefaultMutableTreeNode workoutNode = new DefaultMutableTreeNode(workouts.get(i));
			root.add(workoutNode);
			for (j = 0; j < workouts.get(i).getExercises().size(); j++) {
				DefaultMutableTreeNode exerciseNode = new DefaultMutableTreeNode(workouts.get(i).getExercises().get(j));
				workoutNode.add(exerciseNode);
				for (k = 0; k < workouts.get(i).getExercises().get(j).getExercises().size(); k++) {
					DefaultMutableTreeNode childExerciseNode = new DefaultMutableTreeNode(workouts.get(i).getExercises().get(j).getExercises().get(k));
					exerciseNode.add(childExerciseNode);
				}
			}
		}
		model.reload(root);
	}

	public boolean addExercise() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			return false;
		} else if (node.getUserObject() instanceof Workout) {
			node.add(new DefaultMutableTreeNode(new Exercise()));
			model.reload(node);
			return true;
		} else {
			while ((DefaultMutableTreeNode) node.getParent() != null) {
				node = (DefaultMutableTreeNode) node.getParent();
				if (node.getUserObject() instanceof Workout) {
					node.add(new DefaultMutableTreeNode(new Exercise()));
					model.reload(node);
					return true;
				}
			}
		}
		return false;
	}

	public boolean addExerciseNodes(ArrayList<Exercise> exercises) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node == null) {
			return false;
		}
		if (isNodeWorkout(node)) {
			addnodes(exercises, model, node);
			return true;
		}

		while ((DefaultMutableTreeNode) node.getParent() != null) {
			node = (DefaultMutableTreeNode) node.getParent();
			if (node.getUserObject() instanceof Workout) {
				addnodes(exercises, model, node);
				return true;
			}
		}
		return false;
	}

	public void addnodes(ArrayList<Exercise> exercises, DefaultTreeModel model, DefaultMutableTreeNode node) {
		
		Workout workout = (Workout) node.getUserObject();
		workout.setExercises(exercises);
		
		for (int i = 0; i < exercises.size(); i++) {
			node.add(new DefaultMutableTreeNode(exercises.get(i)));
		}
		model.reload(node);
	}

	public boolean isNodeWorkout(DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof Workout) {
			return true;
		}
		return false;
	}

	public void clearTree() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren(); // this removes all nodes
		model.reload();
	}

	public ArrayList<Workout> getWorkouts() {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		ArrayList<Workout> workouts = new ArrayList<Workout>();

		if (root != null && root.getNextNode() != null) {
			
			// Get the workout node below root
			root = root.getNextNode();

			while (root != null && root.getUserObject() instanceof Workout) {
				
				// Get the workout object from the workout node and add it to the workout list
				Object obj = root.getUserObject();
				Workout workout = (Workout) obj;
				workouts.add(workout);
				
				// Init the list of exercises for this workout
				ArrayList<Exercise> exercises = new ArrayList<Exercise>();
				
				DefaultMutableTreeNode exerciseNode = root.getNextNode();

				while (exerciseNode != null && exerciseNode.getUserObject() instanceof Exercise) {
					
					ArrayList<Exercise> childExercises = new ArrayList<Exercise>(); // Superset/Giant set exercises
					Exercise exercise = (Exercise) exerciseNode.getUserObject(); // Get the exercise object in the superset/giant set
					
					for (int i = 0; i < exerciseNode.getChildCount(); i++) { // Iterate through all of the superset/giant set exercises
						
						DefaultMutableTreeNode childExerciseNode = (DefaultMutableTreeNode) exerciseNode.getChildAt(i);
						
						if (childExerciseNode.getUserObject() instanceof Exercise) {
							Exercise childExercise = (Exercise) childExerciseNode.getUserObject();
							childExercises.add(childExercise);
						}
					} // End of superset/giant set exercises for the current exercise
					if (exerciseNode.getNextSibling() == null) {
						exerciseNode = null;
					} else {
						exerciseNode = exerciseNode.getNextSibling();
					}
					exercise.setExercises(childExercises);
					exercises.add(exercise);
				} // End of exercises for this workout
				workout.setExercises(exercises);
				root = root.getNextSibling();
			}
		}
		return workouts;
	}
	
	public void reload() {
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		model.reload(root);
	}
	
	public void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
	   
		for (int i = startingIndex; i < rowCount; ++i){
	        tree.expandRow(i);
	    }
		
	    if(tree.getRowCount() != rowCount){
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	    }
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public void setExerciseListener(ExerciseSelectionListener listener) {
		this.listener = listener;
	}
}
