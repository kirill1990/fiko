package ru.fiko.purchase.supports;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

//Handles rendering cells in the list using a check box

public class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    /**
	 * 
	 */
    private static final long serialVersionUID = 6054410183836695681L;

    public Component getListCellRendererComponent(JList list, Object value,
	    int index, boolean isSelected, boolean hasFocus) {
	setEnabled(list.isEnabled());
	setSelected(((CheckListItem) value).isSelected());
	setFont(list.getFont());
	setBackground(list.getBackground());
	setForeground(list.getForeground());
	setText(value.toString());
	return this;
    }
}