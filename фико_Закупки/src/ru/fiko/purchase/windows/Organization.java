package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ru.fiko.purchase.main.Zakon223_FZ;
import ru.fiko.purchase.supports.CheckListItem;
import ru.fiko.purchase.supports.CheckListRenderer;

public class Organization extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4404715103010867587L;

    private Zakon223_FZ purchase;
    private int id;

    private JPanel profile;
    private JPanel profile_hidden;

    private Vector<CheckListItem> checklist;

    /**
     * @param parent
     * @param id
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Organization(Zakon223_FZ parent, int id)
	    throws ClassNotFoundException, SQLException {

	Class.forName("org.sqlite.JDBC");

	this.purchase = parent;
	this.id = id;

	/**
	 * Получение данных о организации
	 */
	String inn = "";
	String name = "";
	String regist = "";
	String polojen_ooc = "";

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Zakon223_FZ.PATHTODB);

	Statement stat = conn.createStatement();
	ResultSet rs_org = stat
		.executeQuery("SELECT * FROM organization WHERE id LIKE '"
			+ this.id + "'");

	if (rs_org.next()) {
	    inn = rs_org.getString("inn");
	    name = rs_org.getString("name");
	    regist = rs_org.getString("regist");
	    polojen_ooc = rs_org.getString("polojen_ooc");
	} else {
	    // TODO кнопка "вернуться назад"
	}

	rs_org.close();

	/**
	 * Профиль организации
	 */

	/**
	 * Строка для ввода наименования организации
	 */
	JTextField c_name = new JTextField(name);

	/**
	 * Кнопка раскрытия/скрытие дополнительных данных
	 */
	JButton btn = new JButton("  +  ");
	btn.setToolTipText("Дополнительные данные");
	btn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		JButton btn = (JButton) arg0.getSource();

		if (btn.getText().equals("  +  ")) {
		    profile.add(profile_hidden);
		    btn.setText("  -  ");
		} else {
		    profile.remove(profile_hidden);
		    btn.setText("  +  ");
		}
	    }
	});

	/**
	 * Основные компоненты организации
	 */
	JPanel profile_main = new JPanel(new BorderLayout());
	profile_main.add(c_name, BorderLayout.CENTER);
	profile_main.add(btn, BorderLayout.EAST);

	/**
	 * Скрытые данные организации.<br>
	 * Скрыты, т.к. занимают внушительное пространство окна.
	 */

	/**
	 * Строка с ИНН
	 */
	JTextField c_inn = new JTextField(inn);

	JPanel profile_hidden_inn = new JPanel(new BorderLayout());
	profile_hidden_inn.add(new JLabel("ИНН:"), BorderLayout.WEST);
	profile_hidden_inn.add(c_inn, BorderLayout.CENTER);

	/**
	 * Регистрация организации
	 */

	JComboBox reg_box = new JComboBox(Zakon223_FZ.registr_items);
	if (regist.equals("true"))
	    reg_box.setSelectedItem(Zakon223_FZ.registr_items[0]);
	else
	    reg_box.setSelectedItem(Zakon223_FZ.registr_items[1]);

	JPanel profile_hidden_regist = new JPanel(new BorderLayout());
	profile_hidden_regist.add(reg_box, BorderLayout.EAST);

	/**
	 * Вид деятельности
	 */

	/**
	 * Формирование списка
	 */
	checklist = new Vector<CheckListItem>();
	ResultSet rs_type_of_org = stat
		.executeQuery("SELECT id, title FROM type_of_org");

	while (rs_type_of_org.next())
	    checklist.add(new CheckListItem(rs_type_of_org.getString("title"),
		    rs_type_of_org.getInt("id")));

	rs_type_of_org.close();

	/**
	 * Установка значеней видов деятельности организации
	 */
	ResultSet rs_types_of_org = stat
		.executeQuery("SELECT type_of_org_id FROM types_of_org WHERE organization_id LIKE '"
			+ this.id + "'");

	while (rs_types_of_org.next())
	    for (CheckListItem item : checklist)
		if (item.getValue() == rs_types_of_org.getInt("type_of_org_id"))
		    item.setSelected(true);

	rs_types_of_org.close();

	/**
	 * Настройки компонента:<br>
	 * - 5 строк<br>
	 * - измененное повидение ячеек + иверсия значений при клике
	 */
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
	    }
	});

	/**
	 * Положения о закупках на ООС
	 */

	JTextField area = new JTextField(polojen_ooc);

	/**
	 * Сборка панели с ИНН и регистрацией
	 */
	JPanel profile_hidden_inn_regist = new JPanel(new GridLayout(1, 2));
	profile_hidden_inn_regist.add(profile_hidden_inn);
	profile_hidden_inn_regist.add(profile_hidden_regist);

	profile_hidden = new JPanel(new BorderLayout(5,5));
	profile_hidden.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
	profile_hidden.add(profile_hidden_inn_regist, BorderLayout.NORTH);
	profile_hidden.add(new JScrollPane(list_type_of_org),
		BorderLayout.CENTER);
	profile_hidden.add(area, BorderLayout.SOUTH);

	profile = new JPanel(new BorderLayout());
	profile.add(profile_main, BorderLayout.NORTH);

	/**
	 * Управляющие кнопки
	 */

	JButton btn_prev = new JButton("Назад");
	btn_prev.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    purchase.setPanelSearchOrg();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});

	JPanel foot = new JPanel(new BorderLayout());
	foot.add(btn_prev);

	this.setLayout(new BorderLayout());
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	this.add(profile, BorderLayout.NORTH);
	this.add(foot, BorderLayout.SOUTH);

	stat.close();
	conn.close();
    }

    public int getId() {
	return id;
    }

}
