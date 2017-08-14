package GUI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ExerciseInfoEditor extends AbstractCellEditor implements TableCellEditor {
	
	private static final long serialVersionUID = 222269520477166289L;
	private JComboBox<Object> combo;
	String[] arr = { "one", "two", "three" };
	
	public ExerciseInfoEditor() {
		combo = new JComboBox<Object>(arr);
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		combo.setSelectedIndex((int)value);
		combo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		return combo;
	}

	@Override
	public Object getCellEditorValue() {
		return combo.getSelectedItem();
	}
	
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}
}
