package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddExercise extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 7431795852196711263L;
	private MainFrame mf;
	private JTextField bodyText;
	private JTextField equipmentText;
	private JTextField siteText;
	
	private JButton addBtn;
	private JButton cancelBtn;
	
	public AddExercise(MainFrame mf) {
		this.mf = mf;
		initComponents();
		setupUI();
		
		setModal(true);
		setAlwaysOnTop(true);
	}
	
	public void initComponents() {
		
		setTitle("Add Exercise to Database");
		setSize(320, 175);
		bodyText = new JTextField();
		bodyText.setPreferredSize(new Dimension(150, 20));
		equipmentText = new JTextField();
		equipmentText.setPreferredSize(new Dimension(150, 20));
		siteText = new JTextField();
		siteText.setPreferredSize(new Dimension(150, 20));
		
		addBtn = new JButton("Add");
		cancelBtn = new JButton("Cancel");
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
		add(new JLabel("Muscle Group : ") , gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(bodyText, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Exercise Name : ") , gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(equipmentText, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Video Link : ") , gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(siteText, gc);
		
		gc.gridy = 3;
		gc.gridx = 0;
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;
		add(addBtn, gc);
		
		gc.gridx++;
		gc.anchor = GridBagConstraints.LINE_START;
		add(cancelBtn, gc);
		
		addBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		mf.centerFrame(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelBtn) {
			this.dispose();
		}
		else if (e.getSource() == addBtn) {
			
			String body = bodyText.getText();
			String equipment = equipmentText.getText();
			String site = siteText.getText();
			mf.controller.saveExercise(body, equipment, site);
			this.dispose();
		}
	}
}
