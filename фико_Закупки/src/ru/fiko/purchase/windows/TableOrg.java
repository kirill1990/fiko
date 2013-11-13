package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.Main;
import ru.fiko.purchase.reports.Number3or4;
import ru.fiko.purchase.reports.Number5;
import ru.fiko.purchase.supports.CheckListItem;
import ru.fiko.purchase.supports.CheckListRenderer;
import ru.fiko.purchase.supports.ComboItemBooleanValue;

public class TableOrg extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5160203124659900133L;

    /**
     * Содержит ИНН или наименование по которым осуществляется выборка
     * организаций.
     */
    private JTextField jSearchTextField;

    /**
     * Спискок принадлежности к видам юридическиз лиц
     */
    private Vector<CheckListItem> checklist;

    /**
     * Разрешение на фильтрацию организации по регистрации
     */
    private JCheckBox check;

    /**
     * Указывает фильтрацию организации по:<br>
     * - Зарегистрированным;<br>
     * - Не зарегистрированным.<br>
     */
    private JComboBox reg_box;

    /**
     * Панель "Расширенного поиска"
     */
    private JPanel filter_type_registr;

    /**
     * Таблица с отфильтрованным списком организаций
     */
    private JTable org_table;

    /**
     * Указатель на родителя.<br>
     * Используется для смены компонентов окна.
     */
    private Main purchase223FZ;

    /**
     * Конструктор. Инициализация структуры панели поиска организации.
     * 
     * @param parent
     *            - класс, создавший данную панель
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public TableOrg(Main purchase) throws SQLException,
	    ClassNotFoundException {
	this.purchase223FZ = purchase;

	Class.forName("org.sqlite.JDBC");

	/**
	 * Часть 1. Фильтрация организаций
	 */

	/**
	 * Строка для ввода ИНН или наименования организации
	 */
	jSearchTextField = new JTextField();
	jSearchTextField
		.setToolTipText("Поиск по ИНН и наимаенованию организации");
	jSearchTextField.addKeyListener(new KeyListener() {

	    @Override
	    public void keyTyped(KeyEvent e) {
		try {
		    updateTable();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }

	    @Override
	    public void keyReleased(KeyEvent e) {
		try {
		    updateTable();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
		try {
		    updateTable();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }
	});

	/**
	 * Кнопка раскрытия/скрытие расширенного поиска
	 */
	JButton btn = new JButton("Расширенный поиск");
	btn.setToolTipText("Расширенный поиск");
	btn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		if (filter_type_registr.isVisible())
		    filter_type_registr.setVisible(false);
		else
		    filter_type_registr.setVisible(true);
	    }
	});

	JPanel filter_orgainzation = new JPanel(new BorderLayout());
	// filter_orgainzation.add(new JLabel("Организация: "),
	// BorderLayout.WEST);
	filter_orgainzation.add(jSearchTextField, BorderLayout.CENTER);
	filter_orgainzation.add(btn, BorderLayout.EAST);

	/**
	 * Формирование списка принадлежности к видам юридическиз лиц,
	 * определенных в ч. 2 статьи 1 Закона 223-ФЗ. <br>
	 * <br>
	 * Изначально не статичен, т.к. возможны изменения в наименовании, а
	 * также добавление новых видов. (Контролирует пользователь программы).
	 */

	checklist = new Vector<CheckListItem>();
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	Statement stat = conn.createStatement();
	ResultSet rs_type_of_org = stat
		.executeQuery("SELECT id, title FROM type_of_org");

	while (rs_type_of_org.next())
	    checklist.add(new CheckListItem(rs_type_of_org.getString("title"),
		    rs_type_of_org.getInt("id")));

	rs_type_of_org.close();
	stat.close();
	conn.close();

	JList list_type_of_org = new JList(checklist);

	list_type_of_org.setCellRenderer(new CheckListRenderer());
	list_type_of_org.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list_type_of_org.setVisibleRowCount(5);
	/**
	 * Инверсия значения выбранного элемента
	 */
	list_type_of_org.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent event) {
		JList list = (JList) event.getSource();

		int index = list.locationToIndex(event.getPoint());
		CheckListItem item = (CheckListItem) list.getModel()
			.getElementAt(index);
		item.setSelected(!item.isSelected());
		list.repaint(list.getCellBounds(index, index));

		try {
		    updateTable();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	});

	// TODO комментарий
	/**
	 * 
	 */
	check = new JCheckBox();
	check.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    updateTable();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	});

	reg_box = new JComboBox(Constant.registr_items);
	reg_box.setToolTipText("Регистрация на ООС");
	/**
	 * Выставляет флаг фильтра по организации в true.<br>
	 * Сделанно для того, чтобы пользователю не нужно было совершать
	 * дополнительный клик мыши по флагу регистрации.
	 */
	reg_box.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		check.setSelected(true);
		try {
		    updateTable();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }
	});
	reg_box.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		check.setSelected(true);
		try {
		    updateTable();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		}
	    }
	});

	/**
	 * Фильтр. Регистрация организации.
	 */
	JPanel filter_registr_check = new JPanel(new BorderLayout());
	filter_registr_check.add(check, BorderLayout.WEST);
	filter_registr_check.add(reg_box, BorderLayout.EAST);

	/**
	 * Фильтр. Строка с наименованием "Вид деятельности" и компонентом
	 * регистрации организации
	 */
	JPanel filter_registr = new JPanel(new BorderLayout());
	filter_registr.add(new JLabel("Вид деятельности:"), BorderLayout.WEST);
	filter_registr.add(filter_registr_check, BorderLayout.EAST);

	/**
	 * Фильтр. Список "Вид деятельности" с регистрацией организации
	 */
	filter_type_registr = new JPanel(new BorderLayout(5, 5));
	filter_type_registr.setVisible(false);
	filter_type_registr.add(filter_registr, BorderLayout.NORTH);
	filter_type_registr.add(new JScrollPane(list_type_of_org),
		BorderLayout.CENTER);

	/**
	 * Фильтр. Наименование орагинизации + Расриненый поиск(на начальном
	 * этапе скрыт)
	 */
	JPanel filter = new JPanel((new BorderLayout()));
	filter.add(filter_orgainzation, BorderLayout.NORTH);
	filter.add(filter_type_registr, BorderLayout.CENTER);

	/**
	 * Часть 2. Показ организаций.
	 */

	/**
	 * Таблица со списком найденных организаций
	 */

	org_table = new JTable() {

	    private static final long serialVersionUID = 2176392794023662702L;

	    // Запрет на редактирование ячеек
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	org_table.setAutoCreateRowSorter(true);
	org_table.addMouseListener(new MouseAdapter() {

	    /**
	     * Двойной клик мыши по строке. Открытие организации
	     */
	    public void mouseClicked(MouseEvent e) {
		// ждём 2 кликов
		if (e.getClickCount() == 2) {
		    // пользователь сделал 2 клика

		    // получаем инф о выбранной таблице
		    JTable target = (JTable) e.getSource();

		    openOrg(Integer.parseInt(target.getValueAt(
			    target.getSelectedRow(), 0).toString()));

		}
	    }

	    /**
	     * Реализации PopUp окна.
	     */
	    public void mouseReleased(MouseEvent e) {
		// TODO доделать PopUp
		final JTable target = (JTable) e.getSource();

		if (0 < target.getSelectedRows().length && e.isMetaDown()) {

		    JMenuItem title = new JMenuItem(target.getValueAt(
			    target.getSelectedRow(), 2).toString());

		    title.addActionListener(new ListenerPopUpGetTextToBuffer());

		    JMenuItem inn = new JMenuItem(target.getValueAt(
			    target.getSelectedRow(), 1).toString());
		    inn.addActionListener(new ListenerPopUpGetTextToBuffer());

		    JMenuItem open = new JMenuItem("Открыть");
		    open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
			    openOrg(Integer.parseInt(target.getValueAt(
				    target.getSelectedRow(), 0).toString()));
			}
		    });

		    /**
		     * Удаление выделенных организаций
		     */
		    JMenuItem del = new JMenuItem("Удалить");
		    del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			    /**
			     * Защита от случайного нажатия
			     */
			    int quest = JOptionPane
				    .showConfirmDialog(
					    null,
					    "Вы уверены, что хотите удалить организацию?",
					    "Вопрос", JOptionPane.YES_NO_OPTION);

			    if (quest != JOptionPane.YES_OPTION)
				return;

			    /**
			     * Выбран пункт удалить
			     */

			    for (int index : target.getSelectedRows()) {

				/**
				 * id удаляемой организации
				 */
				int id = Integer.parseInt(target.getValueAt(
					index, 0).toString());
				try {

				    /**
				     * Удаляем не только организацию, но и
				     * связанные с ней записи
				     */
				    Connection conn = DriverManager
					    .getConnection("jdbc:sqlite:"
						    + Constant.PATHTODB);
				    Statement stat = conn.createStatement();

				    stat.executeUpdate("DELETE FROM organization WHERE id = '"
					    + id + "';");

				    stat.executeUpdate("DELETE FROM purchase WHERE organization_id = '"
					    + id + "';");

				    stat.executeUpdate("DELETE FROM types_of_org WHERE organization_id = '"
					    + id + "';");

				    stat.executeUpdate("DELETE FROM day_d WHERE organization_id = '"
					    + id + "';");

				    stat.close();
				    conn.close();
				} catch (Exception e) {
				    e.printStackTrace();
				}
			    }

			    try {
				updateTable();
			    } catch (SQLException e) {
				e.printStackTrace();
			    }
			}
		    });
		    
		    JMenuItem num3 = new JMenuItem("Отчет №3");
		    num3.addActionListener(new ActionListener() {
		        
		        @Override
		        public void actionPerformed(ActionEvent arg0) {
		            Vector<Integer> result = new Vector<Integer>();
		            for (int index : target.getSelectedRows()) {

				/**
				 * id удаляемой организации
				 */
				int id = Integer.parseInt(target.getValueAt(
					index, 0).toString());
				result.add(id);
		            }
		            
		            new Number3or4(new Date(System.currentTimeMillis()), 3, result);
		        }
		    });
		    
		    JMenuItem num4 = new JMenuItem("Отчет №4");
		    num4.addActionListener(new ActionListener() {
		        
		        @Override
		        public void actionPerformed(ActionEvent arg0) {
		            Vector<Integer> result = new Vector<Integer>();
		            for (int index : target.getSelectedRows()) {

				/**
				 * id удаляемой организации
				 */
				int id = Integer.parseInt(target.getValueAt(
					index, 0).toString());
				result.add(id);
		            }
		            
		            new Number3or4(new Date(System.currentTimeMillis()), 4, result);
		        }
		    });
		    
		    JMenuItem num5 = new JMenuItem("Отчет №5");
		    num5.addActionListener(new ActionListener() {
		        
		        @Override
		        public void actionPerformed(ActionEvent arg0) {
		            Vector<Integer> result = new Vector<Integer>();
		            for (int index : target.getSelectedRows()) {

				/**
				 * id удаляемой организации
				 */
				int id = Integer.parseInt(target.getValueAt(
					index, 0).toString());
				result.add(id);
		            }
		            
		            new Number5(new Date(System.currentTimeMillis()), result);
		        }
		    });

		    JPopupMenu popup = new JPopupMenu();

		    popup.add(title);
		    popup.add(inn);
		    popup.add(num3);
		    popup.add(num4);
		    popup.add(num5);

		    if (target.getSelectedRows().length == 1) {
			popup.add(open);
		    }

		    popup.add(del);
		    popup.show(e.getComponent(), e.getX(), e.getY());
		}
	    }
	});

	updateTable();

	JPanel grid = new JPanel(new BorderLayout());
	grid.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
	grid.add(new JScrollPane(org_table), BorderLayout.CENTER);

	/**
	 * Часть 3. Системный кнопки
	 */

	/**
	 * Добавление новой организации.<br>
	 * Создаётся пустая организация.
	 */
	JButton btn_add_org = new JButton("Добавить организацию");
	btn_add_org.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		String new_inn = JOptionPane
			.showInputDialog("Введите ИНН новой организации: ");

		if (new_inn != null)
		    new_inn = new_inn.trim();
		else
		    new_inn = "";

		//

		if (new_inn.length() > 0) {

		    try {
			Connection conn = DriverManager
				.getConnection("jdbc:sqlite:"
					+ Constant.PATHTODB);

			Statement stat2 = conn.createStatement();

			ResultSet rs2 = stat2
				.executeQuery("SELECT id FROM organization WHERE inn LIKE '"
					+ new_inn + "'");

			if (rs2.next()) {
			    JOptionPane.showMessageDialog(null,
				    "Организация с таким ИНН уже записана!");
			    return;
			}
			rs2.close();
			stat2.close();

			PreparedStatement pst = conn
				.prepareStatement("INSERT INTO organization VALUES (?, ?, ?, ?, ?, ?);");

			// ИНН
			pst.setString(2, new_inn);
			// Наименование организации
			pst.setString(3, "");
			// Наименование организации для поиска
			pst.setString(4, "");
			// Регистрация
			pst.setString(5, "false");
			// Положение ООС
			pst.setString(6, "");

			pst.addBatch();

			pst.executeBatch();
			pst.close();

			Statement stat = conn.createStatement();

			/**
			 * Получение id новой организации
			 */
			int id_new_org = 0;

			ResultSet get_new_id = stat
				.executeQuery("SELECT last_insert_rowid();");

			if (get_new_id.next())
			    id_new_org = get_new_id.getInt(1);

			get_new_id.close();
			conn.close();

			/**
			 * Переход к редактированию данных новой организации
			 */
			openOrg(id_new_org);
			TableOrg.this.purchase223FZ
				.invertAdditonBtnOrganization();

			/**
			 * Обновление таблицы необходимо, т.к. сам класс
			 * SearchOrg не уничтожается, а скрывается!
			 */
			updateTable();

		    } catch (Exception e) {
			e.printStackTrace();
		    }

		}
	    }
	});

	JButton btn_settings = new JButton("Настройки");
	btn_settings.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		openSettings();
	    }
	});
	
	JButton btn_sta = new JButton("Статистика");
	btn_sta.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    purchase223FZ.setPanelStatistics();
		} catch (SQLException e1) {
		    e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
		    e1.printStackTrace();
		}
	    }
	});

	JPanel buttons = new JPanel(new GridLayout(1, 3));
	buttons.add(btn_settings);
	buttons.add(btn_sta);
	buttons.add(btn_add_org);

	this.setLayout(new BorderLayout());
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	this.add(filter, BorderLayout.NORTH);
	this.add(grid, BorderLayout.CENTER);
	this.add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Обновление данных в таблице со списком организации с учетом фильтра.<br>
     * Фильтраци производится по:
     * <ul>
     * <li>Наименованию организации;</li>
     * <li>ИНН;</li>
     * <li>Виду деятельности организации;</li>
     * <li>Зарегистрирована или не зарегистрирована орг.</li>
     * </ul>
     * 
     * @throws SQLException
     */
    public void updateTable() throws SQLException {
	
	/**
	 * Настройки таблицы
	 */
	Vector<String> header = new Vector<String>();
	header.add("id");
	header.add("ИНН");
	header.add("Наименование организации");

	DefaultTableModel dtm = (DefaultTableModel) org_table.getModel();
	dtm.setDataVector(getValues(), header);

	TableColumnModel colModel = org_table.getColumnModel();

	/**
	 * Скрытие колонки с id, т.к. нафиг её не нужно видеть пользователю
	 */
	colModel.getColumn(0).setMaxWidth(0);
	colModel.getColumn(0).setMinWidth(0);
	colModel.getColumn(0).setPreferredWidth(0);

	/**
	 * Настройка ширины колонки с ИНН
	 */
	colModel.getColumn(1).setMaxWidth(250);
	colModel.getColumn(1).setMinWidth(90);

    }
    
    public Vector<Vector<String>> getValues() throws SQLException{
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	/**
	 * Хранит данные, для внесения в таблицу
	 */
	Vector<Vector<String>> values = new Vector<Vector<String>>();

	/**
	 * Фильтрации по ИНН и наименованию организации
	 */
	/**
	 * sql запрос списка организаций
	 */
	String sqlsearch = "SELECT id, inn, name, regist FROM organization ";

	/**
	 * Добавление к запросу параметров фильтрации по ИНН и наименованию
	 */
	String[] result = jSearchTextField.getText().split(" ");
	if (result.length > 0) {
	    for (int i = 0; i < result.length; i++) {
		if (i == 0)
		    sqlsearch += " WHERE ";
		else
		    sqlsearch += " AND ";

		sqlsearch += "(inn LIKE '%" + result[i].toLowerCase()
			+ "%' OR name_low LIKE '%" + result[i].toLowerCase()
			+ "%')";
	    }
	}

	Statement stat_org = conn.createStatement();
	ResultSet rs_org = stat_org.executeQuery(sqlsearch);

	NEXT_ORG: while (rs_org.next()) {

	    /**
	     * Фильтрацию по регистрации организации
	     */
	    if (check.isSelected()) {
		boolean current_value_org = Boolean.parseBoolean(rs_org
			.getString(("regist")));

		ComboItemBooleanValue search_value = (ComboItemBooleanValue) reg_box
			.getSelectedItem();

		if (search_value.getValue() != current_value_org)
		    continue NEXT_ORG;
	    }

	    /**
	     * Фильтрация по виду деятельности
	     */
	    Map<Integer, Boolean> res = new HashMap<Integer, Boolean>();

	    Statement stat_check = conn.createStatement();
	    ResultSet rs_check = stat_check
		    .executeQuery("SELECT type_of_org_id FROM types_of_org WHERE organization_id LIKE '"
			    + rs_org.getString("id") + "'");

	    while (rs_check.next())
		res.put(rs_check.getInt("type_of_org_id"), false);

	    rs_check.close();
	    stat_check.close();

	    for (CheckListItem check_el : checklist)
		if (check_el.isSelected())
		    if (res.get(check_el.getValue()) == null)
			continue NEXT_ORG;

	    /**
	     * Организцаия полностью удовлетворяет запросу пользователя
	     */
	    Vector<String> el = new Vector<String>();
	    el.add(rs_org.getString("id"));
	    el.add(rs_org.getString("inn"));
	    el.add(rs_org.getString("name"));
	    values.add(el);
	}

	rs_org.close();
	stat_org.close();
	conn.close();
	
	return values;
    }

    /**
     * Переход к просмотру анкеты организации.
     * 
     * @param id
     *            - id организации
     */
    private void openOrg(int id) {
	try {
	    this.purchase223FZ.setPanelOrganization(id);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Переход к настройкам БД
     */
    private void openSettings() {
	this.purchase223FZ.setPanelSettings();
    }
    
    private class ListenerPopUpGetTextToBuffer implements ActionListener{

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    String str = ((JMenuItem)e.getSource()).getText();
	    Transferable data = new StringSelection(str);
	    try {
		Clipboard c = java.awt.Toolkit
			.getDefaultToolkit()
			.getSystemClipboard();
		c.setContents(data, null);
	    } catch (Throwable t) {
	    }
	}
	
    }
}
