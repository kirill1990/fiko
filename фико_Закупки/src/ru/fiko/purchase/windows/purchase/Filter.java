package ru.fiko.purchase.windows.purchase;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.supports.ComboItemIntValue;
import ru.fiko.purchase.windows.organization.Organization;

import com.toedter.calendar.JDateChooser;

public class Filter extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1055332608374064585L;

    /**
     * Выбранная организия
     */
    private Organization org;

    /**
     * Компонент фильтра. Ссылка на панель с расширенным поиском закупки
     */
    private JPanel filter_hidden;

    /**
     * Компонент фильтра. Кнопка раскрытия/скрытие расширенного поиска.
     */
    private JTextField filter_subject_title;

    /*
     * Компонент фильтра. Разрешение поиска по статусу закупки.
     */
    private JCheckBox filter_check_status;

    /**
     * Компонент фильтра. Разрешение поиска по способу размещения закупки.
     */
    private JCheckBox filter_check_aspect;

    /**
     * Компонент фильтра. Разрешение поиска по предмету закупки
     */
    private JCheckBox filter_check_subject;

    /**
     * Компонент фильтра. Разрешение поиска по классификации закупки
     */
    private JCheckBox filter_check_type;

    /**
     * Компонент фильтра. Разрешение поиска по дате закупки
     */
    private JCheckBox filter_check_date;

    /**
     * Список со состоянием закупки
     */
    private JComboBox filter_status = new JComboBox(Constant.status_items);

    /**
     * Список "Способ размещения заказа"
     */
    private JComboBox filter_aspect;

    /**
     * Список "Предмет закупки"
     */
    private JComboBox filter_subject;

    /**
     * Список "Классификация закупки"
     */
    private JComboBox filter_type;

    /**
     * Дата с которой начинается поиск закупки(с 00:00:00)
     */
    private JDateChooser date_from;

    /**
     * Дата по которую заканчивается поиск закупки(до 23:59:59)
     */
    private JDateChooser date_to;

    /**
     * Формирование панели с элементами фильтрации закупок.
     * 
     * @param organization
     *            - панель данных организации
     * @throws SQLException
     */
    public Filter(Organization organization) throws SQLException {
	this.org = organization;

	filter();
    }

    /**
     * Формирование панели поиска закупки.
     * 
     * @return filter - панель поиска закупки
     * @throws SQLException
     */
    private void filter() throws SQLException {

	/**
	 * Получение список закупок из бд.<br>
	 * - Способ размещения заказа.<br>
	 * - Предмет закупки.<br>
	 * - Классификация закупки.<br>
	 */

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	Statement stat = conn.createStatement();

	/**
	 * Формирование списка "Способ размещения заказа"
	 */
	Vector<ComboItemIntValue> list_aspect = new Vector<ComboItemIntValue>();

	ResultSet rs_aspect = stat.executeQuery("SELECT id, title FROM aspect");

	while (rs_aspect.next())
	    list_aspect.add(new ComboItemIntValue(rs_aspect.getInt("id"),
		    rs_aspect.getString("title")));

	rs_aspect.close();

	/**
	 * Формирование списка "Предмет закупки"
	 */
	Vector<ComboItemIntValue> list_subject = new Vector<ComboItemIntValue>();

	ResultSet rs_subject = stat
		.executeQuery("SELECT id, title FROM subject");

	while (rs_subject.next())
	    list_subject.add(new ComboItemIntValue(rs_subject.getInt("id"),
		    rs_subject.getString("title")));

	rs_subject.close();

	/**
	 * Формирование списка "Классификация закупки"
	 */
	Vector<ComboItemIntValue> list_type = new Vector<ComboItemIntValue>();

	ResultSet rs_type = stat.executeQuery("SELECT id, title FROM type");

	while (rs_type.next())
	    list_type.add(new ComboItemIntValue(rs_type.getInt("id"), rs_type
		    .getString("title")));

	rs_type.close();

	stat.close();
	conn.close();

	/**
	 * Основная часть. Строка поиска и кнопка расширенного поиска.
	 */

	filter_subject_title = new JTextField("");
	filter_subject_title
		.setToolTipText("Поиск по № заказа и наименованию закупки ");
	filter_subject_title.addKeyListener(new ListenerUpdatePurchaseTable());

	/**
	 * Кнопка раскрытия/скрытие расширенного поиска
	 */
	JButton filter_btn_addition = new JButton("Расширенный поиск");
	filter_btn_addition.setToolTipText("Расширенный поиск");
	filter_btn_addition.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		ivertFilterBtn();
	    }
	});

	/**
	 * Основные компоненты поиска
	 */
	JPanel filter_main = new JPanel(new BorderLayout());
	filter_main.add(filter_subject_title, BorderLayout.CENTER);
	filter_main.add(filter_btn_addition, BorderLayout.EAST);

	/**
	 * Расширенный поиск
	 */

	/*
	 * Статус
	 */
	filter_check_status = new JCheckBox("Статус: ");
	filter_check_status
		.addActionListener(new ListenerUpdatePurchaseTable());
	filter_status.addActionListener(new ListenerUpdatePurchaseTable());

	/**
	 * Сборка статуса
	 */
	JPanel filter_panel_status = new JPanel(new BorderLayout());
	filter_panel_status.add(filter_check_status, BorderLayout.WEST);
	filter_panel_status.add(filter_status, BorderLayout.CENTER);

	/**
	 * Дата
	 */
	filter_check_date = new JCheckBox("Дата:");
	filter_check_date.addActionListener(new ListenerUpdatePurchaseTable());

	/**
	 * Дата С
	 */
	date_from = new JDateChooser(new Date(System.currentTimeMillis()));
	date_from.addPropertyChangeListener(new ListenerUpdatePurchaseTable());

	/**
	 * Дата ПО
	 */
	date_to = new JDateChooser(new Date(System.currentTimeMillis()));
	date_to.addPropertyChangeListener(new ListenerUpdatePurchaseTable());

	/**
	 * Сборка даты с dd.MM.yyyy
	 */
	JPanel filter_panel_date_from = new JPanel(new BorderLayout());
	filter_panel_date_from.add(new JLabel("  c  "), BorderLayout.WEST);
	filter_panel_date_from.add(date_from, BorderLayout.CENTER);

	/**
	 * Сборка даты по dd.MM.yyyy
	 */
	JPanel filter_panel_date_to = new JPanel(new BorderLayout());
	filter_panel_date_to.add(new JLabel("   по  "), BorderLayout.WEST);
	filter_panel_date_to.add(date_to, BorderLayout.CENTER);

	/**
	 * Сборка даты с dd.MM.yyyy по dd.MM.yyyy
	 */
	JPanel filter_panel_date_from_to = new JPanel(new GridLayout(1, 2));
	filter_panel_date_from_to.add(filter_panel_date_from);
	filter_panel_date_from_to.add(filter_panel_date_to);

	/**
	 * Сборка даты
	 */
	JPanel filter_panel_date = new JPanel(new BorderLayout());
	filter_panel_date.add(filter_check_date, BorderLayout.WEST);
	filter_panel_date.add(filter_panel_date_from_to, BorderLayout.CENTER);

	/**
	 * Способ размещения заказа
	 */
	filter_check_aspect = new JCheckBox("Способ размещения заказа: ");
	filter_check_aspect
		.addActionListener(new ListenerUpdatePurchaseTable());
	filter_aspect = new JComboBox(list_aspect);
	filter_aspect.addActionListener(new ListenerUpdatePurchaseTable());

	/**
	 * Предмет закупки
	 */
	filter_check_subject = new JCheckBox("Предмет закупки: ");
	filter_check_subject
		.addActionListener(new ListenerUpdatePurchaseTable());
	filter_subject = new JComboBox(list_subject);
	filter_subject.addActionListener(new ListenerUpdatePurchaseTable());

	/**
	 * Классификация закупки
	 */
	filter_check_type = new JCheckBox("Классификация закупки: ");
	filter_check_type.addActionListener(new ListenerUpdatePurchaseTable());
	filter_type = new JComboBox(list_type);
	filter_type.addActionListener(new ListenerUpdatePurchaseTable());

	/**
	 * Сборка панели с флагами по которым будет осуществляться фильтрация
	 * закупок
	 */
	JPanel filter_panel_check = new JPanel(new GridLayout(4, 1));
	filter_panel_check.add(filter_panel_status);
	filter_panel_check.add(filter_check_aspect);
	filter_panel_check.add(filter_check_subject);
	filter_panel_check.add(filter_check_type);

	/**
	 * Сборка компонентов по которым будет осуществляться фильтрация закупок
	 */
	JPanel filter_panel_items = new JPanel(new GridLayout(4, 1));
	filter_panel_items.add(filter_panel_date);
	filter_panel_items.add(filter_aspect);
	filter_panel_items.add(filter_subject);
	filter_panel_items.add(filter_type);

	/**
	 * Сборка расширенного поиска
	 */
	filter_hidden = new JPanel(new BorderLayout());
	filter_hidden.add(filter_panel_check, BorderLayout.WEST);
	filter_hidden.add(filter_panel_items, BorderLayout.CENTER);

	/**
	 * Сборка
	 */
	this.setLayout(new BorderLayout(5, 5));
	this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
	this.add(filter_main, BorderLayout.NORTH);
	this.add(filter_hidden, BorderLayout.CENTER);
	filter_hidden.setVisible(false);
    }

    /**
     * Открытие/Скрытие панели с расширенным поиском закупки.
     */
    public void ivertFilterBtn() {
	if (filter_hidden.isVisible())
	    filter_hidden.setVisible(false);
	else
	    filter_hidden.setVisible(true);
    }

    /**
     * Обновление данных в таблице закупок организации с учетом фильтров.
     * 
     * @throws SQLException
     */
    public void updatePurchaseTable() throws SQLException {
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	/**
	 * Хранит данные, для внесения в таблицу
	 */
	Vector<Vector<String>> values = new Vector<Vector<String>>();

	/**
	 * Фильтрации по Номеру и Наименованию закупки
	 */
	/**
	 * sql запрос списка закупок
	 */
	String sqlsearch = "SELECT * FROM purchase WHERE organization_id LIKE '"
		+ this.org.getId() + "' ";

	/**
	 * Добавление к запросу параметров фильтрации по ИНН и наименованию
	 */
	String[] result = filter_subject_title.getText().split(" ");
	if (result.length > 0) {
	    for (int i = 0; i < result.length; i++) {
		sqlsearch += " AND ";

		sqlsearch += "(number LIKE '%" + result[i].toLowerCase()
			+ "%' OR subject_title_low LIKE '%"
			+ result[i].toLowerCase() + "%')";
	    }
	}

	Statement stat_purchase = conn.createStatement();
	ResultSet rs_purchase = stat_purchase.executeQuery(sqlsearch);

	NEXT: while (rs_purchase.next()) {

	    /**
	     * Фильтрация по способу размещения заказа
	     */
	    if (filter_check_aspect.isSelected()) {
		ComboItemIntValue item = (ComboItemIntValue) filter_aspect
			.getSelectedItem();

		if (item.getValue() != rs_purchase.getInt("aspect_id"))
		    continue NEXT;
	    }

	    /**
	     * Фильтрация по состоянию закупки
	     */
	    if (filter_check_status.isSelected()) {
		ComboItemIntValue item = (ComboItemIntValue) filter_status
			.getSelectedItem();

		if (item.getValue() != rs_purchase.getInt("status"))
		    continue NEXT;
	    }

	    /**
	     * Фильтрация по предмету закупки
	     */
	    if (filter_check_subject.isSelected()) {
		ComboItemIntValue item = (ComboItemIntValue) filter_subject
			.getSelectedItem();

		if (item.getValue() != rs_purchase.getInt("subject_id"))
		    continue NEXT;
	    }

	    /**
	     * Фильтрация по классификации закупки
	     */
	    if (filter_check_type.isSelected()) {
		ComboItemIntValue item = (ComboItemIntValue) filter_type
			.getSelectedItem();

		if (item.getValue() != rs_purchase.getInt("type_id"))
		    continue NEXT;
	    }

	    /**
	     * Фильтрация по дате
	     */
	    if (filter_check_date.isSelected()) {
		Long date = Long.parseLong(rs_purchase.getString("date"));

		if (date < date_from.getDate().getTime()
			|| date_to.getDate().getTime() < date)
		    continue NEXT;
	    }

	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
	    Date date = new Date(Long.parseLong(rs_purchase.getString("date")));

	    Vector<String> el = new Vector<String>();
	    el.add(rs_purchase.getString("id"));
	    el.add(dateFormat.format(date).toString());
	    el.add(rs_purchase.getString("number"));
	    el.add(rs_purchase.getString("subject_title"));
	    values.add(0, el);
	}

	rs_purchase.close();
	stat_purchase.close();
	conn.close();

	/**
	 * Настройки таблицы
	 */

	/**
	 * Шапка таблицы закупок
	 */
	Vector<String> header = new Vector<String>();
	header.add("id");
	header.add("Дата");
	header.add("Номер закупки");
	header.add("Наименование закупки");

	if (this.org.getPurchase() == null)
	    return;

	DefaultTableModel dtm = (DefaultTableModel) this.org.getPurchase()
		.getPurchase_table().getModel();
	dtm.setDataVector(values, header);

	TableColumnModel colModel = this.org.getPurchase().getPurchase_table()
		.getColumnModel();

	/**
	 * Скрытие колонки с id, т.к. нафиг её не нужно видеть пользователю
	 */
	colModel.getColumn(0).setMaxWidth(0);
	colModel.getColumn(0).setMinWidth(0);
	colModel.getColumn(0).setPreferredWidth(0);

	/**
	 * Настройка ширины колонки с датой
	 */
	colModel.getColumn(1).setMaxWidth(300);
	colModel.getColumn(1).setMinWidth(90);
	
	/**
	 * Номер закупки
	 */
	colModel.getColumn(2).setMaxWidth(300);
	colModel.getColumn(2).setMinWidth(130);
    }

    /**
     * Вызывает обновление данных в таблице с закупками, после изменения
     * значения любого из фильтров.
     * 
     * @author kirill
     * 
     */
    class ListenerUpdatePurchaseTable implements ActionListener, KeyListener,
	    PropertyChangeListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    update();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    update();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    update();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
	    update();
	}

	private void update() {
	    try {
		updatePurchaseTable();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}
    }
}
