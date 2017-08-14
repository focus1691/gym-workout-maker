package GUI;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import Model.ExerciseInformation;

public class ExerciseInformationRenderer implements TableCellRenderer {

	private JComboBox combo;

	public ExerciseInformationRenderer() {
		combo = new JComboBox(ExerciseInformation.values());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		combo.setSelectedItem(value);
		
		return combo;
	}

}
