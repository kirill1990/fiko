package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import ru.fiko.purchase.Main;
import ru.fiko.purchase.supports.ComboItemStringValue;

public class Settings extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8289627279722245129L;

    private Main purchases223fz;

    /**
     * Определяет таблицу, у которой изменяют данные
     */
    private JComboBox type;

    /**
     * Список со всеми наименованиями
     */
    private JTable data;

    /**
     * Список таблиц. Поля таблиц должны быть только:<br>
     *  - id INTEGER;<br>
     *  - title STRING.<br>
     */
    private static Object[] table_items = {
	    new ComboItemStringValue("type_of_org",
		    "Вид деятельности организации"),
	    new ComboItemStringValue("subject", "Предмет закупок"),
	    new ComboItemStringValue("type", "Классификация закупки"),
	    new ComboItemStringValue("aspect", "Способ размещения заказа") };

    public Settings(Main purchases223fz) throws SQLException {
	this.purchases223fz = purchases223fz;

	/**
	 * Настройки панели организации
	 */
	this.setLayout(new BorderLayout(5, 5));
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	//
	this.add(top(), BorderLayout.NORTH);
	this.add(data_panel(), BorderLayout.CENTER);
	this.add(foot_btns(), BorderLayout.SOUTH);

	updateDataTable();
    }

    /**
     * Панель с выпадающим списком(выбор таблицы) и кнопкой добавить строку в
     * таблицу.
     * 
     * @return панель с элементами управления.
     */
    private JPanel top() {

	type = new JComboBox(table_items);
	type.addActionListener(new ListenerChangeTable());

	JButton btn_add_item = new JButton("Добавить строку");
	btn_add_item.addActionListener(new ListenerAddStringToTable());

	JPanel panel = new JPanel(new BorderLayout(5, 5));

	panel.add(type, BorderLayout.CENTER);
	panel.add(btn_add_item, BorderLayout.EAST);

	return panel;
    }

    private JPanel data_panel() {
	data = new JTable();

	data.getModel().addTableModelListener(new JTableChanged());

	JPanel panel = new JPanel(new BorderLayout(5, 5));
	panel.add(new JScrollPane(data), BorderLayout.CENTER);
	return panel;
    }

    /**
     * Формирование панели с управляющими кнопками.
     * 
     * @return панель с управляющими кнопкамии
     */
    private JPanel foot_btns() {

	JButton btn_prev = new JButton("Назад");
	btn_prev.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		backToSearchOrg();
	    }
	});

	/**
	 * Сборка управляющих кнопок
	 */
	JPanel foot_btns = new JPanel(new GridLayout(1, 1, 5, 5));
	foot_btns.add(btn_prev);

	JPanel foot = new JPanel(new BorderLayout());
	foot.add(foot_btns, BorderLayout.EAST);

	return foot;
    }

    /**
     * Возвращение к панели поиска организации
     */
    public void backToSearchOrg() {
	try {
	    purchases223fz.setPanelSearchOrg();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Обновление данных в таблице
     * 
     * @throws SQLException
     */
    private void updateDataTable() throws SQLException {

	/**
	 * Хранит данные, для внесения в таблицу
	 */
	Vector<Vector<String>> values = new Vector<Vector<String>>();

	/**
	 * Поиск наименований
	 */
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Main.PATHTODB);

	Statement stat = conn.createStatement();

	ResultSet rs = stat.executeQuery("SELECT id, title FROM " + getTable()
		+ " ;");

	while (rs.next()) {
	    Vector<String> element = new Vector<String>();
	    element.add(rs.getString("id"));
	    element.add(rs.getString("title"));
	    values.add(element);
	}
	rs.close();
	stat.close();
	conn.close();

	/**
	 * Настройки таблицы
	 */
	Vector<String> header = new Vector<String>();
	header.add("id");
	header.add("Наименование");

	DefaultTableModel dtm = (DefaultTableModel) data.getModel();
	dtm.setDataVector(values, header);

	TableColumnModel colModel = data.getColumnModel();

	/**
	 * Скрытие колонки с id, т.к. нафиг её не нужно видеть пользователю
	 */
	colModel.getColumn(0).setMaxWidth(0);
	colModel.getColumn(0).setMinWidth(0);
	colModel.getColumn(0).setPreferredWidth(0);
    }

    /**
     * Добавление новой строчки в таблицу
     * 
     * @param title
     *            - заголовок новой строки
     * @throws SQLException
     */
    private void addStringToTable(String title) throws SQLException {
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Main.PATHTODB);

	PreparedStatement pst = conn.prepareStatement("INSERT INTO "
		+ getTable() + " VALUES (?, ?);");

	// Наименование
	pst.setString(2, title);

	pst.addBatch();

	pst.executeBatch();
	pst.close();
	conn.close();
    }

    private void updateString(String id, String title) throws SQLException {
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Main.PATHTODB);

	Statement stat = conn.createStatement();

	stat.executeUpdate("UPDATE " + getTable() + " SET title = '" + title
		+ "' WHERE id LIKE '" + id + "';");

	stat.close();

	conn.close();
    }

    /**
     * Выдает наименование активной таблицы в бд
     * 
     * @return наименование таблицы
     */
    private String getTable() {
	return ((ComboItemStringValue) type.getSelectedItem()).getValue();
    }

    /**
     * Событие: Измненение текущей таблицы -> смена данных в списке
     * 
     * @author kirill
     * 
     */
    private class ListenerChangeTable implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		updateDataTable();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}

    }

    /**
     * 
     * Событие: Добавление новой строки в таблицу
     * 
     * @author kirill
     * 
     */
    private class ListenerAddStringToTable implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    String title = JOptionPane
		    .showInputDialog("Введите наименование нового пункта: ");
	    try {
		addStringToTable(title);
		updateDataTable();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}

    }

    /**
     * Обновление бд, после изменения значения в таблице
     * 
     * @author kirill
     * 
     */
    private class JTableChanged implements TableModelListener {

	@Override
	public void tableChanged(TableModelEvent e) {
	    if (e.getColumn() > 0) {
		DefaultTableModel dtm = (DefaultTableModel) e.getSource();

		String id = dtm.getValueAt(e.getFirstRow(), 0).toString();
		String title = dtm.getValueAt(e.getFirstRow(), 1).toString();

		try {
		    updateString(id, title);
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

}
