package output;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import basedata.ConnectionBD;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ToExcelSetev {
    public ToExcelSetev(String year) {
	WorkbookSettings ws = new WorkbookSettings();
	ws.setLocale(new Locale("ru", "RU"));

	try {
	    String dt = new SimpleDateFormat("dd.MM.yy").format(Calendar
		    .getInstance().getTime());

	    // создание книги
	    WritableWorkbook workbook = Workbook.createWorkbook(new File(
		    "Сетевые орг. - структура факт. сети  " + year + "(" + dt
			    + ").xls"), ws);

	    Vector<String> inn = new ConnectionBD().getINN(year);

	    for (int i = 0; i < inn.size(); i = i + 2) {

		/*
		 * Основной формат ячеек Tahoma 9pt, no bold выравнивание по
		 * горизонтале: центр выравнивание по вертикале: центр перенос
		 * по словам стиль границы - все цвет фона - без цвета
		 */
		WritableCellFormat tahoma9pt = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9pt.setAlignment(Alignment.CENTRE);
		tahoma9pt.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9pt.setWrap(true);
		tahoma9pt.setBorder(Border.ALL, BorderLineStyle.THIN);

		/*
		 * формат ячеек зелёного цвета Tahoma 9pt, no bold выравнивание
		 * по горизонтале: по правому краю выравнивание по вертикале:
		 * центр перенос по словам стиль границы - все цвет фона -
		 * легкий зелёный
		 */
		WritableCellFormat tahoma9ptGreen = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9ptGreen.setAlignment(Alignment.RIGHT);
		tahoma9ptGreen.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptGreen.setWrap(true);
		tahoma9ptGreen.setBorder(Border.ALL, BorderLineStyle.THIN);
		tahoma9ptGreen.setBackground(Colour.LIGHT_GREEN);

		WritableCellFormat tahoma9ptRed = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9ptRed.setAlignment(Alignment.RIGHT);
		tahoma9ptRed.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptRed.setWrap(true);
		tahoma9ptRed.setBorder(Border.ALL, BorderLineStyle.THIN);
		tahoma9ptRed.setBackground(Colour.RED);

		WritableCellFormat tahoma9ptORANGE = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9ptORANGE.setAlignment(Alignment.RIGHT);
		tahoma9ptORANGE.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptORANGE.setWrap(true);
		tahoma9ptORANGE.setBorder(Border.ALL, BorderLineStyle.THIN);
		tahoma9ptORANGE.setBackground(Colour.LIGHT_ORANGE);

		/*
		 * формат ячеек жёлтого цвета Tahoma 9pt, no bold выравнивание
		 * по горизонтале: по правому краю выравнивание по вертикале:
		 * центр перенос по словам стиль границы - все цвет фона -
		 * легкий жёлтый
		 */
		WritableCellFormat tahoma9ptYellow = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9ptYellow.setAlignment(Alignment.RIGHT);
		tahoma9ptYellow.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptYellow.setWrap(true);
		tahoma9ptYellow.setBorder(Border.ALL, BorderLineStyle.THIN);
		tahoma9ptYellow.setBackground(Colour.VERY_LIGHT_YELLOW);

		/*
		 * Основной с выравниванием по левому краю Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по левому краю выравнивание по
		 * вертикале: центр перенос по словам стиль границы: все цвет
		 * фона: без цвета
		 */
		WritableCellFormat tahoma9ptLeft = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.NO_BOLD));
		tahoma9ptLeft.setAlignment(Alignment.LEFT);
		tahoma9ptLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptLeft.setWrap(true);
		tahoma9ptLeft.setBorder(Border.ALL, BorderLineStyle.THIN);

		/*
		 * Основной с выравниванием по центру без рамки Tahoma 9pt, no
		 * bold выравнивание по горизонтале: центр выравнивание по
		 * вертикале: центр перенос по словам стиль границы: без рамки
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahoma12ptNoBold = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 12,
				WritableFont.NO_BOLD));
		tahoma12ptNoBold.setAlignment(Alignment.CENTRE);
		tahoma12ptNoBold.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma12ptNoBold.setWrap(true);
		tahoma12ptNoBold.setBorder(null, null);

		/*
		 * Основной с выравниванием по центру без рамки Tahoma 9pt, no
		 * bold выравнивание по горизонтале: центр выравнивание по
		 * вертикале: центр перенос по словам стиль границы: без рамки
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahoma12ptBold = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 12,
				WritableFont.BOLD));
		tahoma12ptBold.setAlignment(Alignment.CENTRE);
		tahoma12ptBold.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma12ptBold.setWrap(true);
		tahoma12ptBold.setBorder(Border.ALL, BorderLineStyle.THIN);

		/*
		 * Основной жирный c серым оттенком, по левому краю Tahoma 9pt,
		 * bold выравнивание по горизонтале: по левому краю выравнивание
		 * по вертикале: центр перенос по словам стиль границы: все цвет
		 * фона: 25% серого
		 */
		WritableCellFormat tahoma9ptLeftBoldGray = new WritableCellFormat(
			new WritableFont(WritableFont.TAHOMA, 9,
				WritableFont.BOLD));
		tahoma9ptLeftBoldGray.setAlignment(Alignment.LEFT);
		tahoma9ptLeftBoldGray
			.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptLeftBoldGray.setWrap(true);
		tahoma9ptLeftBoldGray.setBorder(Border.ALL,
			BorderLineStyle.THIN);
		tahoma9ptLeftBoldGray.setBackground(Colour.GRAY_25);
		/*
		 * Получение названия организации
		 */
		String name = inn.get(i + 1);
		/*
		 * макс длина названия листа 32 символа
		 */
		if (name.length() > 31) {
		    name = name.substring(0, 31);
		}
		/*
		 * новый лист
		 */
		WritableSheet sheet = workbook.createSheet(name, i);

		sheet.addCell(new Label(
			0,
			2,
			"Сведения об отпуске (передаче) электроэнергии распределительными сетевыми организациями отдельным категориям потребителей",
			tahoma12ptNoBold));

		sheet.addCell(new Label(0, 4, inn.get(i + 1), tahoma12ptNoBold));

		for (int p = 0; p < 2; p++) {
		    sheet.addCell(new Label(0, 5 + p * 52,
			    "Наименование показателя", tahoma9pt));
		    sheet.addCell(new Label(1, 5 + p * 52, "Код строки",
			    tahoma9pt));

		    sheet.addCell(new Label(0, 6 + p * 52,
			    "Электроэнергия (тыс. кВт•ч)",
			    tahoma9ptLeftBoldGray));
		    sheet.mergeCells(0, 6 + p * 52, 31 + p * 10, 6 + p * 52);

		    sheet.addCell(new Label(
			    0,
			    7 + p * 52,
			    "Поступление в сеть из других организаций, в том числе: ",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 8 + p * 52, "  - из сетей ФСК",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 9 + p * 52,
			    "  - от генерирующих компаний и блок-станций",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    10 + p * 52,
			    "Поступление в сеть из других уровней напряжения (трансформация)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 11 + p * 52, "ВН	",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 12 + p * 52, "СН1",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 13 + p * 52, "СН2",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 14 + p * 52, "НН", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 15 + p * 52,
			    "Отпуск из сети, в том числе: ", tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    16 + p * 52,
			    "  - конечные потребители (кроме совмещающих с передачей)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 17 + p * 52, "  - другие сети",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 18 + p * 52, "  - поставщики",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 19 + p * 52,
			    "Отпуск в сеть других уровней напряжения",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 20 + p * 52,
			    "Хозяйственные нужды сети", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 21 + p * 52,
			    "Потери, в том числе:", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 22 + p * 52,
			    "  - относимые на собственное потребление ",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    23 + p * 52,
			    "Генерация на установках организации (совмещение деятельности)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    24 + p * 52,
			    "Собственное потребление (совмещение деятельности)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 25 + p * 52, "Небаланс",
			    tahoma9ptLeft));

		    sheet.addCell(new Label(0, 26 + p * 52,
			    "Мощность (МВт) <*>", tahoma9ptLeftBoldGray));
		    sheet.mergeCells(0, 26 + p * 52, 31 + p * 10, 26 + p * 52);

		    sheet.addCell(new Label(
			    0,
			    27 + p * 52,
			    "Поступление в сеть из других организаций, в том числе: ",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 28 + p * 52, "  - из сетей ФСК",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 29 + p * 52,
			    "  - от генерирующих компаний и блок-станций",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    30 + p * 52,
			    "Поступление в сеть из других уровней напряжения (трансформация)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 31 + p * 52, "ВН", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 32 + p * 52, "СН1",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 33 + p * 52, "СН2",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 34 + p * 52, "НН", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 35 + p * 52,
			    "Отпуск из сети, в том числе: ", tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    36 + p * 52,
			    "  - конечные потребители (кроме совмещающих с передачей)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 37 + p * 52, "  - другие сети",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 38 + p * 52, "  - поставщики",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 39 + p * 52,
			    "Отпуск в сеть других уровней напряжения",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 40 + p * 52,
			    "Хозяйственные нужды сети", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 41 + p * 52,
			    "Потери, в том числе:", tahoma9ptLeft));
		    sheet.addCell(new Label(0, 42 + p * 52,
			    "  - относимые на собственное потребление ",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    43 + p * 52,
			    "Генерация на установках организации (совмещение деятельности)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    44 + p * 52,
			    "Собственное потребление (совмещение деятельности)",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 45 + p * 52, "Небаланс",
			    tahoma9ptLeft));

		    sheet.addCell(new Label(0, 46 + p * 52,
			    "Заявленная и присоединенная мощность (МВт)",
			    tahoma9ptLeftBoldGray));
		    sheet.mergeCells(0, 46 + p * 52, 31 + p * 10, 46 + p * 52);

		    sheet.addCell(new Label(0, 47 + p * 52,
			    "Заявленная мощность конечных потребителей ",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(0, 48 + p * 52,
			    "Присоединенная мощность конечных потребителей",
			    tahoma9ptLeft));

		    sheet.addCell(new Label(0, 49 + p * 52,
			    "Платежи, тыс. руб.", tahoma9ptLeftBoldGray));
		    sheet.mergeCells(0, 49 + p * 52, 31 + p * 10, 49 + p * 52);

		    sheet.addCell(new Label(
			    0,
			    50 + p * 52,
			    "Стоимость поставленных организацией услуг по передаче услуг по передаче",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    51 + p * 52,
			    "Стоимость приобретенных организацией услуг по передаче",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    52 + p * 52,
			    "Поступления денежных средств в счет стоимости поставленных услуг по передаче",
			    tahoma9ptLeft));
		    sheet.addCell(new Label(
			    0,
			    53 + p * 52,
			    "Уплата денежных средств в счет стоимости приобретенных услуг по передаче",
			    tahoma9ptLeft));

		    sheet.addCell(new Label(1, 7 + p * 52, "10", tahoma9pt));
		    sheet.addCell(new Label(1, 8 + p * 52, "20", tahoma9pt));
		    sheet.addCell(new Label(1, 9 + p * 52, "30", tahoma9pt));
		    sheet.addCell(new Label(1, 10 + p * 52, "40", tahoma9pt));
		    sheet.addCell(new Label(1, 11 + p * 52, "50", tahoma9pt));
		    sheet.addCell(new Label(1, 12 + p * 52, "60", tahoma9pt));
		    sheet.addCell(new Label(1, 13 + p * 52, "70", tahoma9pt));
		    sheet.addCell(new Label(1, 14 + p * 52, "80", tahoma9pt));
		    sheet.addCell(new Label(1, 15 + p * 52, "90", tahoma9pt));
		    sheet.addCell(new Label(1, 16 + p * 52, "100", tahoma9pt));
		    sheet.addCell(new Label(1, 17 + p * 52, "110", tahoma9pt));
		    sheet.addCell(new Label(1, 18 + p * 52, "120", tahoma9pt));
		    sheet.addCell(new Label(1, 19 + p * 52, "130", tahoma9pt));
		    sheet.addCell(new Label(1, 20 + p * 52, "140", tahoma9pt));
		    sheet.addCell(new Label(1, 21 + p * 52, "150", tahoma9pt));
		    sheet.addCell(new Label(1, 22 + p * 52, "160", tahoma9pt));
		    sheet.addCell(new Label(1, 23 + p * 52, "170", tahoma9pt));
		    sheet.addCell(new Label(1, 24 + p * 52, "180", tahoma9pt));
		    sheet.addCell(new Label(1, 25 + p * 52, "190", tahoma9pt));

		    sheet.addCell(new Label(1, 27 + p * 52, "210", tahoma9pt));
		    sheet.addCell(new Label(1, 28 + p * 52, "220", tahoma9pt));
		    sheet.addCell(new Label(1, 29 + p * 52, "230", tahoma9pt));
		    sheet.addCell(new Label(1, 30 + p * 52, "240", tahoma9pt));
		    sheet.addCell(new Label(1, 31 + p * 52, "250", tahoma9pt));
		    sheet.addCell(new Label(1, 32 + p * 52, "260", tahoma9pt));
		    sheet.addCell(new Label(1, 33 + p * 52, "270", tahoma9pt));
		    sheet.addCell(new Label(1, 34 + p * 52, "280", tahoma9pt));
		    sheet.addCell(new Label(1, 35 + p * 52, "290", tahoma9pt));
		    sheet.addCell(new Label(1, 36 + p * 52, "300", tahoma9pt));
		    sheet.addCell(new Label(1, 37 + p * 52, "310", tahoma9pt));
		    sheet.addCell(new Label(1, 38 + p * 52, "320", tahoma9pt));
		    sheet.addCell(new Label(1, 39 + p * 52, "330", tahoma9pt));
		    sheet.addCell(new Label(1, 40 + p * 52, "340", tahoma9pt));
		    sheet.addCell(new Label(1, 41 + p * 52, "350", tahoma9pt));
		    sheet.addCell(new Label(1, 42 + p * 52, "360", tahoma9pt));
		    sheet.addCell(new Label(1, 43 + p * 52, "370", tahoma9pt));
		    sheet.addCell(new Label(1, 44 + p * 52, "380", tahoma9pt));
		    sheet.addCell(new Label(1, 45 + p * 52, "390", tahoma9pt));

		    sheet.addCell(new Label(1, 47 + p * 52, "400", tahoma9pt));
		    sheet.addCell(new Label(1, 48 + p * 52, "410", tahoma9pt));

		    sheet.addCell(new Label(1, 50 + p * 52, "500", tahoma9pt));
		    sheet.addCell(new Label(1, 51 + p * 52, "520", tahoma9pt));
		    sheet.addCell(new Label(1, 52 + p * 52, "530", tahoma9pt));
		    sheet.addCell(new Label(1, 53 + p * 52, "540", tahoma9pt));
		}

		sheet.mergeCells(0, 2, 10, 2);
		sheet.mergeCells(0, 4, 1, 4);

		sheet.setRowView(2, 750);
		sheet.setRowView(4, 750);

		for (int p = 5; p < 106; p++) {
		    sheet.setRowView(p, 450);
		}

		for (int p = 2; p < 5 * 8 + 2; p++) {
		    sheet.setColumnView(p, 15);
		}

		sheet.setColumnView(0, 50);

		String[] months = { "январь", "февраль", "март", "апрель",
			"май", "июнь", "июль", "август", "сентябрь", "октябрь",
			"ноябрь", "декабрь" };

		Vector<Vector<String>> done = new Vector<Vector<String>>(44, 1);
		Vector<Vector<Double>> done_num = new Vector<Vector<Double>>(
			44, 1);

		for (int v = 0; v < 44; v++) {
		    Vector<String> element = new Vector<String>(5);
		    Vector<Double> el_num = new Vector<Double>(5);
		    for (int r = 0; r < 5; r++) {
			element.add("");
			el_num.add((Double) 0.0);
		    }
		    done.add(element);
		    done_num.add(el_num);
		}

		for (int p = 0; p < months.length; p++) {
		    // смещение по строчно
		    int x = p;
		    // смещение по столбцам
		    int y = 0;

		    if (p > 5) {
			x = x - 6;
			y = 1;
		    }

		    sheet.addCell(new Label(2 + x * 5, 5 + y * 52, "Всего",
			    tahoma9pt));
		    sheet.addCell(new Label(3 + x * 5, 5 + y * 52, "ВН",
			    tahoma9pt));
		    sheet.addCell(new Label(4 + x * 5, 5 + y * 52, "СН1",
			    tahoma9pt));
		    sheet.addCell(new Label(5 + x * 5, 5 + y * 52, "СН2",
			    tahoma9pt));
		    sheet.addCell(new Label(6 + x * 5, 5 + y * 52, "НН",
			    tahoma9pt));
		    sheet.addCell(new Label(2 + x * 5, 4 + y * 52, months[p],
			    tahoma12ptBold));

		    sheet.mergeCells(2 + x * 5, 4 + y * 52, 6 + x * 5,
			    4 + y * 52);

		    /*
		     * Заполнение формул
		     */

		    // количество добавочных строк
		    int add_y = 0;

		    for (int res_i = 0; res_i < done.size(); res_i++) {
			if (res_i == 19 || res_i == 38 || res_i == 40) {
			    // пропуск строки
			    add_y++;
			}

			for (int res_p = 0; res_p < done.get(res_i).size(); res_p++) {
			    String res = "";
			    if (done.get(res_i).get(res_p).equals("")) {
				// первая ячейка в формуле
				res = getColumnExcel(2 + res_p + x * 5)
					+ Integer.toString(8 + res_i + add_y
						+ y * 52);
			    } else {
				// дополнительная ячейка в формуле
				res = done.get(res_i).get(res_p)
					+ " + "
					+ getColumnExcel(2 + res_p + x * 5)
					+ Integer.toString(8 + res_i + add_y
						+ y * 52);
			    }
			    done.get(res_i).set(res_p, res);
			}
		    }

		    /*
		     * Вывод результата
		     */

		    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
		    @SuppressWarnings({ "rawtypes" })
		    Vector<Vector> result = new ConnectionBD().getInfo(
			    inn.get(i).toString(), months[p], year);
		    

		    // количество добавочных строк
		    add_y = 0;
		    for (int res_i = 0; res_i < result.size(); res_i++) {
			if (res_i == 19 || res_i == 38 || res_i == 40) {
			    // пропуск строки
			    add_y++;
			}

			// "Всего"
			sheet.addCell(new Label(2 + x * 5, 7 + res_i + add_y
				+ y * 52, toNumberString(result.get(res_i)
				.get(0).toString()), tahoma9ptGreen));

			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);

			for (int res_p = 1; res_p < result.get(res_i).size(); res_p++) {
			    // остальные
			    sheet.addCell(new Label(2 + res_p + x * 5, 7
				    + res_i + add_y + y * 52,
				    toNumberString(result.get(res_i).get(res_p)
					    .toString()), tahoma9ptYellow));

			    sum = new BigDecimal(done_num.get(res_i).get(res_p)
				    + parseStringToDouble(result.get(res_i)
					    .get(res_p).toString())).setScale(
				    4, RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(res_p, sum);
			}
		    }
		}

		/*
		 * Итог
		 */

		{
		    int x = 6;
		    int y = 1;
		    int add_y = 0;

		    sheet.addCell(new Label(2 + x * 5, 5 + y * 52, "Всего",
			    tahoma9pt));
		    sheet.addCell(new Label(3 + x * 5, 5 + y * 52, "ВН",
			    tahoma9pt));
		    sheet.addCell(new Label(4 + x * 5, 5 + y * 52, "СН1",
			    tahoma9pt));
		    sheet.addCell(new Label(5 + x * 5, 5 + y * 52, "СН2",
			    tahoma9pt));
		    sheet.addCell(new Label(6 + x * 5, 5 + y * 52, "НН",
			    tahoma9pt));
		    sheet.addCell(new Label(2 + x * 5, 4 + y * 52, "Итог",
			    tahoma12ptBold));

		    sheet.mergeCells(2 + x * 5, 4 + y * 52, 6 + x * 5,
			    4 + y * 52);

		    for (int res_i = 0; res_i < done.size(); res_i++) {
			if (res_i == 19 || res_i == 38 || res_i == 40) {
			    // пропуск строки
			    add_y++;
			}

			// всего
			sheet.addCell(new Formula(2 + x * 5, 7 + res_i + add_y
				+ y * 52, "SUM("
				+ done.get(res_i).get(0).toString() + ")",
				tahoma9ptGreen));

			for (int res_p = 1; res_p < done.get(res_i).size(); res_p++) {
			    // остальные
			    sheet.addCell(new Formula(2 + res_p + x * 5, 7
				    + res_i + add_y + y * 52, "SUM("
				    + done.get(res_i).get(res_p).toString()
				    + ")", tahoma9ptYellow));
			}
		    }
		}

		/*
		 * ГОД
		 */

		@SuppressWarnings({ "rawtypes" })
		Vector<Vector> result = new ConnectionBD().getInfo(inn.get(i)
			.toString(), "год", year);

		int x = 7;
		int y = 1;
		int add_y = 0;

		sheet.addCell(new Label(2 + x * 5, 5 + y * 52, "Всего",
			tahoma9pt));
		sheet.addCell(new Label(3 + x * 5, 5 + y * 52, "ВН", tahoma9pt));
		sheet.addCell(new Label(4 + x * 5, 5 + y * 52, "СН1", tahoma9pt));
		sheet.addCell(new Label(5 + x * 5, 5 + y * 52, "СН2", tahoma9pt));
		sheet.addCell(new Label(6 + x * 5, 5 + y * 52, "НН", tahoma9pt));
		sheet.addCell(new Label(2 + x * 5, 4 + y * 52, "Год",
			tahoma12ptBold));

		sheet.mergeCells(2 + x * 5, 4 + y * 52, 6 + x * 5, 4 + y * 52);

		for (int res_i = 0; res_i < result.size(); res_i++) {
		    if (res_i == 19 || res_i == 38 || res_i == 40) {
			// пропуск строки
			add_y++;
		    }
		    Double res = parseStringToDouble(result.get(res_i).get(0)
			    .toString());

		    if (res.equals(done_num.get(res_i).get(0))) {
			// всего
			sheet.addCell(new Label(2 + x * 5, 7 + res_i + add_y
				+ y * 52, toNumberString(result.get(res_i)
				.get(0).toString()), tahoma9ptGreen));
		    } else {
			sheet.addCell(new Label(2 + x * 5, 7 + res_i + add_y
				+ y * 52, toNumberString(result.get(res_i)
				.get(0).toString()), tahoma9ptRed));
		    }

		    for (int res_p = 1; res_p < done.get(res_i).size(); res_p++) {
			res = parseStringToDouble(result.get(res_i).get(res_p)
				.toString());

			if (res.equals(done_num.get(res_i).get(res_p))) {
			    // остальные
			    sheet.addCell(new Label(2 + res_p + x * 5, 7
				    + res_i + add_y + y * 52,
				    toNumberString(result.get(res_i).get(res_p)
					    .toString()), tahoma9ptYellow));
			} else {
			    sheet.addCell(new Label(2 + res_p + x * 5, 7
				    + res_i + add_y + y * 52,
				    toNumberString(result.get(res_i).get(res_p)
					    .toString()), tahoma9ptORANGE));
			}
		    }
		}
	    }
	    JOptionPane.showMessageDialog(null, "Сетевые готовы");
	    // закрываем книгу
	    workbook.write();
	    workbook.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
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

    private String toNumberString(String value) {
	// проверяем на наличие информации в ячейке
	if (value != null) {
	    // если что-то есть, возвращаем строковок представление
	    value = value.replace(" ", "");
	    return value;
	}
	// пустая строка, то возвращаем пустую строку
	return "";
    }

    private Double parseStringToDouble(String value) {
	if (value != null) {
	    value = value.replace(" ", "");
	    value = value.replace(" ", "");
	    value = value.replace(",", ".");

	    try {
		return Double.parseDouble(value);
	    } catch (Exception e) {
		return 0.0;
	    }
	}

	return 0.0;
    }
}
