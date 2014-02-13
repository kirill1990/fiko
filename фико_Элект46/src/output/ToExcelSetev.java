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

		    int dy = p * 80;

		    int col = 5 + dy;

		    sheet.addCell(new Label(0, col, "Наименование показателя",
			    tahoma9pt));
		    sheet.addCell(new Label(1, col, "Код строки", tahoma9pt));
		    col++;

		    for (int column = 1; column < 93; column++) {
			if (column == 22)
			    column = 30;
			if (column == 51)
			    column = 60;
			if (column == 63)
			    column = 70;

			String text = "";

			if (column == 1)
			    text = "Электроэнергия (тыс. кВт•ч)";
			if (column == 30)
			    text = "Мощность (МВт)";
			if (column == 60)
			    text = "Мощность (МВт)";
			if (column == 70)
			    text = "Фактический полезный отпуск конечным потребителям (тыс кВт ч)";
			if (column == 80)
			    text = "Стоимость услуг (тыс руб)";

			if (text.equals("") != true) {
			    sheet.addCell(new Label(0, col, text,
				    tahoma9ptLeftBoldGray));
			    sheet.mergeCells(0, col, 31 + p * 10, col);
			    col++;
			}

			sheet.addCell(new Label(0, col, getStringCode(Integer
				.toString(column) + "0"), tahoma9ptLeft));
			sheet.addCell(new Label(1, col, Integer
				.toString(column) + "0", tahoma9pt));
			col++;
		    }
		}

		sheet.mergeCells(0, 2, 10, 2);
		sheet.mergeCells(0, 4, 1, 4);

		sheet.setRowView(2, 750);
		sheet.setRowView(4, 750);

		for (int p = 5; p < 10 + 80 * 2; p++) {
		    sheet.setRowView(p, 450);
		}

		for (int p = 2; p < 5 * 8 + 2; p++) {
		    sheet.setColumnView(p, 15);
		}

		sheet.setColumnView(0, 50);

		String[] months = {
			"январь",
			"февраль",
			"март",
			"апрель",
			"май",
			"июнь",
			"июль",
			"август",
			"сентябрь",
			"октябрь",
			"ноябрь",
			"декабрь" };

		Vector<Vector<String>> done = new Vector<Vector<String>>(68, 1);
		Vector<Vector<Double>> done_num = new Vector<Vector<Double>>(
			68, 1);

		for (int v = 0; v < 68; v++) {
		    Vector<String> element = new Vector<String>(5);
		    Vector<Double> el_num = new Vector<Double>(5);
		    for (int r = 0; r < 5; r++) {
			element.add("");
			el_num.add((Double) 0.0);
		    }
		    done.add(element);
		    done_num.add(el_num);
		}

		for (int month = 0; month < months.length; month++) {
		    // смещение по строчно
		    int dy = month * 5;
		    // смещение по столбцам
		    int dx = 0;

		    if (month > 5) {
			dy = (month - 6) * 5;
			dx = 80;
		    }

		    sheet.addCell(new Label(2 + dy, 4 + dx, months[month],
			    tahoma12ptBold));
		    sheet.mergeCells(2 + dy, 4 + dx, 6 + dy, 4 + dx);

		    String[] text = { "Всего", "ВН", "СН1", "СН2", "НН" };

		    for (int each = 0; each < text.length; each++) {
			sheet.addCell(new Label(2 + dy + each, 5 + dx,
				text[each], tahoma9pt));
		    }

		    /*
		     * Заполнение формул
		     */

		    // количество добавочных строк
		    int add_y = 0;

		    for (int res_i = 0; res_i < done.size(); res_i++) {
			if (res_i == 21 || res_i == 42 || res_i == 45
				|| res_i == 55) {
			    // пропуск строки
			    add_y++;
			}

			for (int res_p = 0; res_p < done.get(res_i).size(); res_p++) {
			    String res = "";
			    if (done.get(res_i).get(res_p).equals("")) {
				// первая ячейка в формуле
				res = getColumnExcel(2 + res_p + dy)
					+ Integer.toString(8 + res_i + add_y
						+ dx);
			    } else {
				// дополнительная ячейка в формуле
				res = done.get(res_i).get(res_p)
					+ " + "
					+ getColumnExcel(2 + res_p + dy)
					+ Integer.toString(8 + res_i + add_y
						+ dx);
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
			    inn.get(i).toString(), months[month], year);

		    // количество добавочных строк
		    add_y = 0;
		    for (int res_i = 0; res_i < result.size(); res_i++) {
			if (res_i == 21 || res_i == 42 || res_i == 45
				|| res_i == 55) {
			    // пропуск строки
			    add_y++;
			}

			// "Всего"
			sheet.addCell(new Label(2 + dy, 7 + res_i + add_y + dx,
				toNumberString(result.get(res_i).get(0)
					.toString()), tahoma9ptGreen));

			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);

			for (int res_p = 1; res_p < result.get(res_i).size(); res_p++) {
			    // остальные
			    sheet.addCell(new Label(2 + res_p + dy, 7 + res_i
				    + add_y + dx, toNumberString(result
				    .get(res_i).get(res_p).toString()),
				    tahoma9ptYellow));

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
		    int x = 6 * 5;
		    int y = 80;
		    int add_y = 0;

		    sheet.addCell(new Label(2 + x, 5 + y, "Всего", tahoma9pt));
		    sheet.addCell(new Label(3 + x, 5 + y, "ВН", tahoma9pt));
		    sheet.addCell(new Label(4 + x, 5 + y, "СН1", tahoma9pt));
		    sheet.addCell(new Label(5 + x, 5 + y, "СН2", tahoma9pt));
		    sheet.addCell(new Label(6 + x, 5 + y, "НН", tahoma9pt));
		    sheet.addCell(new Label(2 + x, 4 + y, "Итог",
			    tahoma12ptBold));

		    sheet.mergeCells(2 + x, 4 + y, 6 + x, 4 + y);

		    for (int res_i = 0; res_i < done.size(); res_i++) {
			if (res_i == 21 || res_i == 42 || res_i == 45
				|| res_i == 55) {
			    // пропуск строки
			    add_y++;
			}

			// всего
			sheet.addCell(new Formula(2 + x, 7 + res_i + add_y + y,
				"SUM(" + done.get(res_i).get(0).toString()
					+ ")", tahoma9ptGreen));

			for (int res_p = 1; res_p < done.get(res_i).size(); res_p++) {
			    // остальные
			    sheet.addCell(new Formula(2 + res_p + x, 7 + res_i
				    + add_y + y, "SUM("
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

		int x = 7 * 5;
		int y = 80;
		int add_y = 0;

		sheet.addCell(new Label(2 + x, 5 + y, "Всего", tahoma9pt));
		sheet.addCell(new Label(3 + x, 5 + y, "ВН", tahoma9pt));
		sheet.addCell(new Label(4 + x, 5 + y, "СН1", tahoma9pt));
		sheet.addCell(new Label(5 + x, 5 + y, "СН2", tahoma9pt));
		sheet.addCell(new Label(6 + x, 5 + y, "НН", tahoma9pt));
		sheet.addCell(new Label(2 + x, 4 + y, "Год", tahoma12ptBold));

		sheet.mergeCells(2 + x, 4 + y, 6 + x, 4 + y);

		for (int res_i = 0; res_i < result.size(); res_i++) {
		    if (res_i == 21 || res_i == 42 || res_i == 45
			    || res_i == 55) {
			// пропуск строки
			add_y++;
		    }
		    Double res = parseStringToDouble(result.get(res_i).get(0)
			    .toString());

		    if (res.equals(done_num.get(res_i).get(0))) {
			// всего
			sheet.addCell(new Label(2 + x, 7 + res_i + add_y + y,
				toNumberString(result.get(res_i).get(0)
					.toString()), tahoma9ptGreen));
		    } else {
			sheet.addCell(new Label(2 + x, 7 + res_i + add_y + y,
				toNumberString(result.get(res_i).get(0)
					.toString()), tahoma9ptRed));
		    }

		    for (int res_p = 1; res_p < done.get(res_i).size(); res_p++) {
			res = parseStringToDouble(result.get(res_i).get(res_p)
				.toString());

			if (res.equals(done_num.get(res_i).get(res_p))) {
			    // остальные
			    sheet.addCell(new Label(2 + res_p + x, 7 + res_i
				    + add_y + y, toNumberString(result
				    .get(res_i).get(res_p).toString()),
				    tahoma9ptYellow));
			} else {
			    sheet.addCell(new Label(2 + res_p + x, 7 + res_i
				    + add_y + y, toNumberString(result
				    .get(res_i).get(res_p).toString()),
				    tahoma9ptORANGE));
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

    /**
     * Расшифровка кода строки шаблона
     * 
     * @param _code
     *            номер строки
     * @return текст строки
     */
    public String getStringCode(String _code) {
	int code = Integer.parseInt(_code);
	switch (code) {
	case 10:
	    return "Поступление в сеть из других организаций, в том числе: ";
	case 20: {
	    return "- из сетей ФСК";
	}
	case 30: {
	    return "- от генерирующих компаний и блок-станций";
	}
	case 40: {
	    return "- от смежных сетевых организаций";
	}
	case 50: {
	    return "Поступление в сеть из других уровней напряжения (трансформация)";
	}
	case 60: {
	    return "ВН";
	}
	case 70: {
	    return "СН1";
	}
	case 80: {
	    return "СН2";
	}
	case 90: {
	    return "НН";
	}
	case 100: {
	    return "Отпуск из сети, в том числе: ";
	}
	case 110: {
	    return "- конечные потребители - юридические лица (кроме совмещающих с передачей)";
	}
	case 120: {
	    return "- население и приравненные к ним группы";
	}
	case 130: {
	    return "- другие сети, в том числе потребители имеющие статус ТСО";
	}
	case 140: {
	    return "- поставщики";
	}
	case 150: {
	    return "Отпуск в сеть других уровней напряжения";
	}
	case 160: {
	    return "Хозяйственные нужды организации";
	}
	case 170: {
	    return "Генерация на установках организации (совмещение деятельности)";
	}
	case 180: {
	    return "Собственное потребление (совмещение деятельности)";
	}
	case 190: {
	    return "Потери, в том числе:";
	}
	case 200: {
	    return "- относимые на собственное потребление ";
	}
	case 210: {
	    return "Небаланс";
	}
	case 300: {
	    return "Поступление в сеть из других организаций, в том числе: ";
	}
	case 310: {
	    return "- из сетей ФСК";
	}
	case 320: {
	    return "- от генерирующих компаний и блок-станций";
	}
	case 330: {
	    return "- от смежных сетевых организаций";
	}
	case 340: {
	    return "Поступление в сеть из других уровней напряжения (трансформация)";
	}
	case 350: {
	    return "ВН";
	}
	case 360: {
	    return "СН1";
	}
	case 370: {
	    return "СН2";
	}
	case 380: {
	    return "НН";
	}
	case 390: {
	    return "Отпуск из сети, в том числе: ";
	}
	case 400: {
	    return "- конечные потребители - юридические лица (кроме совмещающих с передачей)";
	}
	case 410: {
	    return "- население и приравненные к ним группы";
	}
	case 420: {
	    return "- другие сети";
	}
	case 430: {
	    return "- поставщики";
	}
	case 440: {
	    return "Отпуск в сеть других уровней напряжения";
	}
	case 450: {
	    return "Хозяйственные нужды сети";
	}
	case 460: {
	    return "Генерация на установках организации (совмещение деятельности)";
	}
	case 470: {
	    return "Собственное потребление (совмещение деятельности)";
	}
	case 480: {
	    return "Потери, в том числе:";
	}
	case 490: {
	    return "- относимые на собственное потребление ";
	}
	case 500: {
	    return "Небаланс";
	}
	case 600: {
	    return "Заявленная мощность конечных потребителей";
	}
	case 610: {
	    return "Максимальная мощность";
	}
	case 620: {
	    return "Резервируемая мощность";
	}
	case 700: {
	    return "Полезный отпуск конечным потребителям, в том числе:";
	}
	case 710: {
	    return "- по одноставочному тарифу";
	}
	case 720: {
	    return "- по двухставочному тарифу, в том числе:";
	}
	case 730: {
	    return "-- мощность";
	}
	case 740: {
	    return "-- компенсация потерь";
	}
	case 750: {
	    return "Полезный отпуск потребителям ГП, ЭСО, ЭСК, в том числе:";
	}
	case 760: {
	    return "- по одноставочному тарифу";
	}
	case 770: {
	    return "- по двухставочному тарифу, в том числе:";
	}
	case 780: {
	    return "-- мощность";
	}
	case 790: {
	    return "-- компенсация потерь";
	}
	case 800: {
	    return "Полезный отпуск конечным потребителям, в том числе:";
	}
	case 810: {
	    return "- по одноставочному тарифу";
	}
	case 820: {
	    return "- по двухставочному тарифу, в том числе:";
	}
	case 830: {
	    return "-- мощность";
	}
	case 840: {
	    return "-- компенсация потерь";
	}
	case 850: {
	    return "Полезный отпуск потребителям ГП, ЭСО, ЭСК, в том числе:";
	}
	case 860: {
	    return "- по одноставочному тарифу";
	}
	case 870: {
	    return "- по двухставочному тарифу, в том числе:";
	}
	case 880: {
	    return "-- мощность";
	}
	case 890: {
	    return "-- компенсация потерь";
	}
	case 900: {
	    return "Стоимость услуг ФСК, в том числе:";
	}
	case 910: {
	    return "- мощность";
	}
	case 920: {
	    return "- компенсация потерь";
	}
	}
	return _code;

    }
}
