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
public class AddTableSbut extends SwingWorker {
    private Sheet sheet_title = null;
    private Sheet sheet_otpusk1 = null;
    private Sheet sheet_otpusk2 = null;
    private Sheet sheet_otpusk3 = null;
    private Sheet sheet_otpusk4 = null;
    private Sheet sheet_otpusk5 = null;
    private Sheet sheet_otpusk6 = null;
    private Sheet sheet_otpusk7 = null;

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

	main.tab.setSelectedIndex(1);
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
	    ws.setSuppressWarnings(true);
	    ws.setDrawingsDisabled(false);

	    Workbook workbook = Workbook.getWorkbook(file, ws);

	    if (getTitle(workbook) && getOtpusk1(workbook)&& getOtpusk2(workbook)&& getOtpusk3(workbook)
		    && getOtpusk4(workbook)&& getOtpusk5(workbook)&& getOtpusk6(workbook)&& getOtpusk7(workbook)) {
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
	String month = sheet_title.getCell("G14").getContents().toLowerCase();

	// Год
	String year = sheet_title.getCell("G13").getContents().toLowerCase();

	// ИНН
	String inn = sheet_title.getCell("G17").getContents();

	// Муниципальный район
	String district = sheet_title.getCell("G21").getContents();

	int id = new ConnectionBD().presenceTableSbut(month, year, inn,
		district);

	if (id > -1) {
	    if (zamenit_all) {
		new ConnectionBD().deleteRowSbut(Integer.toString(id));
		return true;
	    }
	    if (propustit_all) {
		return false;
	    }

	    // Сообщение
	    String[] choices = {
		    "Заменить",
		    "Пропустить",
		    "Заменять далее",
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
		new ConnectionBD().deleteRowSbut(Integer.toString(id));
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

	content_title.add(sheet_title.getCell("G14").getContents()
		.toLowerCase());
	search += sheet_title.getCell("G14").getContents().toLowerCase() + " ";

	// Год
	content_title.add(sheet_title.getCell("G13").getContents()
		.toLowerCase());
	search += sheet_title.getCell("G13").getContents().toLowerCase() + " ";

	// Наименование орг
	content_title.add(sheet_title.getCell("G16").getContents());
	search += sheet_title.getCell("G16").getContents().toLowerCase() + " ";

	// ИНН
	content_title.add(sheet_title.getCell("G17").getContents());

	// КПП
	content_title.add(sheet_title.getCell("G18").getContents());

	// Вид деятельности
	content_title.add(sheet_title.getCell("G19").getContents());
	// content_title.add("-");

	// Муниципальный район
	content_title.add(sheet_title.getCell("G21").getContents());
	search += sheet_title.getCell("G21").getContents().toLowerCase() + " ";

	// Муниципальное образование
	content_title.add(sheet_title.getCell("G23").getContents());
	search += sheet_title.getCell("G23").getContents().toLowerCase() + " ";

	// ОКТМО
	content_title.add(sheet_title.getCell("G25").getContents());

	// Юридический адрес
	content_title.add(sheet_title.getCell("G28").getContents());

	// Почтовый адрес
	content_title.add(sheet_title.getCell("G29").getContents());

	// ФИО руководителя
	content_title.add(sheet_title.getCell("G32").getContents());

	// Тел руководителя
	content_title.add(sheet_title.getCell("G33").getContents());

	// ФИО гл бухгалтера
	content_title.add(sheet_title.getCell("G36").getContents());

	// Тел гл бухгалтера
	content_title.add(sheet_title.getCell("G37").getContents());

	// ФИО ответ за форму
	content_title.add(sheet_title.getCell("G40").getContents());

	// Должность ответ за форму
	content_title.add(sheet_title.getCell("G41").getContents());

	// Тел ответ за форму
	content_title.add(sheet_title.getCell("G42").getContents());

	// Емаил ответ за форму
	content_title.add(sheet_title.getCell("G43").getContents());

	// Переменная для поиска
	content_title.add(search);
	
	// запись в бд
	new ConnectionBD().addTableSbut(content_title, sheet_otpusk1,
		sheet_otpusk2, sheet_otpusk3,sheet_otpusk4,
		sheet_otpusk5, sheet_otpusk6, sheet_otpusk7);
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

    private boolean getOtpusk1(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName().equals("Раздел I. А")) {
		sheet_otpusk1 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk2(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName()
		    .equals("Раздел I. Б")) {
		sheet_otpusk2 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk3(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName()
		    .equals("Раздел I. В")) {
		sheet_otpusk3 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk4(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName()
		    .equals("Раздел II. А")) {
		sheet_otpusk4 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk5(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName()
		    .equals("Раздел II. Б")) {
		sheet_otpusk5 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk6(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName().equals("Раздел III")) {
		sheet_otpusk6 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }

    private boolean getOtpusk7(Workbook workbook) {
	for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	    if (workbook.getSheet(i).getName().equals("Раздел IV")) {
		sheet_otpusk7 = workbook.getSheet(i);
		return true;
	    }
	}
	return false;
    }
}
