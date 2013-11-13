package ru.fiko.purchase.reports;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.supports.ComboItemIntValue;
import ru.fiko.purchase.supports.JXLConstant;

import jxl.Workbook;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Number5 {

    String dir = "Отчеты";

    int year = 2013;
    int month = 0;

    Vector<Integer> org_ids;

    Date date_finish = new Date(System.currentTimeMillis());
    Date date_start = new Date(System.currentTimeMillis());

    @SuppressWarnings("unchecked")
    public Number5(Date date, Vector<Integer> ids) {

	org_ids = (Vector<Integer>) ids.clone();

	SimpleDateFormat formatter = new SimpleDateFormat("dd.MMMM.yyyy");
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
	SimpleDateFormat formatter3 = new SimpleDateFormat("MM");

	try {
	    long d1 = formatter.parse(formatter.format(date)).getTime();

	    this.date_finish = new Date(d1 + 86400000 - 1);
	} catch (ParseException e1) {
	    e1.printStackTrace();
	}
	try {
	    long d2 = formatter2.parse(formatter2.format(date)).getTime();
	    this.date_start = new Date(d2);
	} catch (ParseException e1) {
	    e1.printStackTrace();
	}

	year = Integer.parseInt(formatter2.format(date));
	month = Integer.parseInt(formatter3.format(date)) - 1;

	try {
	    create();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void create() throws IOException, WriteException,
	    ClassNotFoundException, SQLException {
	int row = 1;
	int column = 1;

	JXLConstant font = new JXLConstant();

	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	new File(dir).mkdirs();

	SimpleDateFormat formatter = new SimpleDateFormat("dd.MMMM.yyyy");

	WritableWorkbook workbook = Workbook.createWorkbook(new File(dir + "/"
		+ "Отчет №5("
		+ formatter.format(new Date(System.currentTimeMillis()))
		+ ").xls"));

	WritableSheet sheet = workbook.createSheet("Отчет №3", 0);

	String title = "Ежемесячный отчет с нарастающим итогом (с предварительной сортировкой по видам деятельности)"
		+ formatter.format(this.date_finish);

	sheet.addCell(new Label(column, row, title, font.tahomaTitleValue));
	sheet.mergeCells(column, row, column + 4, row);
	sheet.setRowView(row, 450);

	String[] titles_column = {
		"№ п/п",
		"Наименование организации",
		"ИНН",
		"Вид юридического лица",
		"Регистрация на ООС",
		"Опубликование положения о закупках на ООС",
		"Размещение заказа на ООС",
		"Состоялось закупок",
		"Начальная цена",
		"Цена контракта",
		"Экономия",
		"Предоставили отчет по договорам" };

	for (int i = 0; i < titles_column.length; i++) {
	    sheet.addCell(new Label(column + i, row + 1, titles_column[i],
		    font.tahomaTitleValue));
	}
	sheet.setRowView(row + 1, 800);

	int[] columnsView = { 7, 60, 20, 40, 20, 30, 30, 20, 40, 40, 40, 20 };
	for (int i = 0; i < columnsView.length; i++)
	    sheet.setColumnView(column + i, columnsView[i]);

	row++;
	int org_count = 1;

	for (int id : org_ids) {

	    Statement stat = conn.createStatement();
	    ResultSet org = stat
		    .executeQuery("SELECT * FROM organization WHERE id LIKE '"
			    + +id + "'");
	    while (org.next()) {

		int thisRow = row + org_count;
		int org_id = org.getInt("id");

		String org_name = org.getString("name");
		String org_inn = org.getString("inn");

		if (org_name.length() > 55)
		    sheet.setRowView(thisRow, 900);
		else
		    sheet.setRowView(thisRow, 450);

		sheet.addCell(new Label(column, thisRow, Integer
			.toString(org_count), font.tahomaValue2));

		sheet.addCell(new Label(column + 1, thisRow, org_name,
			font.tahomaValue2));

		sheet.addCell(new Label(column + 2, thisRow, org_inn,
			font.tahomaValue2));

		String type_of_org = "";

		Statement stat2 = conn.createStatement();
		ResultSet t = stat2
			.executeQuery("SELECT * FROM types_of_org WHERE organization_id LIKE '"
				+ org_id + "'");

		while (t.next()) {
		    Statement stat3 = conn.createStatement();
		    ResultSet t2 = stat3
			    .executeQuery("SELECT * FROM type_of_org WHERE id LIKE '"
				    + t.getString("type_of_org_id") + "'");
		    while (t2.next()) {
			type_of_org += t2.getString("title") + "; ";
		    }

		    t2.close();
		    stat3.close();
		}

		t.close();
		stat2.close();

		sheet.addCell(new Label(column + 3, thisRow, type_of_org,
			font.tahomaValue2));

		String regist = "Зарегистрирована";
		if (org.getString("regist").equals("false"))
		    regist = "Не зарегистрирована";

		sheet.addCell(new Label(column + 4, thisRow, regist,
			font.tahomaValue2));

		sheet.addCell(new Label(column + 5, thisRow, org
			.getString("polojen_ooc"), font.tahomaValue2));

		sheet.addCell(new Label(column + 6, thisRow, "",
			font.tahomaValue2));

		Double count = 0.0;
		BigDecimal start = BigDecimal.ZERO;
		BigDecimal finish = BigDecimal.ZERO;

		Statement stat3 = conn.createStatement();
		ResultSet pur = stat3
			.executeQuery("SELECT * FROM purchase WHERE organization_id LIKE '"
				+ org_id + "'");

		NextPurchase: while (pur.next()) {

		    long time = Long.parseLong(pur.getString("date"));
		    Date thisDate = new Date(time);

		    if (thisDate.after(date_finish)
			    && thisDate.before(date_start))
			continue NextPurchase;

		    if (pur.getString("dogovor").equals("false"))
			continue NextPurchase;

		    BigDecimal st = BigDecimal.valueOf(Double.parseDouble(pur
			    .getString("torgi_start_cost")));
		    BigDecimal fn = BigDecimal.valueOf(Double.parseDouble(pur
			    .getString("torgi_finish_cost")));

		    if (st.doubleValue() < 0.010)
			continue NextPurchase;

		    start = start.add(st);
		    finish = finish.add(fn);
		    count++;
		}

		pur.close();
		stat3.close();

		sheet.addCell(new Number(column + 7, thisRow, count,
			font.tahomaValue3));

		sheet.addCell(new Number(column + 8, thisRow, start
			.doubleValue(), font.tahomaValue2));

		sheet.addCell(new Number(column + 9, thisRow, finish
			.doubleValue(), font.tahomaValue2));

		sheet.addCell(new Formula(column + 10, thisRow, ""
			+ getColumnExcel(column + 8)
			+ Integer.toString(thisRow + 1) + " - "
			+ getColumnExcel(column + 9)
			+ Integer.toString(thisRow + 1) + "", font.tahomaValue2));

		long dogovor = 0;
		BigDecimal all_sum = BigDecimal.ZERO;

		Object[] obj = Constant.month_items;

		for (Object m : obj) {

		    // System.out.println(((ComboItemIntValue) m).getValue());

		    Statement stat4 = conn.createStatement();
		    ResultSet rs = stat4
			    .executeQuery("SELECT * FROM report WHERE report_type_id LIKE '1' AND month LIKE '"
				    + ((ComboItemIntValue) m).getValue()
				    + "' AND year LIKE '"
				    + this.year
				    + "' AND organization_id LIKE '"
				    + org_id
				    + "'");

		    while (rs.next()) {
			dogovor += rs.getInt("count_dogovors");

			Double sum = Double.parseDouble(rs.getString("summa"));
			all_sum = all_sum.add(new BigDecimal(sum));
			all_sum = all_sum.divide(BigDecimal.ONE).setScale(2,
				RoundingMode.HALF_UP);
		    }

		    rs.close();
		    stat4.close();

		    if (((ComboItemIntValue) m).getValue() == this.month)
			break;
		}

		sheet.addCell(new Number(column + 11, thisRow, dogovor,
			font.tahomaValue3));

		org_count++;
	    }

	    stat.close();
	    org.close();
	}

	// int thisColumn = column + titles_column.length;
	// int index = 0;
	// for (@SuppressWarnings("unused")
	// Map.Entry<Integer, String> entry : subject.entrySet()) {
	//
	// for (int i = 0; i < 2; i++) {
	// int column_f = thisColumn + index + i;
	// int row1 = row + 2;
	// int row2 = row + org_count;
	//
	// WritableCellFormat thisfont = font.tahomaValue3;
	// if (i != 0)
	// thisfont = font.tahomaValue2;
	//
	// sheet.addCell(new Formula(column_f, row2, "SUM("
	// + getColumnExcel(column_f) + Integer.toString(row1)
	// + ":" + getColumnExcel(column_f)
	// + Integer.toString(row2) + ")", thisfont));
	// }
	//
	// index += 2;
	// }

	workbook.write();
	workbook.close();
	conn.close();
	JOptionPane.showMessageDialog(null, "Готово");
    }

    /**
     * Вычисляет символьное представления индекса колонки
     * 
     * @param value
     *            - цифровой индекс колонки(33)
     * @return текстовый индекс колонки(AF)
     */
    private String getColumnExcel(Integer value) {
	// промежуточный результат
	String result = "";
	// для определения первого символа
	boolean first = true;

	while (value / 26 > 0) {
	    if (first) {
		result += (char) (65 + value % 26);
		first = false;
	    } else {
		result += (char) (64 + value % 26);
	    }

	    value = value / 26;
	}

	if (first) {
	    result += (char) (65 + value % 26);
	} else {
	    result += (char) (64 + value % 26);
	}

	// переварачиваем результат EFA = > AFE
	String res = "";

	for (int i = 0; i < result.length(); i++) {
	    res += result.substring(result.length() - i - 1, result.length()
		    - i);
	}

	return res;
    }

}
