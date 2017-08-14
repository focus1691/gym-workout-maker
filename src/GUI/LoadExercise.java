package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Model.Exercise;

public class LoadExercise extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 3380718068756934547L;
	private MainFrame mf;
	private JButton searchBtn;
	private JTextField searchText;

	private JButton loadBtn;

	private JComboCheckBox combo;
	private JCheckBox[] checkbox;

	private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

	public LoadExercise(MainFrame mf, ArrayList<Exercise> exercises) {
		this.mf = mf;
		this.exercises = mf.controller.getDatabase().getExercises();
		checkbox = new JCheckBox[exercises.size()];
		initComponents();
		setupUI();
		
		mf.centerFrame(this);
		setModal(true);
		setAlwaysOnTop(true);
		setVisible(true);
	}
	
    private static volatile boolean enterPressed = false;
    public static boolean isEnterPressed() {
        synchronized (LoadExercise.class) {
            return enterPressed;
        }
    }

	public void initComponents() {

		setTitle("Load Exercises");
		setSize(350, 175);
		setResizable(false);
		searchText = new JTextField();
		searchText.setPreferredSize(new Dimension(200, 28));
		
		for (int i = 0; i < exercises.size(); i++) {
			checkbox[i] = new JCheckBox(exercises.get(i).getEquipment());
		}
		combo = new JComboCheckBox(checkbox);
		combo.setPreferredSize(new Dimension(175, 30));

		loadBtn = new JButton("Load");
		loadBtn.setPreferredSize(new Dimension(100, 30));
		
		
		
		searchBtn = new JButton("Search");
		searchBtn.setPreferredSize(new Dimension(100, 30));
		
		loadBtn.addActionListener(this);
		searchBtn.addActionListener(this);
		
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (LoadExercise.class) {
                	switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        	loadExercises();
                            enterPressed = true;
                        }
                        break;

                    case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        	System.out.println("bbb");
                        	enterPressed = false;
                        }
                        break;
                    }
                    return false;
                }
            }
        });
    }

	public void setupUI() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gc = new GridBagConstraints();
		setLayout(gbl);

		gc.weightx = 1;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(searchText, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_START;
		add(searchBtn, gc);

		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(combo, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(loadBtn, gc);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadBtn) {
			ArrayList<Exercise> exercises = new ArrayList<Exercise>();
			for (int i = 0; i < checkbox.length; i++) {
				if (checkbox[i].isSelected()) {
					Exercise exercise = this.exercises.get(i);
					exercises.add(exercise);
				}
			}
			mf.controller.putExercisesInTree(mf.treePanel, exercises);
			mf.exerciseTableModel.addExercises(exercises);
			mf.treePanel.expandAllNodes(mf.treePanel.getTree(), 0, mf.treePanel.getTree().getRowCount());
			dispose();
		}
		else if (e.getSource() == searchBtn) {
			loadExercises();
		}
	}
	
	public void loadExercises() {
		exercises.clear();
		
		exercises = mf.controller.getExercises(searchText.getText());
		Collections.sort(exercises, new Comparator<Exercise>() {
			@Override
			public int compare(Exercise o1, Exercise o2) {
				return o1.getEquipment().compareToIgnoreCase(o2.getEquipment());
			}
		});
		
		if (!exercises.isEmpty()) {
			
			combo.removeAllItems();
			combo.removeAll();
			this.remove(combo);
		
			checkbox = new JCheckBox[exercises.size()];
			for (int i = 0; i < exercises.size(); i++) {
				checkbox[i] = new JCheckBox(exercises.get(i).getEquipment());
			}
			combo = new JComboCheckBox(checkbox);
			add(combo);
			combo.setPreferredSize(new Dimension(175, 30));
			setupUI();
			revalidate();
		} else {
			JOptionPane.showMessageDialog(this, "No Exercises found.", "Search Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
