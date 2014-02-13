package ru.fiko.purchase.windows.organization;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.supports.ComboItemIntValue;
import ru.fiko.purchase.supports.ListenerTextChangeTrue;

public class Reports extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5918392117313671690L;

    /**
     * Выпадающий список возможных годов
     */
    private JComboBox year;

    /**
     * Выпадающий список возможных месяцев
     */
    private JComboBox month;

    private Organization org;

    private JPanel content = new JPanel(new BorderLayout(5, 5));

    private String id0 = "0";
    private String id1 = "0";
    private String id2 = "0";

    private JFormattedTextField s0;
    private JFormattedTextField s1;
    private JFormattedTextField s2;

    private JFormattedTextField c0;
    private JFormattedTextField c1;
    private JFormattedTextField c2;

    public Reports(Organization organization) throws ClassNotFoundException,
	    SQLException {
	org = organization;

	this.setLayout(new BorderLayout(5, 5));

	this.add(north(), BorderLayout.NORTH);
	this.add(content, BorderLayout.CENTER);
	content();

    }

    /**
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void content() throws ClassNotFoundException, SQLException {
	content.removeAll();

	int month = ((ComboItemIntValue) this.month.getSelectedItem())
		.getValue();
	int year = ((ComboItemIntValue) this.year.getSelectedItem()).getValue();

	JPanel panel = new JPanel(new BorderLayout(5, 5));

	JPanel table = new JPanel();
	table.setLayout(new BoxLayout(table, BoxLayout.PAGE_AXIS));

	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);
	Statement stat = conn.createStatement();
	ResultSet rs;

	for (Object m : Constant.month_items) {

	    rs = stat
		    .executeQuery("SELECT * FROM report WHERE organization_id LIKE '"
			    + this.org.getId()
			    + "' AND month LIKE '"
			    + ((ComboItemIntValue) m).getValue()
			    + "' AND year LIKE '" + year + "'");

	    if (!rs.next()) {

		// создание записи о month и year
		PreparedStatement pst = conn
			.prepareStatement("INSERT INTO report VALUES (?, ?, ?, ?, ?, ?, ?);");

		int[] types = { 1, 2, 3 };

		for (int type : types) {

		    // ID организации
		    pst.setInt(2, org.getId());
		    // тип отчета
		    pst.setInt(3, type);
		    // месяц
		    pst.setInt(4, ((ComboItemIntValue) m).getValue());
		    // год
		    pst.setString(5, Integer.toString(year));
		    // количество договоров
		    pst.setInt(6, 0);
		    // сумма
		    pst.setString(7, "0");

		    pst.addBatch();
		}
		pst.executeBatch();

		pst.close();

	    }
	    rs.close();
	}

	rs = stat
		.executeQuery("SELECT * FROM report WHERE organization_id LIKE '"
			+ this.org.getId()
			+ "' AND month LIKE '"
			+ month
			+ "' AND year LIKE '" + year + "'");

	while (rs.next()) {

	    NumberFormat paymentFormat = NumberFormat
		    .getCurrencyInstance(new Locale("RU", "ru"));
	    paymentFormat.setMinimumFractionDigits(2);

	    NumberFormat humanFormat = NumberFormat.getIntegerInstance();

	    /**
	     * Начальная цена закупки
	     */
	    JFormattedTextField summa = new JFormattedTextField(paymentFormat);

	    JFormattedTextField count_do = new JFormattedTextField(humanFormat);

	    switch (rs.getInt("report_type_id")) {
	    case 1:
		id0 = rs.getString("id");
		s0 = summa;
		c0 = count_do;
		break;
	    case 2:
		id1 = rs.getString("id");
		s1 = summa;
		c1 = count_do;
		break;
	    case 3:
		id2 = rs.getString("id");
		s2 = summa;
		c2 = count_do;
		break;
	    }
	    String title = "1";
	    BigDecimal all_sum = BigDecimal.ZERO;
	    long all_dogovor = 0;

	    Statement stat2 = conn.createStatement();
	    ResultSet rs2 = stat2
		    .executeQuery("SELECT * FROM report_type WHERE id LIKE '"
			    + rs.getInt("report_type_id") + "'");
	    if (rs2.next())
		title = rs2.getString("title");

	    rs2.close();
	    stat2.close();

	    Statement stat3 = conn.createStatement();

	    for (Object m : Constant.month_items) {

		ResultSet rs3 = stat3
			.executeQuery("SELECT * FROM report WHERE organization_id LIKE '"
				+ this.org.getId()
				+ "' AND month LIKE '"
				+ ((ComboItemIntValue) m).getValue()
				+ "' AND year LIKE '"
				+ year
				+ "'"
				+ " AND report_type_id LIKE '"
				+ rs.getInt("report_type_id") + "'");
		if (rs3.next()) {
		    all_dogovor += rs3.getInt("count_dogovors");

		    Double sum = Double.parseDouble(rs3.getString("summa"));
		    all_sum = all_sum.add(new BigDecimal(sum));
		    all_sum = all_sum.divide(BigDecimal.ONE).setScale(2,
			    RoundingMode.HALF_UP);
		}

		rs3.close();

		if (((ComboItemIntValue) m).getValue() == month)
		    break;
	    }
	    stat3.close();

	    summa.setToolTipText("Сумма");
	    summa.setColumns(15);
	    summa.addFocusListener(new ListenerTextChangeTrue());
	    summa.setValue(Double.parseDouble(rs.getString("summa")));

	    count_do.setToolTipText("Количество договоров");
	    count_do.setColumns(15);
	    count_do.setValue(rs.getInt("count_dogovors"));

	    JPanel p1 = new JPanel(new BorderLayout(5, 5));
	    p1.add(new JLabel("Сумма: "), BorderLayout.WEST);
	    p1.add(summa, BorderLayout.CENTER);

	    JPanel p2 = new JPanel(new BorderLayout(5, 5));
	    p2.add(new JLabel("Количество договоров: "), BorderLayout.WEST);
	    p2.add(count_do, BorderLayout.CENTER);

	    JPanel p3 = new JPanel(new BorderLayout(5, 5));
	    p3.add(p1, BorderLayout.WEST);
	    p3.add(p2, BorderLayout.EAST);

	    JPanel p11 = new JPanel(new BorderLayout(5, 5));
	    p11.add(new JLabel("Общая сумма: "), BorderLayout.WEST);
	    p11.add(new JLabel(all_sum.toString()), BorderLayout.CENTER);

	    JPanel p22 = new JPanel(new BorderLayout(5, 5));
	    p22.add(new JLabel("Общее количество договоров: "),
		    BorderLayout.WEST);
	    p22.add(new JLabel(Long.toString(all_dogovor) + "  "),
		    BorderLayout.CENTER);

	    JPanel p33 = new JPanel(new BorderLayout(5, 5));
	    p33.add(p11, BorderLayout.WEST);
	    p33.add(p22, BorderLayout.EAST);

	    JPanel p_34 = new JPanel(new BorderLayout(5, 5));
	    p_34.add(p3, BorderLayout.NORTH);
	    p_34.add(p33, BorderLayout.CENTER);

	    JPanel p4 = new JPanel(new BorderLayout(5, 5));
	    p4.add(new JLabel(title), BorderLayout.NORTH);
	    p4.add(p_34, BorderLayout.CENTER);

	    JPanel item = new JPanel(new BorderLayout(5, 5));

	    item.add(p4, BorderLayout.NORTH);

	    table.add(item);
	}

	rs.close();
	stat.close();
	conn.close();

	JScrollPane pane = new JScrollPane(table);
	JScrollBar jsp = pane.getVerticalScrollBar();
	jsp.setUnitIncrement(20);

	panel.add(pane, BorderLayout.CENTER);

	content.add(panel);

	content.repaint();
	content.validate();
    }

    private JPanel north() {
	JPanel panel = new JPanel(new BorderLayout(5, 5));

	year = new JComboBox(Constant.year_items);
	month = new JComboBox(Constant.month_items);

	SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
	Date date = new Date(System.currentTimeMillis());

	int mon = Integer.parseInt(dateFormat.format(date)) - 2;
	if (mon < 0)
	    mon = 12 + mon;

	month.setSelectedIndex(mon);

	year.addActionListener(new ListenerChange());
	month.addActionListener(new ListenerChange());

	JButton btn = new JButton("Обновить");
	btn.addActionListener(new ListenerBtn());

	JPanel panel2 = new JPanel(new BorderLayout(5, 5));

	panel2.add(year, BorderLayout.EAST);
	panel2.add(month, BorderLayout.CENTER);
	panel2.add(btn, BorderLayout.WEST);

	panel.add(panel2, BorderLayout.EAST);

	return panel;
    }

    private boolean saveData() throws SQLException {

	// return false;
	// System.out.println(purchase_id);
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	Object[][] obj = { { id0, s0, c0 }, { id1, s1, c1 }, { id2, s2, c2 } };

	for (Object[] objects : obj) {

	    String query = "UPDATE report SET summa = ? , count_dogovors = ? "
		    + " WHERE id LIKE '" + ((String) objects[0]) + "' ;";

	    PreparedStatement preparedStmt = conn.prepareStatement(query);

	    preparedStmt.setInt(2, new Integer(
		    ((JFormattedTextField) objects[2]).getValue().toString()));

	    // Конечная цена
	    preparedStmt.setString(1, ((JFormattedTextField) objects[1])
		    .getValue().toString());

	    preparedStmt.executeUpdate();
	    preparedStmt.close();
	}
	conn.close();

	try {
	    content();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	return true;
    }

    private class ListenerBtn implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		saveData();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	}

    }

    private class ListenerChange implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		content();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    } catch (ClassNotFoundException e1) {
		e1.printStackTrace();
	    }
	}

    }

}
