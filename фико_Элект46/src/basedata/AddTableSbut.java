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
public class AddTableSbut extends SwingWorker
{
	private Sheet				sheet_title		= null;
	private Sheet				sheet_otpusk1	= null;
	private Sheet				sheet_otpusk2	= null;
	private Sheet				sheet_otpusk3	= null;
	private Sheet				sheet_otpusk4	= null;
	private Sheet				sheet_otpusk5	= null;
	private Sheet				sheet_otpusk6	= null;
	private Sheet				sheet_otpusk7	= null;
	
	private JProgressBar		jProgressBar	= null;
	private Main				main			= null;
	
	private DefaultListModel	listPaths		= null;
	private DefaultListModel	listNames		= null;

	private boolean				propustit_all	= false;
	private boolean				zamenit_all		= false;

	public void setJProgressBar(JProgressBar jProgressBar)
	{
		this.jProgressBar = jProgressBar;
	}
	
	public void setMain(Main main)
	{
		this.main = main;
	}
	
	
	public void setListPaths(DefaultListModel listPaths)
	{
		this.listPaths = listPaths;
	}

	public void setListNames(DefaultListModel listNames)
	{
		this.listNames = listNames;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void done()
	{
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
	protected Object doInBackground() throws Exception
	{
		// проссматриваем весь список
		for (int i = 0; i < listPaths.getSize(); i++)
		{
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
	private void runAdd(File file)
	{
		try
		{
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("ru", "RU"));
			ws.setSuppressWarnings(false);
			ws.setDrawingsDisabled(false);

			Workbook workbook = Workbook.getWorkbook(file, ws);

			if (getTitle(workbook) && getOtpusk1(workbook) && getOtpusk2(workbook) && getOtpusk3(workbook) && getOtpusk4(workbook) && getOtpusk5(workbook) && getOtpusk6(workbook) && getOtpusk7(workbook))
			{
				if (presenceTable(file))
				{
					excel();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Ошибка в файле: " + file.getName());
			}

			workbook.close();
		}
		catch (BiffException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean presenceTable(File file)
	{
		// Месяц
		String month = sheet_title.getCell("F10").getContents().toLowerCase();

		// Год
		String year = sheet_title.getCell("F11").getContents().toLowerCase();

		// ИНН
		String inn = sheet_title.getCell("F15").getContents();

		// Муниципальный район
		String district = sheet_title.getCell("F19").getContents();

		int id = new ConnectionBD().presenceTableSbut(month, year, inn, district);

		if (id > -1)
		{
			if (zamenit_all)
			{
				new ConnectionBD().deleteRowSbut(Integer.toString(id));
				return true;
			}
			if (propustit_all)
			{
				return false;
			}

			// Сообщение
			String[] choices = { "Заменить", "Пропустить", "Заменять далее", "Пропускать далее" };
			int response = JOptionPane.showOptionDialog(null // Center in
																// window.
			, "Данные файла: '" + file.getName() + "' уже записаны\n" + "" + inn + " " + month + " " + year + "\n" + "Путь к файлу: " + file.getAbsolutePath() // Message
			, "" // Title in titlebar
			, JOptionPane.YES_NO_OPTION // Option type
			, JOptionPane.PLAIN_MESSAGE // messageType
			, null // Icon (none)
			, choices // Button text as above.
			, "None of your business" // Default button's labelF
			);

			// определяем полученный ответ от пользователя
			switch (response)
			{
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
					JOptionPane.showMessageDialog(null, "Unexpected response " + response);
			}
		}
		else
		{
			// такой записи не было, продолжаем
			return true;
		}

		// неведомая ошибка
		return false;
	}

	// Считываем с excel файла и заносит данные в бд
	private void excel()
	{
		// Строка поиска
		// по ней происходит фильтрация данных на гл.фрейме
		String search = "";

		// собираем данные в одном месте(title)
		ArrayList<String> content_title = new ArrayList<String>();

		// Месяц
		content_title.add(sheet_title.getCell("F10").getContents().toLowerCase());
		search += sheet_title.getCell("F10").getContents().toLowerCase() + " ";

		// Год
		content_title.add(sheet_title.getCell("F11").getContents().toLowerCase());
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
		content_title.add(sheet_title.getCell("F32").getContents());

		// Почтовый адрес
		content_title.add(sheet_title.getCell("F33").getContents());

		// ФИО руководителя
		content_title.add(sheet_title.getCell("F36").getContents());

		// Тел руководителя
		content_title.add(sheet_title.getCell("F37").getContents());

		// ФИО гл бухгалтера
		content_title.add(sheet_title.getCell("F40").getContents());

		// Тел гл бухгалтера
		content_title.add(sheet_title.getCell("F41").getContents());

		// ФИО ответ за форму
		content_title.add(sheet_title.getCell("F44").getContents());

		// Должность ответ за форму
		content_title.add(sheet_title.getCell("F46").getContents());

		// Тел ответ за форму
		content_title.add(sheet_title.getCell("F46").getContents());

		// Емаил ответ за форму
		content_title.add(sheet_title.getCell("F47").getContents());

		// Переменная для поиска
		content_title.add(search);

		// определит айди таблиц
		int table_id = 0;
		
		// Отпуск ээ по рег тар
		ArrayList<String> content_otpusk1 = new ArrayList<String>();
		for (int p = 0; p < 6; p++)
		{
			for (int i = 11; i < 30; i++)
			{
				// всего
				content_otpusk1.add(getZero(sheet_otpusk1.getCell(5 + p * 5, i - 1).getContents()));

				// вн
				content_otpusk1.add(getZero(sheet_otpusk1.getCell(6 + p * 5, i - 1).getContents()));

				// сн1
				content_otpusk1.add(getZero(sheet_otpusk1.getCell(7 + p * 5, i - 1).getContents()));

				// сн2
				content_otpusk1.add(getZero(sheet_otpusk1.getCell(8 + p * 5, i - 1).getContents()));

				// нн
				content_otpusk1.add(getZero(sheet_otpusk1.getCell(9 + p * 5, i - 1).getContents()));

				// Код строки
				content_otpusk1.add(sheet_otpusk1.getCell("E" + Integer.toString(i)).getContents());

				// table_id
				content_otpusk1.add(Integer.toString(table_id));
			}
			table_id++;
		}

		// Отпуск ээ по рег тар (насел)
		ArrayList<String> content_otpusk2 = new ArrayList<String>();
		for (int p = 0; p < 5; p++)
		{
			for (int i = 10; i < 16; i++)
			{
				// атр1
				content_otpusk2.add(getZero(sheet_otpusk2.getCell(5 + p * 3, i - 1).getContents()));

				// атр2
				content_otpusk2.add(getZero(sheet_otpusk2.getCell(6 + p * 3, i - 1).getContents()));

				// атр3
				content_otpusk2.add(getZero(sheet_otpusk2.getCell(7 + p * 3, i - 1).getContents()));

				// Код строки
				content_otpusk2.add(sheet_otpusk2.getCell("E" + Integer.toString(i)).getContents());

				// table_id
				// отдельная последовательность
				content_otpusk2.add(Integer.toString(p));
			}
		}

		// Отпуск мощности по рег тар
		ArrayList<String> content_otpusk3 = new ArrayList<String>();
		for (int p = 0; p < 5; p++)
		{
			for (int i = 11; i < 30; i++)
			{
				// всего
				content_otpusk3.add(getZero(sheet_otpusk3.getCell(5 + p * 5, i - 1).getContents()));

				// вн
				content_otpusk3.add(getZero(sheet_otpusk3.getCell(6 + p * 5, i - 1).getContents()));

				// сн1
				content_otpusk3.add(getZero(sheet_otpusk3.getCell(7 + p * 5, i - 1).getContents()));

				// сн2
				content_otpusk3.add(getZero(sheet_otpusk3.getCell(8 + p * 5, i - 1).getContents()));

				// нн
				content_otpusk3.add(getZero(sheet_otpusk3.getCell(9 + p * 5, i - 1).getContents()));

				// Код строки
				content_otpusk3.add(sheet_otpusk3.getCell("E" + Integer.toString(i)).getContents());

				// table_id
				content_otpusk3.add(Integer.toString(table_id));
			}
			table_id++;
		}

		// Отпуск ээ по нерег ценам
		ArrayList<String> content_otpusk4 = new ArrayList<String>();
		for (int p = 0; p < 6; p++)
		{
			for (int i = 11; i < 30; i++)
			{
				// всего
				content_otpusk4.add(getZero(sheet_otpusk4.getCell(5 + p * 5, i - 1).getContents()));

				// вн
				content_otpusk4.add(getZero(sheet_otpusk4.getCell(6 + p * 5, i - 1).getContents()));

				// сн1
				content_otpusk4.add(getZero(sheet_otpusk4.getCell(7 + p * 5, i - 1).getContents()));

				// сн2
				content_otpusk4.add(getZero(sheet_otpusk4.getCell(8 + p * 5, i - 1).getContents()));

				// нн
				content_otpusk4.add(getZero(sheet_otpusk4.getCell(9 + p * 5, i - 1).getContents()));

				// Код строки
				content_otpusk4.add(sheet_otpusk3.getCell("E" + Integer.toString(i)).getContents());

				// table_id
				content_otpusk4.add(Integer.toString(table_id));
			}
			table_id++;
		}

		// Отпуск мощности по нерег ценам
		ArrayList<String> content_otpusk5 = new ArrayList<String>();
		for (int p = 0; p < 5; p++)
		{
			for (int i = 11; i < 30; i++)
			{
				// всего
				content_otpusk5.add(getZero(sheet_otpusk5.getCell(5 + p * 5, i - 1).getContents()));

				// вн
				content_otpusk5.add(getZero(sheet_otpusk5.getCell(6 + p * 5, i - 1).getContents()));

				// сн1
				content_otpusk5.add(getZero(sheet_otpusk5.getCell(7 + p * 5, i - 1).getContents()));

				// сн2
				content_otpusk5.add(getZero(sheet_otpusk5.getCell(8 + p * 5, i - 1).getContents()));

				// нн
				content_otpusk5.add(getZero(sheet_otpusk5.getCell(9 + p * 5, i - 1).getContents()));

				// Код строки
				content_otpusk5.add(sheet_otpusk5.getCell("E" + Integer.toString(i)).getContents());

				// table_id
				content_otpusk5.add(Integer.toString(table_id));
			}
			System.out.println(table_id);
			table_id++;
		}

		// Продажа
		ArrayList<String> content_otpusk6 = new ArrayList<String>();
		for (int i = 9; i < 26; i++)
		{
			// атр1
			content_otpusk6.add(getZero(sheet_otpusk6.getCell("F" + Integer.toString(i)).getContents()));

			// атр2
			content_otpusk6.add(getZero(sheet_otpusk6.getCell("G" + Integer.toString(i)).getContents()));

			// атр3
			content_otpusk6.add(getZero(sheet_otpusk6.getCell("H" + Integer.toString(i)).getContents()));

			// атр4
			content_otpusk6.add(getZero(sheet_otpusk6.getCell("I" + Integer.toString(i)).getContents()));

			// атр5
			content_otpusk6.add(getZero(sheet_otpusk6.getCell("J" + Integer.toString(i)).getContents()));

			// код
			content_otpusk6.add(sheet_otpusk6.getCell("E" + Integer.toString(i)).getContents());
		}

		// Покупка
		ArrayList<String> content_otpusk7 = new ArrayList<String>();
		for (int i = 9; i < 26; i++)
		{
			//
			content_otpusk7.add(getZero(sheet_otpusk7.getCell("F" + Integer.toString(i)).getContents()));

			//
			content_otpusk7.add(getZero(sheet_otpusk7.getCell("G" + Integer.toString(i)).getContents()));

			//
			content_otpusk7.add(getZero(sheet_otpusk7.getCell("H" + Integer.toString(i)).getContents()));

			//
			content_otpusk7.add(getZero(sheet_otpusk7.getCell("I" + Integer.toString(i)).getContents()));

			//
			content_otpusk7.add(getZero(sheet_otpusk7.getCell("J" + Integer.toString(i)).getContents()));

			//
			content_otpusk7.add(sheet_otpusk7.getCell("E" + Integer.toString(i)).getContents());
		}

		// запись в бд
		new ConnectionBD().addTableSbut(content_title, content_otpusk1, content_otpusk2, content_otpusk3, content_otpusk4, content_otpusk5, content_otpusk6, content_otpusk7);
	}

	private boolean getTitle(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Титульный"))
			{
				sheet_title = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk1(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск ээ по рег тар"))
			{
				sheet_otpusk1 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk2(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск ээ по рег тар (насел)"))
			{
				sheet_otpusk2 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk3(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск мощности по рег тар"))
			{
				sheet_otpusk3 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk4(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск ээ по нерег ценам"))
			{
				sheet_otpusk4 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk5(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск мощности по нерег ценам"))
			{
				sheet_otpusk5 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk6(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Продажа"))
			{
				sheet_otpusk6 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpusk7(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Покупка"))
			{
				sheet_otpusk7 = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private String getZero(String _text)
	{
		if (_text == "")
		{
			return "0,0000";
		}
		return _text;
	}
}
