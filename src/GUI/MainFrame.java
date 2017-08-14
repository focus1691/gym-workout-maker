package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import Controller.Controller;
import Model.ExerciseTableModel;
import Model.Workout;

public class MainFrame implements ActionListener {

	private JFrame frame;

	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu toolMenu;
	private JMenuItem addExerciseItem;
	private JMenuItem loadExercisesItem;
	private JMenuItem loadWorkoutItem;
	private JMenuItem pdfItem;
	private JMenuItem refreshItem;
	private JMenuItem clearItem;
	private JMenuItem exitBtn;

	protected JPanel toolbar;
	private JButton workoutBtn;
	private JButton exerciseBtn;
	private JButton saveBtn;
	private JButton refreshBtn;
	private JButton clearBtn;
	private JButton pdfBtn;

	private JSplitPane splitPane;
	private JTable table;
	protected WorkoutPanel treePanel;

	protected Controller controller;
	protected ExerciseTableModel exerciseTableModel;

	public static void main(String[] args) {
		new MainFrame();
	}

	private MainFrame() {
		initLogic();
		initComponents();
		setupUI();

		frame.setPreferredSize(new Dimension(1200, 600));
		frame.setMinimumSize(new Dimension(400, 300));
		frame.pack();
		centerFrame(frame);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initLogic() {
		controller = new Controller();
		exerciseTableModel = new ExerciseTableModel();
	}

	private void initComponents() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}

		frame = new JFrame();
		frame.setTitle("Workout Builder");

		/* Create the menu bar */
		menu = new JMenuBar();
		frame.setJMenuBar(menu);
		menu.setBorder(BorderFactory.createEmptyBorder());
		fileMenu = new JMenu("File");
		toolMenu = new JMenu("Tools");
		menu.add(fileMenu);
		menu.add(toolMenu);

		exitBtn = new JMenuItem("Exit");
		exitBtn.setIcon(IconFetch.getInstance().getIcon("/images/exit.png"));
		fileMenu.add(exitBtn);

		addExerciseItem = new JMenuItem("New Exercise");
		addExerciseItem.setIcon(IconFetch.getInstance().getIcon("/images/add.png"));
		toolMenu.add(addExerciseItem);

		loadExercisesItem = new JMenuItem("Load Exercises");
		loadExercisesItem.setIcon(IconFetch.getInstance().getIcon("/images/load.png"));
		toolMenu.add(loadExercisesItem);
		
		loadWorkoutItem = new JMenuItem("Read Workout");
		loadWorkoutItem.setIcon(IconFetch.getInstance().getIcon("/images/dumbbell.png"));
		toolMenu.add(loadWorkoutItem);

		toolMenu.addSeparator();

		pdfItem = new JMenuItem("Generate PDF");
		pdfItem.setIcon(IconFetch.getInstance().getIcon("/images/pdf.png"));
		toolMenu.add(pdfItem);

		toolMenu.addSeparator();

		refreshItem = new JMenuItem("Refresh");
		refreshItem.setIcon(IconFetch.getInstance().getIcon("/images/refresh.png"));
		toolMenu.add(refreshItem);

		clearItem = new JMenuItem("Clear");
		clearItem.setIcon(IconFetch.getInstance().getIcon("/images/clear.png"));
		toolMenu.add(clearItem);

		exitBtn.addActionListener(this);
		loadExercisesItem.addActionListener(this);
		loadWorkoutItem.addActionListener(this);
		addExerciseItem.addActionListener(this);
		pdfItem.addActionListener(this);
		refreshItem.addActionListener(this);
		clearItem.addActionListener(this);

		/* Create the toolbar */
		toolbar = new JPanel();
		toolbar.setOpaque(true);
		toolbar.setBackground(Color.decode("#5EA0E2"));
		toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbar.setVisible(true);

		workoutBtn = new JButton("Add Week");
		workoutBtn.setFocusable(false);
		workoutBtn.setOpaque(true);
		workoutBtn.setBackground(Color.decode("#214A73"));
		workoutBtn.setIcon(IconFetch.getInstance().getIcon("/images/calender.png"));
		workoutBtn.setToolTipText("Add Workout");
		workoutBtn.setPreferredSize(new Dimension(120, 30));
		toolbar.add(workoutBtn);
		workoutBtn.addActionListener(this);

		exerciseBtn = new JButton("Exercise");
		exerciseBtn.setFocusable(false);
		exerciseBtn.setOpaque(true);
		exerciseBtn.setBackground(Color.decode("#214A73"));
		exerciseBtn.setIcon(IconFetch.getInstance().getIcon("/images/exercise.png"));
		exerciseBtn.setToolTipText("Add exercise");
		exerciseBtn.setPreferredSize(new Dimension(100, 30));
		toolbar.add(exerciseBtn);
		exerciseBtn.addActionListener(this);
		
		saveBtn = new JButton("Save");
		saveBtn.setFocusable(false);
		saveBtn.setOpaque(true);
		saveBtn.setBackground(Color.decode("#214A73"));
		saveBtn.setIcon(IconFetch.getInstance().getIcon("/images/save.png"));
		saveBtn.setToolTipText("Save");
		saveBtn.setPreferredSize(new Dimension(100, 30));
		toolbar.add(saveBtn);
		saveBtn.addActionListener(this);
		
		refreshBtn = new JButton("Refresh");
		refreshBtn.setFocusable(false);
		refreshBtn.setOpaque(true);
		refreshBtn.setBackground(Color.decode("#214A73"));
		refreshBtn.setIcon(IconFetch.getInstance().getIcon("/images/refresh.png"));
		refreshBtn.setToolTipText("Refresh");
		refreshBtn.setPreferredSize(new Dimension(100, 30));
		toolbar.add(refreshBtn);
		refreshBtn.addActionListener(this);

		clearBtn = new JButton("Clear");
		clearBtn.setFocusable(false);
		clearBtn.setOpaque(true);
		clearBtn.setBackground(Color.decode("#214A73"));
		clearBtn.setIcon(IconFetch.getInstance().getIcon("/images/clear.png"));
		clearBtn.setToolTipText("Clear Tree");
		clearBtn.setPreferredSize(new Dimension(100, 30));
		toolbar.add(clearBtn);
		clearBtn.addActionListener(this);
		
		pdfBtn = new JButton("Print PDF");
		pdfBtn.setFocusable(false);
		pdfBtn.setOpaque(true);
		pdfBtn.setBackground(Color.decode("#214A73"));
		pdfBtn.setIcon(IconFetch.getInstance().getIcon("/images/pdf.png"));
		pdfBtn.setToolTipText("Print PDF");
		pdfBtn.setPreferredSize(new Dimension(100, 30));
		toolbar.add(pdfBtn);
		pdfBtn.addActionListener(this);
		
		table = new JTable();
		table.setModel(exerciseTableModel);

		treePanel = new WorkoutPanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, new JScrollPane(table));
		
		treePanel.setExerciseListener(new ExerciseSelectionListener() {

			@Override
			public void exerciseSelectionEventOccurred(ExerciseSelectionEvent e) {
				exerciseTableModel.setExercises(e.getExercises());
				exerciseTableModel.makeCellsEditable(true);
				exerciseTableModel.fireTableDataChanged();
				frame.revalidate();
			}
		});
	}

	public void setupUI() {
		frame.setLayout(new BorderLayout());
		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(.3d);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == workoutBtn) {
			new AddWorkout(this);
			
		} else if (ae.getSource() == exitBtn) {
			System.exit(1);
			
		} else if (ae.getSource() == saveBtn) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("workouts.txt"));
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				controller.saveData(treePanel.getWorkouts(), file.getAbsolutePath());
			}
			
		} else if (ae.getSource() == clearBtn || ae.getSource() == clearItem) {
			treePanel.clearTree();
		} else if (ae.getSource() == refreshBtn) {
			treePanel.getExercisesFromWorkoutNode();
			treePanel.reload();
			treePanel.expandAllNodes(treePanel.getTree(), 0, treePanel.getTree().getRowCount());
			
		} else if (ae.getSource() == addExerciseItem) {
			new AddExercise(this);
			
		} else if (ae.getSource() == exerciseBtn || ae.getSource() == loadExercisesItem) {
			new LoadExercise(this, controller.getExercises());
			
		} else if (ae.getSource() == loadWorkoutItem) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				ArrayList<Workout> workouts = controller.loadData(file.getAbsolutePath());
				treePanel.addWorkouts(workouts);
				treePanel.expandAllNodes(treePanel.getTree(), 0, treePanel.getTree().getRowCount());
			}
			
		} else if (ae.getSource() == pdfItem || ae.getSource() == pdfBtn) {
			ArrayList<Workout> workouts = treePanel.getWorkouts();
			
			if (workouts.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "No Exercises found.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ADOBE PDF Files (*.pdf)", "pdf", "PDF");
				fileChooser.setFileFilter(filter);
				fileChooser.setSelectedFile(new File("routine.pdf"));
				if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File src = fileChooser.getSelectedFile();
					File dest = new File(src.getParentFile(), FilenameUtils.getBaseName(src.getName()) + "\\src.pdf");
					System.out.println(dest.getAbsolutePath());
					
					if (!FilenameUtils.getExtension(src.getName()).equalsIgnoreCase("pdf")) {
						src = new File(src.toString() + ".pdf");
						src = new File(src.getParentFile(), FilenameUtils.getBaseName(src.getName()) + ".pdf");
					}
					if (!FilenameUtils.getExtension(dest.getName()).equalsIgnoreCase("pdf")) {
						dest = new File(src.toString() + ".pdf");
						dest = new File(src.getParentFile(), FilenameUtils.getBaseName(src.getName()) + ".pdf");
					}
					controller.createPDF(workouts, src.getAbsolutePath(), dest.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * A general-purpose method to vertically and horizontally center a window.
	 * http://stackoverflow.com/questions/144892/how-to-center-a-window-in-java
	 */
	public void centerFrame(JFrame frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	
	public void centerFrame(JDialog dialog) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - dialog.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - dialog.getHeight()) / 2);
		dialog.setLocation(x, y);
	}
}
