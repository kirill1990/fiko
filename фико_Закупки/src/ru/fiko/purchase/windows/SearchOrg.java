package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import ru.fiko.purchase.main.Purchase;
import ru.fiko.purchase.supports.CheckListItem;
import ru.fiko.purchase.supports.CheckListRenderer;

public class SearchOrg extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 5160203124659900133L;
    private JTextField jSearchTextField;
    private Vector<CheckListItem> checklist;

    public SearchOrg() throws SQLException, ClassNotFoundException {

	Class.forName("org.sqlite.JDBC");

	this.setLayout(new BorderLayout(5, 5));
	this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	JPanel filter = new JPanel((new BorderLayout(5, 5)));
	JPanel grid = new JPanel();

	this.add(filter, BorderLayout.NORTH);
	this.add(grid, BorderLayout.CENTER);

	JPanel f1 = new JPanel(new BorderLayout(5, 5));
	f1.add(new JLabel("Организация: "), BorderLayout.WEST);
	jSearchTextField = new JTextField();
	f1.add(jSearchTextField);

	JPanel f2 = new JPanel(new BorderLayout(5, 5));

	checklist = new Vector<CheckListItem>();
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchase.PATHTODB);

	Statement stat = conn.createStatement();
	ResultSet test_type_of_org = stat
		.executeQuery("SELECT title FROM type_of_org");

	while (test_type_of_org.next()) {
	    checklist
		    .add(new CheckListItem(test_type_of_org.getString("title")));
	}
	test_type_of_org.close();
	stat.close();
	conn.close();

	JList list = new JList(checklist);

	// Use a CheckListRenderer (see below)
	// to renderer list cells

	list.setCellRenderer(new CheckListRenderer());
	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list.setVisibleRowCount(5);

	// Add a mouse listener to handle changing selection

	list.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent event) {
		JList list = (JList) event.getSource();

		// Get index of item clicked

		int index = list.locationToIndex(event.getPoint());
		CheckListItem item = (CheckListItem) list.getModel()
			.getElementAt(index);

		// Toggle selected state

		item.setSelected(!item.isSelected());

		// Repaint cell

		list.repaint(list.getCellBounds(index, index));
	    }
	});
	
	JPanel f22 = new JPanel(new BorderLayout());
	JCheckBox check = new JCheckBox("Регистрация");
	f22.add(new JLabel("Вид деятельности:"), BorderLayout.WEST);
	f22.add(check, BorderLayout.EAST);
	

	f2.add(f22, BorderLayout.NORTH);
	f2.add(new JScrollPane(list), BorderLayout.CENTER);
	filter.add(f1, BorderLayout.NORTH);
	filter.add(f2, BorderLayout.CENTER);

	this.validate();
    }
}
