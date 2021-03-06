package ru.fiko.purchase.reports;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import ru.fiko.purchase.Constant;
import ru.fiko.purchase.supports.JXLConstant;

import jxl.Workbook;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Number3or4 {

    String dir = "Отчеты";

    int year = 2013;
    int month = 0;
    Vector<Integer> org_ids;

    Date date_finish = new Date(System.currentTimeMillis());
    Date date_start = new Date(System.currentTimeMillis());

    String type = "subject";
    int number = 3;

    @SuppressWarnings("unchecked")
    public Number3or4(Date date, int number, Vector<Integer> ids) {

	org_ids = (Vector<Integer>) ids.clone();

	if (number == 4) {
	    type = "type";
	    this.number = 4;
	}

	SimpleDateFormat formatter = new SimpleDateFormat("dd.MMMM.yyyy");
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");

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

	try {
	    create();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void create() throws IOException, WriteException,
	    ClassNotFoundException, SQLException {
	int row = 2;
	int column = 2;

	JXLConstant font = new JXLConstant();

	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Constant.PATHTODB);

	new File(dir).mkdirs();

	SimpleDateFormat formatter = new SimpleDateFormat("dd.MMMM.yyyy");

	WritableWorkbook workbook = Workbook.createWorkbook(new File(dir + "/"
		+ "Отчет №" + this.number + "("
		+ formatter.format(new Date(System.currentTimeMillis()))
		+ ").xls"));

	WritableSheet sheet = workbook.createSheet("Отчет №" + this.number, 0);

	String title = "Таблица о завершенных процедурах размещения заказа для государственных нужд по видам продукции по организациям.(с сортировкой по видам организаций)"
		+ formatter.format(this.date_finish);

	sheet.addCell(new Label(column, row, title, font.tahomaTitleValue));
	sheet.mergeCells(column, row, column + 4, row);
	sheet.setRowView(row, 450);

	String[] titles_column = {
		"№ п/п",
		"Заказчик(Наименование организации)",
		"ИНН" };

	for (int i = 0; i < titles_column.length; i++) {
	    sheet.addCell(new Label(column + i, row + 1, titles_column[i],
		    font.tahomaTitleValue));
	    sheet.mergeCells(column + i, row + 1, column + i, row + 2);
	}
	sheet.setRowView(row + 1, 800);
	sheet.setRowView(row + 2, 450);

	int[] columnsView = { 7, 60, 20 };
	for (int i = 0; i < columnsView.length; i++)
	    sheet.setColumnView(column + i, columnsView[i]);

	Map<Integer, String> subject = new HashMap<Integer, String>();

	Statement stat = conn.createStatement();
	ResultSet rs = stat.executeQuery("SELECT * FROM " + type);

	while (rs.next())
	    subject.put(rs.getInt("id"), rs.getString("title"));

	rs.close();
	stat.close();

	subject.put(999, "Всего");

	int columnIndex = titles_column.length;
	for (Map.Entry<Integer, String> entry : subject.entrySet()) {
	    int thisColumn = column + columnIndex;
	    int thisRow = row + 1;

	    sheet.addCell(new Label(thisColumn, thisRow, entry.getValue(),
		    font.tahomaTitleValue));
	    sheet.mergeCells(thisColumn, thisRow, thisColumn + 1, thisRow);

	    sheet.addCell(new Label(thisColumn, thisRow + 1, "кол-во",
		    font.tahomaTitleValue));

	    sheet.addCell(new Label(thisColumn + 1, thisRow + 1, "сумма",
		    font.tahomaTitleValue));

	    columnIndex += 2;
	}

	row += 2;
	int org_count = 1;

	for (int id : org_ids) {

	    stat = conn.createStatement();
	    ResultSet org = stat
		    .executeQuery("SELECT id, name, inn FROM organization WHERE id LIKE '"
			    + id + "'");
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

		String[] formula = new String[2];
		for (int index = 0; index < formula.length; index++)
		    formula[index] = "";

		int thisColumn = column + titles_column.length;
		for (Map.Entry<Integer, String> entry : subject.entrySet()) {

		    sheet.setColumnView(thisColumn + 1, 20);

		    if (entry.getKey().equals(999)) {

			for (int index = 0; index < formula.length; index++) {

			    WritableCellFormat thisfont = font.tahomaValue3;
			    if (index != 0)
				thisfont = font.tahomaValue2;

			    sheet.addCell(new Formula(thisColumn + index,
				    thisRow, "SUM(" + formula[index] + ")",
				    thisfont));
			    formula[index] = "";
			}

		    } else {

			Double count = 0.0;
			BigDecimal start = BigDecimal.ZERO;
			BigDecimal finish = BigDecimal.ZERO;

			Statement stat3 = conn.createStatement();
			ResultSet pur = stat3
				.executeQuery("SELECT * FROM purchase WHERE "
					+ type + "_id LIKE '" + entry.getKey()
					+ "' AND organization_id LIKE '"
					+ org_id + "'");

			NextPurchase: while (pur.next()) {

			    long time = Long.parseLong(pur.getString("date"));
			    Date thisDate = new Date(time);

			    if (thisDate.after(date_finish)
				    && thisDate.before(date_start))
				continue NextPurchase;

			    if (pur.getString("dogovor").equals("false"))
				continue NextPurchase;

			    BigDecimal st = BigDecimal.valueOf(Double
				    .parseDouble(pur
					    .getString("torgi_start_cost")));
			    BigDecimal fn = BigDecimal.valueOf(Double
				    .parseDouble(pur
					    .getString("torgi_finish_cost")));

			    if (st.doubleValue() < 0.010)
				continue NextPurchase;

			    start = start.add(st);
			    finish = finish.add(fn);
			    count++;
			}

			pur.close();
			stat3.close();

			sheet.addCell(new Number(thisColumn, thisRow, count,
				font.tahomaValue3));

			sheet.addCell(new Number(thisColumn + 1, thisRow,
				finish.doubleValue(), font.tahomaValue2));

			// sheet.addCell(new Formula(column + 4, row, ""
			// + getColumnExcel(column + 2)
			// + Integer.toString(row + 1) + " - "
			// + getColumnExcel(column + 3)
			// + Integer.toString(row + 1) + "",
			// font.tahomaValue2));

			for (int index = 0; index < formula.length; index++) {
			    String dot = ",";
			    if (formula[index].equals(""))
				dot = "";

			    formula[index] += dot
				    + getColumnExcel(thisColumn + index)
				    + Integer.toString(thisRow + 1);
			}
		    }
		    thisColumn += 2;
		}

		org_count++;
	    }

	    stat.close();
	    org.close();
	}

	int thisColumn = column + titles_column.length;
	int index = 0;
	for (@SuppressWarnings("unused")
	Map.Entry<Integer, String> entry : subject.entrySet()) {

	    for (int i = 0; i < 2; i++) {
		int column_f = thisColumn + index + i;
		int row1 = row + 2;
		int row2 = row + org_count;

		WritableCellFormat thisfont = font.tahomaValue3;
		if (i != 0)
		    thisfont = font.tahomaValue2;

		sheet.addCell(new Formula(column_f, row2, "SUM("
			+ getColumnExcel(column_f) + Integer.toString(row1)
			+ ":" + getColumnExcel(column_f)
			+ Integer.toString(row2) + ")", thisfont));
	    }

	    index += 2;
	}

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
