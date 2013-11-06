package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.Main;
import ru.fiko.purchase.reports.Number2;
import ru.fiko.purchase.reports.Number3or4;
import ru.fiko.purchase.reports.Number5;
import ru.fiko.purchase.supports.ComboItemIntValue;

public class Reports extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -8215638536621659987L;

    /**
     * Указатель на родителя.<br>
     * Используется для смены компонентов окна.
     */
    private Main purchase;

    JPanel table = new JPanel();

    /**
     * Выпадающий список возможных годов
     */
    private JComboBox year;

    private JDateChooser date = new JDateChooser();

    /**
     * Выпадающий список возможных месяцев
     */
    private JComboBox month;

    public Reports(Main purchase) throws ClassNotFoundException, SQLException {
	this.purchase = purchase;

	this.setLayout(new BorderLayout(5, 5));
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	this.add(north(), BorderLayout.NORTH);
	this.add(data_panel(), BorderLayout.CENTER);
	this.add(foot_btns(), BorderLayout.SOUTH);
    }

    private JPanel north() {
	JPanel panel = new JPanel(new BorderLayout(5, 5));
	date.setDate(new Date(System.currentTimeMillis()));

	JButton btn_report = new JButton("Отчет №2");
	btn_report.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new Number2(date.getDate());
	    }
	});

	JButton btn_report3 = new JButton("Отчет №3");
	btn_report3.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new Number3or4(date.getDate(), 3);
	    }
	});

	JButton btn_report4 = new JButton("Отчет №4");
	btn_report4.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new Number3or4(date.getDate(), 4);
	    }
	});

	JButton btn_report5 = new JButton("Отчет №5");
	btn_report5.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new Number5(date.getDate());
	    }
	});

	year = new JComboBox(Constant.year_items);
	month = new JComboBox(Constant.month_items);

	year.addActionListener(new ListenerChange());
	month.addActionListener(new ListenerChange());

	JPanel panel2 = new JPanel(new BorderLayout(5, 5));

	panel2.add(year, BorderLayout.EAST);
	panel2.add(month, BorderLayout.CENTER);

	JPanel panel3 = new JPanel(new GridLayout(1, 3));
	panel3.add(btn_report);
	panel3.add(btn_report3);
	panel3.add(btn_report4);
	panel3.add(btn_report5);

	panel.add(panel2, BorderLayout.EAST);
	panel.add(panel3, BorderLayout.WEST);
	panel.add(date);

	return panel;
    }

    /**
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private JPanel data_panel() throws ClassNotFoundException, SQLException {
	JPanel panel = new JPanel(new BorderLayout(5, 5));

	table.setLayout(new BoxLayout(table, BoxLayout.PAGE_AXIS));

	content();

	panel.add(table);

	return panel;
    }

    private void content() throws ClassNotFoundException, SQLException {
	table.removeAll();
	Object[] result = getContentFromDB();

	JPanel p1 = new JPanel(new BorderLayout(5, 5));
	p1.add(new JLabel("Организации, рег законом о закупках:"),
		BorderLayout.WEST);
	p1.add(new JLabel(Integer.toString((Integer) result[0])),
		BorderLayout.CENTER);

	JPanel p2 = new JPanel(new BorderLayout(5, 5));
	p2.add(new JLabel("Организации, зарегистр на Общероссийском сайте:"),
		BorderLayout.WEST);
	p2.add(new JLabel(Integer.toString((Integer) result[1])),
		BorderLayout.CENTER);

	JPanel p22 = new JPanel(new BorderLayout(5, 5));
	p22.add(new JLabel("из них регулируемых:"), BorderLayout.WEST);
	p22.add(new JLabel(Integer.toString((Integer) result[2])),
		BorderLayout.CENTER);

	JPanel p3 = new JPanel(new BorderLayout(5, 5));
	p3.add(new JLabel(" - по водоснабжению:"), BorderLayout.WEST);
	p3.add(new JLabel(Integer.toString((Integer) result[3])),
		BorderLayout.CENTER);

	JPanel p4 = new JPanel(new BorderLayout(5, 5));
	p4.add(new JLabel(" - в теплоэнергетике:"), BorderLayout.WEST);
	p4.add(new JLabel(Integer.toString((Integer) result[4])),
		BorderLayout.CENTER);

	JPanel p5 = new JPanel(new BorderLayout(5, 5));
	p5.add(new JLabel(" - электроэнергия:"), BorderLayout.WEST);
	p5.add(new JLabel(Integer.toString((Integer) result[5])),
		BorderLayout.CENTER);

	JPanel p6 = new JPanel(new BorderLayout(5, 5));
	p6.add(new JLabel(" - утилизцаия ТБО:"), BorderLayout.WEST);
	p6.add(new JLabel(Integer.toString((Integer) result[6])),
		BorderLayout.CENTER);

	JPanel p7 = new JPanel(new BorderLayout(5, 5));
	p7.add(new JLabel("Орг. разместившие положение:"), BorderLayout.WEST);
	p7.add(new JLabel(Integer.toString((Integer) result[7])),
		BorderLayout.CENTER);

	JPanel p8 = new JPanel(new BorderLayout(5, 5));
	p8.add(new JLabel("из них регулируемых:"), BorderLayout.WEST);
	p8.add(new JLabel(Integer.toString((Integer) result[8])),
		BorderLayout.CENTER);

	JPanel p33 = new JPanel(new BorderLayout(5, 5));
	p33.add(new JLabel(" - по водоснабжению:"), BorderLayout.WEST);
	p33.add(new JLabel(Integer.toString((Integer) result[9])),
		BorderLayout.CENTER);

	JPanel p44 = new JPanel(new BorderLayout(5, 5));
	p44.add(new JLabel(" - в теплоэнергетике:"), BorderLayout.WEST);
	p44.add(new JLabel(Integer.toString((Integer) result[10])),
		BorderLayout.CENTER);

	JPanel p55 = new JPanel(new BorderLayout(5, 5));
	p55.add(new JLabel(" - электроэнергия:"), BorderLayout.WEST);
	p55.add(new JLabel(Integer.toString((Integer) result[11])),
		BorderLayout.CENTER);

	JPanel p66 = new JPanel(new BorderLayout(5, 5));
	p66.add(new JLabel(" - утилизцаия ТБО:"), BorderLayout.WEST);
	p66.add(new JLabel(Integer.toString((Integer) result[12])),
		BorderLayout.CENTER);

	JPanel p9 = new JPanel(new BorderLayout(5, 5));
	p9.add(new JLabel("Количество договоров:"), BorderLayout.WEST);
	p9.add(new JLabel(Long.toString((Long) result[13])),
		BorderLayout.CENTER);
	
	NumberFormat paymentFormat = NumberFormat
		.getCurrencyInstance(new Locale("RU", "ru"));
	paymentFormat.setMinimumFractionDigits(2);
	
	
	
	JPanel p99 = new JPanel(new BorderLayout(5, 5));
	p99.add(new JLabel("Сумма:                      "), BorderLayout.WEST);
	p99.add(new JLabel(paymentFormat.format(((BigDecimal) result[14]).doubleValue())),
		BorderLayout.CENTER);

	JPanel p999 = new JPanel(new BorderLayout(5, 5));
	p999.add(new JLabel("Сумма за месяц:"), BorderLayout.WEST);
	p999.add(new JLabel(paymentFormat.format(((BigDecimal) result[15]).doubleValue())),
		BorderLayout.CENTER);
	
	

	table.add(p1);
	table.add(p2);
	table.add(p22);
	table.add(p3);
	table.add(p4);
	table.add(p5);
	table.add(p6);
	table.add(p7);
	table.add(p8);
	table.add(p33);
	table.add(p44);
	table.add(p55);
	table.add(p66);
	table.add(p9);
	table.add(p99);
	table.add(p999);

	this.repaint();
	this.validate();
    }

    private Object[] getContentFromDB() throws ClassNotFoundException,
	    SQLException {
	Object[] result = new Object[16];

	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	Statement stat = conn.createStatement();

	int org_all = 0;
	int poloj = 0;

	int p_all = 0;
	int p_water = 0;
	int p_teplo = 0;
	int p_elect = 0;
	int p_tbo = 0;

	ResultSet rs = stat.executeQuery("SELECT * FROM organization");

	while (rs.next()) {
	    org_all++;
	    if (rs.getString("polojen_ooc").equals("") != true) {
		boolean plus_org = false;
		poloj++;
		Statement stat2 = conn.createStatement();
		ResultSet rs2 = stat2
			.executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
				+ rs.getString("id")
				+ "' AND type_of_org_id LIKE '7'");

		while (rs2.next()) {
		    p_water++;
		    plus_org = true;
		}

		rs2.close();

		rs2 = stat2
			.executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
				+ rs.getString("id")
				+ "' AND type_of_org_id LIKE '6'");

		while (rs2.next()) {
		    p_teplo++;
		    plus_org = true;
		}

		rs2.close();

		rs2 = stat2
			.executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
				+ rs.getString("id")
				+ "' AND type_of_org_id LIKE '4'");

		while (rs2.next()) {
		    p_elect++;
		    plus_org = true;
		}

		rs2.close();

		rs2 = stat2
			.executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
				+ rs.getString("id")
				+ "' AND type_of_org_id LIKE '8'");

		while (rs2.next()) {
		    p_tbo++;
		    plus_org = true;
		}

		rs2.close();
		stat2.close();

		if (plus_org)
		    p_all++;
	    }
	}

	rs.close();

	result[0] = org_all;

	int org_all_reg = 0;
	int water = 0;
	int teplo = 0;
	int elect = 0;
	int tbo = 0;

	int all = 0;

	rs = stat
		.executeQuery("SELECT * FROM organization WHERE regist LIKE 'true'");

	while (rs.next()) {
	    boolean plus_org = false;
	    org_all_reg++;
	    Statement stat2 = conn.createStatement();
	    ResultSet rs2 = stat2
		    .executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
			    + rs.getString("id")
			    + "' AND type_of_org_id LIKE '7'");

	    while (rs2.next()) {
		water++;
		plus_org = true;
	    }

	    rs2.close();

	    rs2 = stat2
		    .executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
			    + rs.getString("id")
			    + "' AND type_of_org_id LIKE '6'");

	    while (rs2.next()) {
		teplo++;
		plus_org = true;
	    }

	    rs2.close();

	    rs2 = stat2
		    .executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
			    + rs.getString("id")
			    + "' AND type_of_org_id LIKE '4'");

	    while (rs2.next()) {
		elect++;
		plus_org = true;
	    }

	    rs2.close();

	    rs2 = stat2
		    .executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
			    + rs.getString("id")
			    + "' AND type_of_org_id LIKE '8'");

	    while (rs2.next()) {
		tbo++;
		plus_org = true;
	    }

	    rs2.close();
	    stat2.close();
	    if (plus_org)
		all++;

	}

	rs.close();

	result[1] = org_all_reg;
	result[2] = all;
	result[3] = water;
	result[4] = teplo;
	result[5] = elect;
	result[6] = tbo;
	result[7] = poloj;
	result[8] = p_all;
	result[9] = p_water;
	result[10] = p_teplo;
	result[11] = p_elect;
	result[12] = p_tbo;

	long dogovor = 0;
	BigDecimal all_sum = BigDecimal.ZERO;
	BigDecimal sum_month = BigDecimal.ZERO;

	for (Object m : Constant.month_items) {

	     sum_month = BigDecimal.ZERO;

	    rs = stat
		    .executeQuery("SELECT * FROM report WHERE report_type_id LIKE '1' AND month LIKE '"
			    + ((ComboItemIntValue) m).getValue()
			    + "' AND year LIKE '"
			    + ((ComboItemIntValue) this.year.getSelectedItem())
				    .getValue() + "'");

	    while (rs.next()) {
		dogovor += rs.getInt("count_dogovors");

		Double sum = Double.parseDouble(rs.getString("summa"));
		sum_month = sum_month.add(new BigDecimal(sum));
		sum_month = sum_month.divide(BigDecimal.ONE).setScale(2,
			RoundingMode.HALF_UP);
	    }

	    rs.close();

	    all_sum = all_sum.add(sum_month);
	    all_sum = all_sum.divide(BigDecimal.ONE).setScale(2,
		    RoundingMode.HALF_UP);

	    if (((ComboItemIntValue) m).getValue() == ((ComboItemIntValue) this.month
		    .getSelectedItem()).getValue())
		break;
	}
	result[13] = dogovor;
	result[14] = sum_month;
	result[15] = all_sum;

	stat.close();
	conn.close();

	return result;
    }

    /**
     * @return
     */
    private JPanel foot_btns() {
	JButton btn_prev = new JButton("Назад");
	btn_prev.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    purchase.setPanelSearchOrg();
		} catch (SQLException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
	    }
	});

	/**
	 * Сборка управляющих кнопок
	 */
	JPanel foot_btns = new JPanel(new GridLayout(1, 1, 5, 5));
	foot_btns.add(btn_prev);

	JPanel foot = new JPanel(new BorderLayout(5, 5));
	foot.add(foot_btns, BorderLayout.EAST);

	return foot;
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
