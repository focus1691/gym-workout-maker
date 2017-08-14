package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddWorkout extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 6390827101457513574L;
	private MainFrame mf;
	private JTextField weekText;
	private JButton addBtn;
	private JButton cancelBtn;
	
	public AddWorkout(MainFrame mf) {
		this.mf = mf;
		initComponents();
		setupUI();
		
		setModal(true);
		setAlwaysOnTop(true);
	}
	
	public void initComponents() {
		
		setTitle("Add Week");
		setSize(250, 125);
		
		weekText = new JTextField();
		weekText.setPreferredSize(new Dimension(150, 20));
		
		addBtn = new JButton("Add");
		cancelBtn = new JButton("Cancel");
		
		addBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
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
		add(new JLabel("Week : ") , gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(weekText, gc);
		
		gc.gridy = 1;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(addBtn, gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(cancelBtn, gc);
		
		mf.centerFrame(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelBtn) {
			this.dispose();
		}
		else if (e.getSource() == addBtn) {
			
			int week = 0;
			
			try {
				week = Integer.parseInt(weekText.getText());
				mf.controller.putWorkoutInTree(mf.treePanel, week);
				this.dispose();
			} catch (NumberFormatException fe) {
				JOptionPane.showMessageDialog(this, "You must enter an integer value for week.", "Fatal Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
