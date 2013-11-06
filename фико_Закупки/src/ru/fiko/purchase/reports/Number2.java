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

public class Number2 {

    String dir = "Отчеты";

    int year = 2013;
    int month = 0;
    Date date_finish = new Date(System.currentTimeMillis());
    Date date_start = new Date(System.currentTimeMillis());

    public Number2(Date date) {

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
		+ "Отчет №2("
		+ formatter.format(new Date(System.currentTimeMillis()))
		+ ").xls"));

	WritableSheet sheet = workbook.createSheet("Отчет №2", 0);

	String title = "Сведения о завершенных процедурах размещения заказа для государственных нужд по видам продукции до "
		+ formatter.format(this.date_finish);

	sheet.addCell(new Label(column, row, title, font.tahomaTitleValue));
	sheet.mergeCells(column, row, column + 4, row);
	sheet.setRowView(row, 450);

	String[] titles_column = {
		"Наименование оценочных показателей",
		"Кол-во закупок",
		"Максимальная цена",
		"Цена закупки",
		"Полученная экономия" };

	for (int i = 0; i < titles_column.length; i++)
	    sheet.addCell(new Label(column + i, row + 1, titles_column[i],
		    font.tahomaTitleValue));

	sheet.setRowView(row + 1, 450);

	int[] columnsView = { 30, 10, 25, 25, 25 };
	for (int i = 0; i < columnsView.length; i++)
	    sheet.setColumnView(column + i, columnsView[i]);

	String[][] titles_row = {
		{ "1. По видам закупаемой продукции:", "" },
		{ "- товары", "WHERE type_id LIKE '2'" },
		{ "- работы", "WHERE type_id LIKE '4'" },
		{ "- услуги", "WHERE type_id LIKE '3'" },
		{ "Всего:", "all" },
		{ "2. По наименованиям закупаемой продукции", "" },
		{ "- медоборудование", "WHERE subject_id LIKE '2'" },
		{
			"- компьютерная, вычислительная и пр. техника",
			"WHERE subject_id LIKE '4'" },
		{ "- проектные работы", "WHERE subject_id LIKE '5'" },
		{ "- капремонт", "WHERE subject_id LIKE '3'" },
		{ "- лекарственные средства", "WHERE subject_id LIKE '12'" },
		{ "- продукты питания", "WHERE subject_id LIKE '6'" },
		{ "Всего:", "all" } };

	String[] formula = new String[4];
	for (int index = 0; index < formula.length; index++)
	    formula[index] = "";

	for (int i = 0; i < titles_row.length; i++) {
	    int thisRow = row + 2 + i;
	    String thisTitle = titles_row[i][0];
	    String sql = titles_row[i][1];

	    sheet.addCell(new Label(column, thisRow, thisTitle,
		    font.tahomaTitleValueLeft));

	    int rowView = 250;

	    if (thisTitle.length() > 20)
		rowView = 450;

	    sheet.setRowView(thisRow, rowView);

	    if (thisTitle.indexOf("1.") >= 0 || thisTitle.indexOf("2.") >= 0)
		sheet.mergeCells(column, thisRow, column + 4, thisRow);

	    if (sql.equals("all")) {

		for (int index = 0; index < formula.length; index++) {

		    WritableCellFormat thisfont = font.tahomaValue3;
		    if (index != 0)
			thisfont = font.tahomaValue2;

		    sheet.addCell(new Formula(column + 1 + index, thisRow,
			    "SUM(" + formula[index] + ")", thisfont));
		    formula[index] = "";
		}
	    } else if (sql.equals("") != true) {

		Double count = 0.0;
		BigDecimal start = BigDecimal.ZERO;
		BigDecimal finish = BigDecimal.ZERO;

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("SELECT * FROM purchase "
			+ sql);

		NextPurchase: while (rs.next()) {

		    long time = Long.parseLong(rs.getString("date"));
		    Date thisDate = new Date(time);

		    if (thisDate.after(date_finish)
			    && thisDate.before(date_start))
			continue NextPurchase;

		    BigDecimal st = BigDecimal.valueOf(Double.parseDouble(rs
			    .getString("torgi_start_cost")));
		    BigDecimal fn = BigDecimal.valueOf(Double.parseDouble(rs
			    .getString("torgi_finish_cost")));

		    if (st.doubleValue() < 0.010)
			continue NextPurchase;

		    start = start.add(st);
		    finish = finish.add(fn);
		    count++;
		}

		rs.close();
		stat.close();

		sheet.addCell(new Number(column + 1, thisRow, count,
			font.tahomaValue3));

		sheet.addCell(new Number(column + 2, thisRow, start
			.doubleValue(), font.tahomaValue2));

		sheet.addCell(new Number(column + 3, thisRow, finish
			.doubleValue(), font.tahomaValue2));

		sheet.addCell(new Formula(column + 4, thisRow, ""
			+ getColumnExcel(column + 2)
			+ Integer.toString(thisRow + 1) + " - "
			+ getColumnExcel(column + 3)
			+ Integer.toString(thisRow + 1) + "", font.tahomaValue2));

		for (int index = 0; index < formula.length; index++) {
		    String dot = ",";
		    if (formula[index].equals(""))
			dot = "";

		    formula[index] += dot + getColumnExcel(column + 1 + index)
			    + Integer.toString(thisRow + 1);
		}
	    }
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
