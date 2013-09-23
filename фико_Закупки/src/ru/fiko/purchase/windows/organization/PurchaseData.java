package ru.fiko.purchase.windows.organization;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.fiko.purchase.Main;
import ru.fiko.purchase.supports.ComboItemBooleanValue;
import ru.fiko.purchase.supports.ComboItemIntValue;

import com.toedter.calendar.JDateChooser;

public class PurchaseData extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7474142126764145274L;

    /**
     * ID закупки
     */
    private int purchase_id;

    /**
     * Наименование закупки
     */
    private JTextField subject_title;

    /**
     * Номер закупки
     */
    private JTextField number;

    /**
     * Статус закупки
     */
    private JComboBox status;

    /**
     * Дата совершения закупки
     */
    private JDateChooser date;

    /**
     * Способ размещения заказа
     */
    private JComboBox aspect;

    /**
     * Предмет закупки
     */
    private JComboBox subject;

    /**
     * Классификация закупки
     */
    private JComboBox type;

    /**
     * Начальная цена закупки
     */
    private JFormattedTextField torgi_start_cost;

    /**
     * Конечная цена закупки
     */
    private JFormattedTextField torgi_finish_cost;

    /**
     * Экономия
     */
    private JLabel econom;

    /**
     * Количество заявок закупки
     */
    private JFormattedTextField count_all;

    /**
     * Количество участников закупки
     */
    private JFormattedTextField count_do;

    /**
     * Закупка выполнена Да/Нет
     */
    private JComboBox dogovor;

    /**
     * Закупки
     */
    private Purchases purchases;

    /**
     * Кнопка сохранить изменения параметров закупки
     */
    private JButton save;

    public PurchaseData(int purchase_id, Purchases purchases)
	    throws SQLException {
	this.purchase_id = purchase_id;
	this.purchases = purchases;

	this.getContentPane().add(initPanel());
	this.setVisible(true);

	this.setSize(850, 250);
	this.setTitle("Закупка: " + subject_title.getText());
	this.setLocation(170, 120);

	// this.set

	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new ListenerCloseWindow());

	save.setEnabled(false);
    }

    /**
     * Инициализация панели.
     * 
     * @throws SQLException
     */
    private JPanel initPanel() throws SQLException {

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Main.PATHTODB);

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

	/**
	 * Инициализация основных компонентов
	 */

	JButton hide = new JButton("Закрыть");
	hide.addActionListener(new ListenerCloseWindow());

	save = new JButton("Сохранить изменения");
	save.addActionListener(new ListenerSaveBtn());

	/**
	 * Наименование закупки
	 */
	subject_title = new JTextField();
	subject_title.setToolTipText("Наименование закупки");
	subject_title.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Номер закупки
	 */
	number = new JTextField();
	number.setToolTipText("Номер закупки");
	number.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Статус закупки
	 */
	status = new JComboBox(Main.status_items);
	status.setToolTipText("Статус закупки");
	status.addActionListener(new ListenerEnabledSaveBtn());

	/**
	 * Дата совершения закупки
	 */
	date = new JDateChooser();
	date.setToolTipText("Дата совершения закупки");
	date.addPropertyChangeListener(new ListenerEnabledSaveBtn());

	/**
	 * Способ размещения заказа
	 */
	aspect = new JComboBox(list_aspect);
	aspect.setToolTipText("Способ размещения заказа");
	aspect.addActionListener(new ListenerEnabledSaveBtn());

	/**
	 * Предмет закупки
	 */
	subject = new JComboBox(list_subject);
	subject.setToolTipText("Предмет закупки");
	aspect.addActionListener(new ListenerEnabledSaveBtn());

	/**
	 * Классификация закупки
	 */
	type = new JComboBox(list_type);
	type.setToolTipText("Классификация закупки");
	type.addActionListener(new ListenerEnabledSaveBtn());

	NumberFormat paymentFormat = NumberFormat
		.getCurrencyInstance(new Locale("RU", "ru"));
	paymentFormat.setMinimumFractionDigits(2);

	NumberFormat humanFormat = NumberFormat.getIntegerInstance();

	/**
	 * Начальная цена закупки
	 */
	torgi_start_cost = new JFormattedTextField(paymentFormat);
	torgi_start_cost.setToolTipText("Начальная цена закупки");
	torgi_start_cost.setColumns(15);
	torgi_start_cost.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Конечная цена закупки
	 */
	torgi_finish_cost = new JFormattedTextField(paymentFormat);
	torgi_finish_cost.setToolTipText("Конечная цена закупки");
	torgi_finish_cost.setColumns(15);
	torgi_finish_cost.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Экономия
	 */
	econom = new JLabel("цифра");
	econom.setToolTipText("Экономия");

	/**
	 * Количество заявок закупки
	 */
	count_all = new JFormattedTextField(humanFormat);
	count_all.setToolTipText("Количество заявок");
	count_all.setColumns(15);
	count_all.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Количество участников закупки
	 */
	count_do = new JFormattedTextField(humanFormat);
	count_do.setToolTipText("Количество участников");
	count_do.setColumns(15);
	count_do.addKeyListener(new ListenerEnabledSaveBtn());

	/**
	 * Закупка выполнена Да/Нет
	 */
	dogovor = new JComboBox(Main.dogovor_items);
	dogovor.setToolTipText("Договор");
	dogovor.addActionListener(new ListenerEnabledSaveBtn());

	/**
	 * Заполнение основных компонетов данными о закупке
	 */

	ResultSet rs_purchase = stat
		.executeQuery("SELECT * FROM purchase WHERE id LIKE '"
			+ this.purchase_id + "'");

	if (rs_purchase.next()) {
	    this.subject_title.setText(rs_purchase.getString("subject_title"));
	    this.number.setText(rs_purchase.getString("number"));
	    this.status.setSelectedIndex(rs_purchase.getInt("status"));

	    date.setDate(new Date(Long.parseLong(rs_purchase.getString("date"))));

	    setSelectedItemPurchase(aspect, rs_purchase.getInt("aspect_id"));
	    setSelectedItemPurchase(subject, rs_purchase.getInt("subject_id"));
	    setSelectedItemPurchase(type, rs_purchase.getInt("type_id"));

	    torgi_start_cost.setText(rs_purchase.getString("torgi_start_cost"));
	    torgi_finish_cost.setText(rs_purchase
		    .getString("torgi_finish_cost"));

	    torgi_start_cost.setValue(Double.parseDouble(rs_purchase
		    .getString("torgi_start_cost")));
	    torgi_finish_cost.setValue(Double.parseDouble(rs_purchase
		    .getString("torgi_finish_cost")));

	    econom.setText(getEconom(
		    Double.parseDouble(torgi_start_cost.getValue().toString()),
		    Double.parseDouble(torgi_finish_cost.getValue().toString())));

	    if (rs_purchase.getString("dogovor").equals("true"))
		dogovor.setSelectedIndex(0);
	    else
		dogovor.setSelectedIndex(1);
	    //
	    // count_all
	    // .setText(Integer.toString(rs_purchase.getInt("count_all")));
	    count_all.setValue(rs_purchase.getInt("count_all"));

	    // count_do.setText(Integer.toString(rs_purchase.getInt("count_do")));
	    count_do.setValue(rs_purchase.getInt("count_do"));
	}

	rs_purchase.close();
	stat.close();
	conn.close();

	/**
	 * Сборка наименования закупки и кнопки скрыть
	 */
	JPanel p_title_hide = new JPanel(new BorderLayout(5, 5));
	p_title_hide.add(subject_title, BorderLayout.CENTER);
	// p_title_hide.add(hide, BorderLayout.EAST);

	/**
	 * Сборка номера закупки
	 */
	JPanel p_number = new JPanel(new BorderLayout(5, 5));
	p_number.add(new JLabel("№ Закупки: "), BorderLayout.WEST);
	p_number.add(number, BorderLayout.CENTER);

	/**
	 * Сборка статуса закупки
	 */
	JPanel p_status = new JPanel(new BorderLayout(5, 5));
	p_status.add(new JLabel("Статус: "), BorderLayout.WEST);
	p_status.add(status, BorderLayout.CENTER);

	/**
	 * Сборка даты
	 */
	JPanel p_date = new JPanel(new BorderLayout(5, 5));
	p_date.add(new JLabel("Дата: "), BorderLayout.WEST);
	p_date.add(date, BorderLayout.CENTER);

	/**
	 * Сборка статуса и даты
	 */
	JPanel p_status_date = new JPanel(new BorderLayout(5, 5));
	p_status_date.add(p_status, BorderLayout.CENTER);
	p_status_date.add(p_date, BorderLayout.EAST);

	/**
	 * Сборка номера закупки - статуса - даты
	 */
	JPanel p_number_status_date = new JPanel(new GridLayout(1, 2, 5, 5));
	p_number_status_date.add(p_number);
	p_number_status_date.add(p_status_date);

	/**
	 * Сборка вверхей части<br>
	 * Наименование закупки<br>
	 * закупки - статус - дата
	 */
	JPanel p_north = new JPanel(new BorderLayout(5, 5));
	p_north.add(p_title_hide, BorderLayout.NORTH);
	p_north.add(p_number_status_date, BorderLayout.CENTER);

	/**
	 * Сборка наименований<br>
	 * - способа размещения закупки<br>
	 * - предмета закупки<br>
	 * - классификации закупки.
	 */
	JPanel p_title = new JPanel(new GridLayout(3, 1, 5, 5));
	p_title.add(new JLabel("Способ размещения закупки: "));
	p_title.add(new JLabel("Предмет закупки: "));
	p_title.add(new JLabel("Классификация закупки: "));

	/**
	 * Сборка контейноров<br>
	 * - способа размещения закупки<br>
	 * - предмета закупки<br>
	 * - классификации закупки.
	 */
	JPanel p_combo = new JPanel(new GridLayout(3, 1, 5, 5));
	p_combo.add(aspect);
	p_combo.add(subject);
	p_combo.add(type);

	/**
	 * Сборка<br>
	 * - способа размещения закупки<br>
	 * - предмета закупки<br>
	 * - классификации закупки.
	 */
	JPanel p_3_combo = new JPanel(new BorderLayout(5, 5));
	p_3_combo.add(p_title, BorderLayout.WEST);
	p_3_combo.add(p_combo, BorderLayout.CENTER);

	/**
	 * Сборка начальная цена
	 */
	JPanel p_start = new JPanel(new BorderLayout(5, 5));
	p_start.add(new JLabel("Начальная цена: "), BorderLayout.WEST);
	p_start.add(torgi_start_cost, BorderLayout.CENTER);

	/**
	 * Сборка конечная цена
	 */
	JPanel p_finish = new JPanel(new BorderLayout(5, 5));
	p_finish.add(new JLabel("Конечная цена: "), BorderLayout.WEST);
	p_finish.add(torgi_finish_cost, BorderLayout.CENTER);

	/**
	 * Сборка экономия
	 */
	JPanel p_econom = new JPanel(new BorderLayout(5, 5));
	p_econom.add(new JLabel("Экономия: "), BorderLayout.WEST);
	p_econom.add(econom, BorderLayout.CENTER);

	/**
	 * Сборка начальной и конечной цены
	 */
	JPanel p_start_finish = new JPanel(new GridLayout(1, 2, 5, 5));
	p_start_finish.add(p_start);
	p_start_finish.add(p_finish);

	/**
	 * Сборка начальной, конечной цены и эконоимии
	 */
	JPanel p_cost = new JPanel(new BorderLayout(5, 5));
	p_cost.add(p_start_finish, BorderLayout.CENTER);
	p_cost.add(p_econom, BorderLayout.EAST);

	JPanel btns = new JPanel(new BorderLayout(5, 5));
	btns.add(hide, BorderLayout.EAST);
	btns.add(save, BorderLayout.CENTER);

	/**
	 * Сборка договора
	 */
	JPanel p_dogovor = new JPanel(new BorderLayout(5, 5));
	p_dogovor.add(new JLabel("Договор: "), BorderLayout.WEST);
	p_dogovor.add(dogovor, BorderLayout.CENTER);
	p_dogovor.add(btns, BorderLayout.EAST);

	/**
	 * Сборка подавщих заявку
	 */
	JPanel p_count_all = new JPanel(new BorderLayout(5, 5));
	p_count_all.add(new JLabel("Подавщих заявку: "), BorderLayout.WEST);
	p_count_all.add(count_all, BorderLayout.CENTER);

	/**
	 * Сборка количество участников
	 */
	JPanel p_count_do = new JPanel(new BorderLayout(5, 5));
	p_count_do.add(new JLabel("Участников: "), BorderLayout.WEST);
	p_count_do.add(count_do, BorderLayout.CENTER);

	/**
	 * Сборка подавщих заявку и количество участников
	 */
	JPanel p_countt_all_do = new JPanel(new GridLayout(1, 2, 5, 5));
	p_countt_all_do.add(p_count_all);
	p_countt_all_do.add(p_count_do);

	/**
	 * Сборка цен, договора, количество участников
	 */
	JPanel p_south = new JPanel(new BorderLayout(5, 5));
	p_south.add(p_cost, BorderLayout.NORTH);
	p_south.add(p_countt_all_do, BorderLayout.CENTER);
	p_south.add(p_dogovor, BorderLayout.EAST);

	JPanel panel = new JPanel();

	panel.setLayout(new BorderLayout(5, 5));
	panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	panel.add(p_north, BorderLayout.NORTH);
	panel.add(p_3_combo, BorderLayout.CENTER);
	panel.add(p_south, BorderLayout.SOUTH);

	return panel;

	// this.add(purchase_panel, BorderLayout.SOUTH);
	// this.repaint();
	// this.validate();
    }

    /**
     * Задает значение выпадающего списка, согласно данным о закупке.
     * 
     * @param combo
     *            - выпадающий список
     * @param value
     *            - искомое значение
     */
    private void setSelectedItemPurchase(JComboBox combo, int value) {
	for (int index = 0; index < combo.getItemCount(); index++) {
	    ComboItemIntValue item = (ComboItemIntValue) combo.getItemAt(index);
	    if (item.getValue() == value) {
		combo.setSelectedIndex(index);
	    }
	}
    }

    /**
     * Высчитывает экономию
     * 
     * @param start
     *            - начальная цена
     * @param finish
     *            - конечная цена
     * @return экономия :: %
     */
    private String getEconom(Double start, Double finish) {

	BigDecimal start_big = new BigDecimal(start);
	BigDecimal finish_big = new BigDecimal(finish);

	BigDecimal eco = start_big.subtract(finish_big).setScale(2,
		RoundingMode.HALF_UP);

	BigDecimal per = new BigDecimal(0);
	if (start_big.doubleValue() != 0.0)
	    per = eco.multiply(BigDecimal.valueOf(100))
		    .divide(start_big, new MathContext(2))
		    .setScale(2, RoundingMode.HALF_UP);

	return eco.toString() + " : " + per + "%";
    }

    /**
     * Закрытие окна закупки
     * 
     * @author kirill
     * 
     */
    private class ListenerCloseWindow implements ActionListener, WindowListener {

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
	    isSave();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    isSave();
	}

	private void isSave() {
	    if (save.isEnabled()) {
		int result = JOptionPane
			.showConfirmDialog(
				null,
				"Сохранить произведенные изменения перед закрытием закупки?",
				"Предупреждение",
				JOptionPane.YES_NO_CANCEL_OPTION);
		switch (result) {
		case JOptionPane.YES_OPTION:
		    try {
			if (saveData())
			    close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		    break;
		case JOptionPane.NO_OPTION:
		    close();
		    break;
		case JOptionPane.CANCEL_OPTION:
		    break;
		}
	    } else
		close();
	}

	private void close() {
	    dispose();
	}

    }

    private boolean saveData() throws SQLException {

	Integer count_alls = new Integer(count_all.getText());
	Integer count_dos = new Integer(count_do.getText());

	if (count_alls < count_dos) {
	    JOptionPane.showMessageDialog(null,
		    "Число участников не может быть больше заявок!");
	    count_do.setValue(0);
	    return false;
	}

	Double start = Double.parseDouble(torgi_start_cost.getValue()
		.toString());
	Double finish = Double.parseDouble(torgi_finish_cost.getValue()
		.toString());

	if (start < finish) {
	    JOptionPane.showMessageDialog(null,
		    "Конечная цена не может быть больше начальной!");
	    torgi_finish_cost.setValue(0.0);
	    return false;
	}

	// return false;
	// System.out.println(purchase_id);
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Main.PATHTODB);

	String query = "UPDATE purchase SET subject_title = ? , subject_title_low = ?, number = ?, "
		+ " subject_id = ?, aspect_id = ?, type_id = ?, date = ?, status = ?, count_all = ?,"
		+ " count_do = ?, torgi_start_cost = ?, torgi_finish_cost = ?, dogovor = ? "
		+ " WHERE id LIKE '" + purchase_id + "' ;";
	PreparedStatement preparedStmt = conn.prepareStatement(query);

	// Наименование закупки
	preparedStmt.setString(1, subject_title.getText());
	// Наименование закупки для поиска
	preparedStmt.setString(2, subject_title.getText().toLowerCase());
	// Номер закупки
	preparedStmt.setString(3, number.getText());

	// Предмет закупки
	preparedStmt.setInt(4,
		((ComboItemIntValue) subject.getSelectedItem()).getValue());
	// Способ размещения заказа
	preparedStmt.setInt(5,
		((ComboItemIntValue) aspect.getSelectedItem()).getValue());
	// Классификация закупки
	preparedStmt.setInt(6,
		((ComboItemIntValue) type.getSelectedItem()).getValue());
	// Дата
	preparedStmt.setString(7, Long.toString(date.getDate().getTime()));

	// Статус закупки
	preparedStmt.setInt(8,
		((ComboItemIntValue) status.getSelectedItem()).getValue());

	// Количество заявок
	preparedStmt.setInt(9, new Integer(count_all.getText()));

	// Количество участников
	preparedStmt.setInt(10, new Integer(count_do.getText()));

	// Начальная цена
	preparedStmt.setString(11, torgi_start_cost.getValue().toString());

	// Конечная цена
	preparedStmt.setString(12, torgi_finish_cost.getValue().toString());

	// Договор
	preparedStmt.setString(13, Boolean
		.toString(((ComboItemBooleanValue) dogovor.getSelectedItem())
			.getValue()));

	preparedStmt.executeUpdate();
	preparedStmt.close();

	conn.close();
	purchases.updateTable();

	econom.setText(getEconom(
		Double.parseDouble(torgi_start_cost.getValue().toString()),
		Double.parseDouble(torgi_finish_cost.getValue().toString())));
	save.setEnabled(false);
	return true;
    }

    /**
     * Сохранение изменений данных закупки.
     * 
     * @author kirill
     * 
     */
    private class ListenerSaveBtn implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		saveData();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}
    }

    /**
     * Активация кнопки "Сохранить изменения", если хотя бы один из параметров
     * закупки был изменен.
     * 
     * @author kirill
     * 
     */
    private class ListenerEnabledSaveBtn implements KeyListener,
	    ActionListener, MouseListener, PropertyChangeListener {

	@Override
	public void mouseClicked(MouseEvent e) {
	    enabled();
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

	@Override
	public void actionPerformed(ActionEvent e) {
	    enabled();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	    enabled();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	private void enabled() {
	    save.setEnabled(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
	    enabled();
	}
    }
}
