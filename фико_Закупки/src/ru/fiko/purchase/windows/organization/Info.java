package ru.fiko.purchase.windows.organization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ru.fiko.purchase.main.Purchases223FZ;
import ru.fiko.purchase.supports.CheckListItem;
import ru.fiko.purchase.supports.CheckListRenderer;
import ru.fiko.purchase.supports.ComboItemBooleanValue;
import ru.fiko.purchase.windows.Organization;

public class Info extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6635703574519008448L;

    /**
     * Выбранная организия
     */
    private Organization org;

    /**
     * Компонент профиля. Строка c наименования организации.
     */
    private JTextArea name;

    /**
     * Компонент профиля. Строка с ИНН организации.
     */
    private JTextField inn;

    /**
     * Компонент профиля. Состояние регистрации организации.
     */
    private JComboBox reg_box = new JComboBox(Purchases223FZ.registr_items);

    /**
     * Компонент профиля. Строка с положением о закупке по ООС.
     */
    private JTextField polojen_ooc;

    /**
     * Виды деятельности организации
     */
    private Vector<CheckListItem> list_types_of_org;

    /**
     * Компонент профиля. Ссылка на панель профиля организации со скрытыми
     * данными
     */
    private JPanel profile_hidden;

    private JButton save;

    /**
     * Формирование панели с данными о организации.<br>
     * Получение данных о организации из БД и построение компонентов для
     * отображения и последующим изменением этих данных.
     * 
     * @param parent
     *            - панель данных организации
     * @throws SQLException
     */
    public Info(Organization parent) throws SQLException {
	super(new BorderLayout());

	this.org = parent;

	getInfoFromBD();

	/**
	 * Сборка профиля организации
	 */
	this.add(main(), BorderLayout.NORTH);
	this.add(hidden(), BorderLayout.CENTER);
    }

    /**
     * Получение данных о организации из базы. Данные сохраняются в компоненты
     * интерфейса.
     * <ul>
     * <li>Наименование организации</li>
     * <li>ИНН организации</li>
     * <li>Положения о закупках на ООС</li>
     * <li>Регистрация на ООС</li>
     * <li>Вид деятельности</li>
     * </ul>
     */
    private void getInfoFromBD() throws SQLException {

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchases223FZ.PATHTODB);

	Statement stat = conn.createStatement();
	ResultSet rs_org = stat
		.executeQuery("SELECT * FROM organization WHERE id LIKE '"
			+ this.org.getId() + "'");

	if (rs_org.next()) {

	    // ИНН организации
	    inn = new JTextField(rs_org.getString("inn"));

	    // Наименование организации
	    name = new JTextArea(rs_org.getString("name"));

	    // Положение о закупке на ООС
	    polojen_ooc = new JTextField(rs_org.getString("polojen_ooc"));

	    // Состояние регистрации организации
	    if (rs_org.getString("regist").equals("true"))
		reg_box.setSelectedItem(Purchases223FZ.registr_items[0]);
	    else
		reg_box.setSelectedItem(Purchases223FZ.registr_items[1]);

	} else {
	    /**
	     * Организация не найдена, возврат к панели поиска организации
	     */
	    JOptionPane.showMessageDialog(null, "Организация не найдена");
	    this.org.backToSearchOrg();
	}

	/**
	 * Формирование списка видов деятельности
	 */
	list_types_of_org = new Vector<CheckListItem>();
	ResultSet rs_type_of_org = stat
		.executeQuery("SELECT id, title FROM type_of_org");

	while (rs_type_of_org.next())
	    list_types_of_org.add(new CheckListItem(rs_type_of_org
		    .getString("title"), rs_type_of_org.getInt("id")));

	rs_type_of_org.close();

	/**
	 * Установка значеней видов деятельности организации
	 */
	ResultSet rs_types_of_org = stat
		.executeQuery("SELECT type_of_org_id FROM types_of_org WHERE organization_id LIKE '"
			+ this.org.getId() + "'");

	while (rs_types_of_org.next())
	    for (CheckListItem item : list_types_of_org)
		if (item.getValue() == rs_types_of_org.getInt("type_of_org_id"))
		    item.setSelected(true);

	rs_types_of_org.close();

	rs_org.close();
	stat.close();
	conn.close();
    }

    /**
     * Формирование панели с данными о организации.
     * 
     * @return profile - панель с данные о организации
     * @throws SQLException
     */
    private JPanel main() throws SQLException {

	/**
	 * Строка для ввода наименования организации
	 */
	name.setToolTipText("Наименование организации");
	name.setRows(2);
	name.setLineWrap(true);
	name.setWrapStyleWord(true);
	name.setBackground(new Color(177, 222, 255));
	name.setBackground(new Color(226, 243, 255));
	// name.setBackground(new Color(242, 241, 240));
	name.addKeyListener(new ListenerSaveEnabled());
	// name.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	/**
	 * Кнопка раскрытия/скрытие дополнительных данных
	 */
	JButton btn_addition = new JButton("  +  ");
	btn_addition.setToolTipText("Дополнительные данные");
	btn_addition.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		ivertAdditionBtn();
	    }
	});

	/**
	 * Основные компоненты организации
	 */
	JPanel profile_main = new JPanel(new BorderLayout(5, 5));
	// profile_main.add(new JLabel(org_name.getText(), JLabel.CENTER),
	// BorderLayout.CENTER);
	profile_main.add(name, BorderLayout.CENTER);
	profile_main.add(btn_addition, BorderLayout.EAST);

	return profile_main;
    }

    /**
     * Скрытые данные организации.<br>
     * Скрыты, т.к. занимают внушительное пространство окна.
     */
    private JPanel hidden() {
	/**
	 * Строка с ИНН
	 */
	inn.setToolTipText("ИНН организации");
	inn.addKeyListener(new ListenerSaveEnabled());

	JPanel profile_hidden_inn = new JPanel(new BorderLayout());
	profile_hidden_inn.add(new JLabel("ИНН: "), BorderLayout.WEST);
	profile_hidden_inn.add(inn, BorderLayout.CENTER);

	/**
	 * Регистрация организации
	 */
	reg_box.setToolTipText("Регистрация на ООС");
	reg_box.addActionListener(new ListenerSaveEnabled());

	JPanel profile_hidden_regist = new JPanel(new BorderLayout());
	profile_hidden_regist.add(reg_box, BorderLayout.EAST);

	/**
	 * Вид деятельности
	 */

	/**
	 * Настройки компонента:<br>
	 * - 5 строк<br>
	 * - измененное повидение ячеек + иверсия значений при клике
	 */
	JList list_type_of_org = new JList(list_types_of_org);

	list_type_of_org.setCellRenderer(new CheckListRenderer());
	list_type_of_org.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	list_type_of_org.setVisibleRowCount(5);
	list_type_of_org.setToolTipText("Вид деятельности организации");
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
	    }
	});

	list_type_of_org.addMouseListener(new ListenerSaveEnabled());

	/**
	 * Положения о закупках на ООС
	 */
	polojen_ooc.setToolTipText("Опубликование положения о закупках на ООС");
	polojen_ooc.addKeyListener(new ListenerSaveEnabled());

	/**
	 * Сборка панели с ИНН и регистрацией
	 */
	JPanel profile_hidden_inn_regist = new JPanel(new GridLayout(1, 2));
	profile_hidden_inn_regist.add(profile_hidden_inn);
	profile_hidden_inn_regist.add(profile_hidden_regist);

	save = new JButton("Сохранить изменения");
	save.setEnabled(false);
	save.addActionListener(new ListenerSaveButton());

	/**
	 * Сборка положения о закупках и кнопки save
	 */
	JPanel p_polojen_save = new JPanel(new BorderLayout(5, 5));
	p_polojen_save.add(polojen_ooc, BorderLayout.CENTER);
	p_polojen_save.add(save, BorderLayout.EAST);

	/**
	 * Сборка скрытой панели
	 */
	profile_hidden = new JPanel(new BorderLayout(5, 5));
	profile_hidden.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
	profile_hidden.add(profile_hidden_inn_regist, BorderLayout.NORTH);
	profile_hidden.add(new JScrollPane(list_type_of_org),
		BorderLayout.CENTER);
	profile_hidden.add(p_polojen_save, BorderLayout.SOUTH);

	profile_hidden.setVisible(false);

	return profile_hidden;
    }

    /**
     * Деййствие кнопки "Сохранить измения". Пересохраняет данные о организации,
     * старые значения удалются!
     * 
     * @author kirill
     * 
     */
    private class ListenerSaveButton implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		save_Org_Info();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    JButton btn = (JButton) e.getSource();
	    btn.setEnabled(false);
	}
    }

    private class ListenerSaveEnabled implements KeyListener, ActionListener,
	    MouseListener {

	private void action() {
	    save.setEnabled(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    action();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	    action();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	    action();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    action();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	    action();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
    }

    /**
     * Обновление данных организации.
     * <ul>
     * <li>Наименование организации</li>
     * <li>Наименование организации для поиска</li>
     * <li>ИНН организации</li>
     * <li>положения о закупках на ООС</li>
     * <li>Регистрация на ООС</li>
     * <li>Вид деятельности</li>
     * </ul>
     * 
     * @throws SQLException
     */
    public void save_Org_Info() throws SQLException {

	boolean regist = ((ComboItemBooleanValue) reg_box.getSelectedItem())
		.getValue();

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchases223FZ.PATHTODB);

	Statement stat = conn.createStatement();

	// Наименование организации
	stat.executeUpdate("UPDATE organization SET name = '" + name.getText()
		+ "' WHERE id LIKE '" + this.org.getId() + "';");

	// Наименование организации для поиска
	stat.executeUpdate("UPDATE organization SET name_low = '"
		+ name.getText().toLowerCase() + "' WHERE id LIKE '"
		+ this.org.getId() + "';");

	// ИНН организации
	stat.executeUpdate("UPDATE organization SET inn = '" + inn.getText()
		+ "' WHERE id LIKE '" + this.org.getId() + "';");

	// положения о закупках на ООС
	stat.executeUpdate("UPDATE organization SET polojen_ooc = '"
		+ polojen_ooc.getText() + "' WHERE id LIKE '"
		+ this.org.getId() + "';");

	// Регистрация на ООС
	stat.executeUpdate("UPDATE organization SET regist = '" + regist
		+ "' WHERE id LIKE '" + this.org.getId() + "';");

	/**
	 * Старые значения видов организация
	 */
	Map<Integer, Integer> types_old = new HashMap<Integer, Integer>();
	/**
	 * Новые значения видов организации для сравнения со старыми
	 */
	Vector<Integer> types_new = new Vector<Integer>();
	/**
	 * Новые значения видов организации, которые будут вносится в бд
	 */
	Vector<Integer> types_new_finish = new Vector<Integer>();

	/**
	 * Новые значения "Видов деятельности"
	 */
	for (CheckListItem check_el : list_types_of_org)
	    if (check_el.isSelected())
		types_new.add(check_el.getValue());

	types_new_finish.addAll(types_new);

	/**
	 * Старые значения "Видов деятельности"
	 */

	ResultSet rs_types = stat
		.executeQuery("SELECT id, type_of_org_id FROM types_of_org WHERE organization_id LIKE '"
			+ this.org.getId() + "';");

	while (rs_types.next())
	    types_old.put(rs_types.getInt("type_of_org_id"),
		    rs_types.getInt("id"));

	rs_types.close();

	/**
	 * Нахождение пересечения
	 */
	for (Integer item : types_new)
	    if (types_old.containsValue(item)) {
		types_old.remove(item);
		types_new_finish.remove(item);
	    }

	/**
	 * Удаление не принадлежаших данной организаций видов деаятельности
	 */
	for (Map.Entry<Integer, Integer> entry : types_old.entrySet()) {
	    stat.executeUpdate("DELETE FROM types_of_org WHERE id = '"
		    + entry.getValue() + "';");
	}

	/**
	 * Добавление новых видов деятельности к организации
	 */
	for (Integer item : types_new_finish) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO types_of_org VALUES (?, ?, ?);");

	    pst.setInt(2, item);
	    pst.setInt(3, this.org.getId());
	    pst.addBatch();

	    pst.executeBatch();
	    pst.close();
	}

	stat.close();
	conn.close();

	this.org.getZakon223_FZ().updateSearchOrgTable();
    }

    /**
     * Открытие/Скрытие панели с дополнительной информацией о организации.
     */
    public void ivertAdditionBtn() {
	if (profile_hidden.isVisible())
	    profile_hidden.setVisible(false);
	else
	    profile_hidden.setVisible(true);

    }
}
