package basedata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import windows.Main;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@SuppressWarnings("rawtypes")
public class AddTableSetev extends SwingWorker {
    private Sheet sheet_title = null;
    private Sheet sheet_otpusk = null;

    private JProgressBar jProgressBar = null;
    private Main main = null;

    private DefaultListModel listPaths = null;
    private DefaultListModel listNames = null;

    private boolean propustit_all = false;
    private boolean zamenit_all = false;

    public void setJProgressBar(JProgressBar jProgressBar) {
	this.jProgressBar = jProgressBar;
    }

    public void setMain(Main main) {
	this.main = main;
    }

    public void setListPaths(DefaultListModel listPaths) {
	this.listPaths = listPaths;
    }

    public void setListNames(DefaultListModel listNames) {
	this.listNames = listNames;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void done() {
	// уничтожаем фрейм
	// вызывает обновление таблицы на гл. фрейме
	// frame.dispose();

	main.getContentPane().removeAll();
	main.getContentPane().add(main.mainPanel());

	main.enable();

	main.validate();

	JOptionPane.showMessageDialog(null, "finish");
    }

    @Override
    protected Object doInBackground() throws Exception {
	// проссматриваем весь список
	for (int i = 0; i < listPaths.getSize(); i++) {
	    // запускаем обработку
	    runAdd(new File(listPaths.getElementAt(i).toString()));
	    // после добавление записи в бд,
	    // символизируем...
	    jProgressBar.setValue(i + 1);
	    // ... о занесение ЭТОЙ записи
	    listNames.removeElementAt(0);
	}
	return null;
    }

    // Обработка данных
    private void runAdd(File file) {
	try {
	    WorkbookSettings ws = new WorkbookSettings();
	    ws.setLocale(new Locale("ru", "RU"));
	    ws.setSuppressWarnings(false);
	    ws.setDrawingsDisabled(false);

	    Workbook workbook = Workbook.getWorkbook(file, ws);

	    if (getTitle(workbook) && getOtpusk(workbook)) {
		if (presenceTable(file)) {
		    excel();
		}
	    } else {

		JOptionPane.showMessageDialog(null,
			"Ошибка в файле: " + file.getName());
	    }

	    workbook.close();
	} catch (BiffException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private boolean presenceTable(File file) {
	// Месяц
	String month = sheet_title.getCell("F10").getContents().toLowerCase();

	// Год
	String year = sheet_title.getCell("F11").getContents().toLowerCase();

	// ИНН
	String inn = sheet_title.getCell("F15").getContents();

	// Муниципальный район
	String district = sheet_title.getCell("F19").getContents();

	int id = new ConnectionBD().presenceTable(month, year, inn, district);

	if (id > -1) {
	    if (zamenit_all) {
		new ConnectionBD().deleteRow(Integer.toString(id));
		return true;
	    }
	    if (propustit_all) {
		return false;
	    }

	    // Сообщение
	    String[] choices = { "Заменить", "Пропустить", "Заменять далее",
		    "Пропускать далее" };
	    int response = JOptionPane.showOptionDialog(null // Center in
							     // window.
		    , "Данные файла: '" + file.getName() + "' уже записаны\n"
			    + "" + inn + " " + month + " " + year + "\n"
			    + "Путь к файлу: " + file.getAbsolutePath() // Message
		    , "" // Title in titlebar
		    , JOptionPane.YES_NO_OPTION // Option type
		    , JOptionPane.PLAIN_MESSAGE // messageType
		    , null // Icon (none)
		    , choices // Button text as above.
		    , "None of your business" // Default button's labelF
	    );

	    // определяем полученный ответ от пользователя
	    switch (response) {
	    case 2:
		zamenit_all = true;
	    case 0:
		// производим замену
		// удаляем старую запись
		new ConnectionBD().deleteRow(Integer.toString(id));
		// и записываем новую
		return true;

	    case 3:
		propustit_all = true;
	    case 1:
	    case -1:
		// получен отрицательный ответ
		return false;

	    default:
		// ... If we get here, something is wrong. Defensive
		// programming.
		JOptionPane.showMessageDialog(null, "Unexpected response "
			+ response);
	    }
	} else {
	    // такой записи не было, продолжаем
	    return true;
	}

	// неведомая ошибка
	return false;
    }

    // Считываем с excel файла и заносит данные в бд
    private void excel() {
	// Строка поиска
	// по ней происходит фильтрация данных на гл.фрейме
	String search = "";

	// собираем данные в одном месте(title)
	ArrayList<String> content_title = new ArrayList<String>();

	// Месяц
	content_title.add(sheet_title.getCell("F10").getContents()
		.toLowerCase());
	search += sheet_title.getCell("F10").getContents().toLowerCase() + " ";

	// Год
	content_title.add(sheet_title.getCell("F11").getContents()
		.toLowerCase());
	search += sheet_title.getCell("F11").getContents().toLowerCase() + " ";

	// Наименование орг
	content_title.add(sheet_title.getCell("F13").getContents());
	search += sheet_title.getCell("F13").getContents().toLowerCase() + " ";

	// ИНН
	content_title.add(sheet_title.getCell("F15").getContents());

	// КПП
	content_title.add(sheet_title.getCell("F16").getContents());

	// ОКПО
	content_title.add(sheet_title.getCell("F17").getContents());

	// Муниципальный район
	content_title.add(sheet_title.getCell("F19").getContents());
	search += sheet_title.getCell("F19").getContents().toLowerCase() + " ";

	// Муниципальное образование
	content_title.add(sheet_title.getCell("F21").getContents());
	search += sheet_title.getCell("F21").getContents().toLowerCase() + " ";

	// ОКТМО
	content_title.add(sheet_title.getCell("F23").getContents());

	// Юридический адрес
	content_title.add(sheet_title.getCell("F26").getContents());

	// Почтовый адрес
	content_title.add(sheet_title.getCell("F27").getContents());

	// ФИО руководителя
	content_title.add(sheet_title.getCell("F30").getContents());

	// Тел руководителя
	content_title.add(sheet_title.getCell("F31").getContents());

	// ФИО гл бухгалтера
	content_title.add(sheet_title.getCell("F34").getContents());

	// Тел гл бухгалтера
	content_title.add(sheet_title.getCell("F35").getContents());

	// ФИО ответ за форму
	content_title.add(sheet_title.getCell("F38").getContents());

	// Должность ответ за форму
	content_title.add(sheet_title.getCell("F39").getContents());

	// Тел ответ за форму
	content_title.add(sheet_title.getCell("F40").getContents());

	// Емаил ответ за форму
	content_title.add(sheet_title.getCell("F41").getContents());

	// Переменная для поиска
	content_title.add(search);

	// собираем данные в одном месте(отпуск)
	ArrayList<String> content_otpusk = new ArrayList<String>();

	// ОТПУСК
	for (int i = 11; i < 58; i++) {
	    if (i != 30 && i != 50 && i != 53) {
		// всего
		content_otpusk.add(getZero(sheet_otpusk.getCell(
			"F" + Integer.toString(i)).getContents()));

		// вн
		content_otpusk.add(getZero(sheet_otpusk.getCell(
			"G" + Integer.toString(i)).getContents()));

		// сн1
		content_otpusk.add(getZero(sheet_otpusk.getCell(
			"H" + Integer.toString(i)).getContents()));

		// сн2
		content_otpusk.add(getZero(sheet_otpusk.getCell(
			"I" + Integer.toString(i)).getContents()));

		// нн
		content_otpusk.add(getZero(sheet_otpusk.getCell(
			"J" + Integer.toString(i)).getContents()));

		// Код строки
		content_otpusk.add(sheet_otpusk.getCell(
			"E" + Integer.toString(i)).getContents());
	    }
	}
	// запись в бд
	new ConnectionBD().addTable(content_title, content_otpusk);
    }

    private boolean getTitle(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName().equals("Титульный")) {
		sheet_title = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName()
		    .equals("Отпуск ЭЭ сет организациями")) {
		sheet_otpusk = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private String getZero(String _text) {
	if (_text == "") {
	    return "0,0000";
	}
	return _text;
    }
}
