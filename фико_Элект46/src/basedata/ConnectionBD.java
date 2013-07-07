package basedata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class ConnectionBD {
    private Connection conn = null;
    private Statement stat = null;
    private ResultSet rs = null;
    private PreparedStatement pst = null;

    // список названий месяцев
    // в виде данных
    private String[] months = { "январь", "февраль", "март", "апрель", "май",
	    "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь",
	    "декабрь", "год" };
    // в виде названия поля
    private String[] months_eng = { "jan", "feb", "mar", "apr", "may", "jun",
	    "jul", "aug", "sep", "oct", "nov", "dec", "itog" };
    // Список муниципальных районов Калужской области
    private String[] districts = { "Бабынинский муниципальный район",
	    "Барятинский муниципальный район", "Боровский муниципальный район",
	    "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район",
	    "Думиничский муниципальный район",
	    "Жиздринский муниципальный район", "Жуковский муниципальный район",
	    "Износковский муниципальный район",
	    "Кировский муниципальный район", "Козельский муниципальный район",
	    "Куйбышевский муниципальный район",
	    "Людиновский муниципальный район",
	    "Малоярославецкий муниципальный район",
	    "Медынский муниципальный район", "Мещовский муниципальный район",
	    "Мосальский муниципальный район",
	    "Перемышльский муниципальный район",
	    "Спас-Деменский муниципальный район",
	    "Сухиничский муниципальный район", "Тарусский муниципальный район",
	    "Ульяновский муниципальный район",
	    "Ферзиковский муниципальный район",
	    "Хвастовичский муниципальный район",
	    "Юхновский муниципальный район" };

    public ConnectionBD() {
	try {
	    Class.forName("org.sqlite.JDBC");
	    conn = DriverManager.getConnection("jdbc:sqlite:electro46.db");
	    stat = conn.createStatement();

	    /*
	     * Титульник таблицы "Сетевые организации" аналогично Сбытовые
	     * компании
	     * 
	     * id INTEGER month STRING year STRING name STRING inn STRING kpp
	     * STRING okpo STRING district STRING city STRING oktmo STRING
	     * uraddress STRING postaddress STRING rukfio STRING ruktel STRING
	     * buhfio STRING buhtel STRING formfio STRING formjob STRING formtel
	     * STRING formemail STRING search STRING
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS set_title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, month STRING, year STRING, name STRING, inn STRING, kpp STRING, okpo STRING, district STRING, city STRING, oktmo STRING, uraddress STRING, postaddress STRING, rukfio STRING, ruktel STRING, buhfio STRING, buhtel STRING, formfio STRING, formjob STRING, formtel STRING, formemail STRING, search STRING);");
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, month STRING, year STRING, name STRING, inn STRING, kpp STRING, okpo STRING, district STRING, city STRING, oktmo STRING, uraddress STRING, postaddress STRING, rukfio STRING, ruktel STRING, buhfio STRING, buhtel STRING, formfio STRING, formjob STRING, formtel STRING, formemail STRING, search STRING);");

	    /*
	     * Данные "Сетевые организации"
	     * 
	     * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT set_titleid INTEGER
	     * REFERENCES set_title(id) set_all STRING vn STRING cn1 STRING cn2
	     * STRING nn STRING code STRING
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS set_otpusk(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, set_titleid INTEGER REFERENCES set_title(id) ON UPDATE CASCADE ON DELETE CASCADE, set_all STRING, vn STRING, cn1 STRING, cn2 STRING, nn STRING, code STRING);");

	    /*
	     * Данные "Сбытовые компании"
	     * 
	     * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT sbut_titleid
	     * INTEGER REFERENCES sbut_title(id) table_id INTEGER set_all STRING
	     * vn STRING cn1 STRING cn2 STRING nn STRING code STRING
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_otpusk(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sbut_titleid INTEGER REFERENCES sbut_title(id) ON UPDATE CASCADE ON DELETE CASCADE, table_id INTEGER, set_all STRING, vn STRING, cn1 STRING, cn2 STRING, nn STRING, code STRING);");

	    /*
	     * Продажа аналогично покупка
	     * 
	     * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT sbut_titleid
	     * INTEGER REFERENCES sbut_title(id) atr1 STRING atr2 STRING atr3
	     * STRING atr4 STRING atr5 STRING code STRING
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_sell(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sbut_titleid INTEGER REFERENCES sbut_title(id) ON UPDATE CASCADE ON DELETE CASCADE, atr1 STRING, atr2 STRING, atr3 STRING, atr4 STRING, atr5 STRING, code STRING);");
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_buy(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sbut_titleid INTEGER REFERENCES sbut_title(id) ON UPDATE CASCADE ON DELETE CASCADE, atr1 STRING, atr2 STRING, atr3 STRING, atr4 STRING, atr5 STRING, code STRING);");

	    /*
	     * Отпуск ээ по рег тар (населен)
	     * 
	     * id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT sbut_titleid
	     * INTEGER REFERENCES sbut_title(id) table_id INTEGER atr1 STRING
	     * atr2 STRING atr3 STRING code STRING
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_nas(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, sbut_titleid INTEGER REFERENCES sbut_title(id) ON UPDATE CASCADE ON DELETE CASCADE, table_id INTEGER, atr1 STRING, atr2 STRING, atr3 STRING, code STRING);");

	    /*
	     * Сетевые орг
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS presence(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name STRING, inn STRING, district STRING);");
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS available(id INTEGER NOT NULL PRIMARY KEY , presenceid INTEGER REFERENCES presence(id) ON UPDATE CASCADE ON DELETE CASCADE, year STRING, jan STRING, feb STRING, mar STRING, apr STRING, may STRING, jun STRING, jul STRING, aug STRING, sep STRING, oct STRING, nov STRING, dec STRING, itog STRING);");

	    /*
	     * Сбытовые компании
	     */
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_presence(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name STRING, inn STRING, district STRING);");
	    stat.executeUpdate("CREATE TABLE IF NOT EXISTS sbut_available(id INTEGER NOT NULL PRIMARY KEY , presenceid INTEGER REFERENCES presence(id) ON UPDATE CASCADE ON DELETE CASCADE, year STRING, jan STRING, feb STRING, mar STRING, apr STRING, may STRING, jun STRING, jul STRING, aug STRING, sep STRING, oct STRING, nov STRING, dec STRING, itog STRING);");

	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * Добавляет новую запись
     * 
     * @param content_title
     * @param content_otpusk
     */
    public void addTable(ArrayList<String> content_title,
	    ArrayList<String> content_otpusk) {
	try {
	    // добавление записи в таблицу title
	    pst = conn
		    .prepareStatement("INSERT INTO set_title VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

	    // считываем данные с контейнера
	    for (int i = 0; i < content_title.size(); i++) {
		// 1 параметр - автоинкремент(id)
		pst.setString(i + 2, content_title.get(i));
	    }

	    pst.addBatch();

	    pst.executeBatch();

	    /*
	     * Определяем id последней добавленной записи
	     */
	    rs = stat.executeQuery("SELECT last_insert_rowid();");
	    int current_id = rs.getInt(1);

	    /*
	     * добавление записи в таблицу set_otpusk
	     */
	    pst = conn
		    .prepareStatement("INSERT INTO set_otpusk VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < 44; i++) {
		pst.setInt(2, current_id);
		for (int p = 0; p < 6; p++) {
		    pst.setString(p + 3, content_otpusk.get(i * 6 + p));
		}

		pst.addBatch();

	    }

	    pst.executeBatch();

	    /*
	     * Работа с таблицами presence и available
	     */

	    // определяем нахождения инн в presence
	    rs = stat.executeQuery("SELECT id FROM presence WHERE inn LIKE '"
		    + content_title.get(3) + "';");

	    if (rs.next()) {
		// инн уже включён

		// сохраняем тек id инн
		int presence_id = rs.getInt(1);

		// ищем год новый записи в уже сохранённой записи
		rs = stat
			.executeQuery("SELECT id FROM available WHERE year LIKE '"
				+ content_title.get(1)
				+ "' AND presenceid LIKE '"
				+ presence_id
				+ "';");

		if (rs.next()) {
		    // год существует

		    // получаем id этого года
		    int available_id = rs.getInt(1);

		    // определяем месяц, который нужно обновить
		    for (int i = 0; i < months.length; i++) {
			if (content_title.get(0).equals(months[i])) {
			    // обновляем запись
			    stat.executeUpdate("UPDATE available SET "
				    + months_eng[i] + " = '+' WHERE id LIKE '"
				    + available_id + "';");
			    break;
			}
		    }
		} else {
		    // необходимо добавить новый год, т.к. этого нету

		    pst = conn
			    .prepareStatement("INSERT INTO available VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		    // presence_id - id инн
		    pst.setString(2, Integer.toString(presence_id));
		    // год
		    pst.setString(3, content_title.get(1));

		    // заполняем строку
		    for (int i = 0; i < months.length; i++) {
			if (content_title.get(0).equals(months[i])) {
			    // нужный месяц
			    pst.setString(4 + i, "+");
			} else {
			    // все ост месяца заполняются пустой строкой
			    pst.setString(4 + i, "");
			}
		    }

		    // добавляем в бд
		    pst.addBatch();
		    pst.executeBatch();
		}

	    } else {
		// инн ещё не включали

		// создаём новую запись инн
		pst = conn
			.prepareStatement("INSERT INTO presence VALUES (?, ?, ?, ?);");
		// наименование организации
		pst.setString(2, content_title.get(2));
		// инн
		pst.setString(3, content_title.get(3));
		// район
		pst.setString(4, content_title.get(6));
		// добавление записи в бд
		pst.addBatch();
		pst.executeBatch();

		// Определяем id последней записи
		rs = stat.executeQuery("SELECT last_insert_rowid();");
		int presence_id = rs.getInt(1);

		// создаём новый год для нового инн
		pst = conn
			.prepareStatement("INSERT INTO available VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		// presence_id - id инн
		pst.setString(2, Integer.toString(presence_id));
		// год
		pst.setString(3, content_title.get(1));

		// заполняем строку
		for (int i = 0; i < months.length; i++) {
		    if (content_title.get(0).equals(months[i])) {
			// нужный месяц
			pst.setString(4 + i, "+");
		    } else {
			// все ост месяца заполняются пустой строкой
			pst.setString(4 + i, "");
		    }
		}

		// добавляем в бд
		pst.addBatch();
		pst.executeBatch();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		pst.close();
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Проверяет вхождение в БД таблицы с "этими параметрами"
     * 
     * @param month
     *            месяц
     * @param year
     *            год
     * @param inn
     *            инн
     * @param district
     *            муниципальный район
     * @return id найденой записи, -1 если запись не найдена
     */
    public int presenceTable(String month, String year, String inn,
	    String district) {
	try {
	    /*
	     * ишем запись с необходимыми параметрами
	     */
	    rs = stat.executeQuery("SELECT id FROM set_title WHERE year LIKE '"
		    + year + "' AND month LIKE '" + month + "' AND inn LIKE '"
		    + inn + "' AND district LIKE '" + district + "';");

	    if (rs.next()) {
		/*
		 * возвращшаем id найденой записи
		 */
		return rs.getInt(1);
	    } else {
		/*
		 * такой записи не было
		 */
		return -1;
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return -1;
    }

    /**
     * Удаление записи из бд
     * 
     * @param id
     *            - id удаляемой записи
     */
    public void deleteRow(String id) {
	try {
	    /*
	     * определяем инн, год и месяц удаляемой записи
	     */
	    rs = stat
		    .executeQuery("SELECT inn,year,month FROM set_title WHERE id LIKE '"
			    + id + "';");

	    if (rs.next()) {
		// инн удаляемой записи
		String title_inn = rs.getString(1);
		// год удаляемой записи
		String title_year = rs.getString(2);
		// месяц удаляемой записи
		String title_month = rs.getString(3);

		// определяем id таблицы с инн(presence)
		rs = stat
			.executeQuery("SELECT id FROM presence WHERE inn LIKE '"
				+ title_inn + "';");

		if (rs.next()) {
		    // id таблицы с инн(presence)
		    int presence_id = rs.getInt(1);

		    /*
		     * определяем id таблицы available по: presence_id - id инн
		     * title_year - год записи
		     */
		    rs = stat
			    .executeQuery("SELECT id FROM available WHERE presenceid LIKE '"
				    + presence_id
				    + "' AND year LIKE '"
				    + title_year + "';");

		    if (rs.next()) {
			// id записи в available
			int available_id = rs.getInt(1);

			// определяем месяц, который нужно обновить
			for (int i = 0; i < months.length; i++) {
			    if (title_month.equals(months[i])) {
				// обновляем запись
				stat.executeUpdate("UPDATE available SET "
					+ months_eng[i]
					+ " = '' WHERE id LIKE '"
					+ available_id + "';");
				break;
			    }
			}
		    }
		}
	    }

	    // удаление записи
	    // с title
	    stat.executeUpdate("DELETE FROM set_title WHERE id = '" + id + "';");
	    // с otpusk
	    stat.executeUpdate("DELETE FROM set_otpusk WHERE set_titleid = '"
		    + id + "';");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Получение таблицы по запросу "строки поиска"
     * 
     * @param sqlsearch
     *            - строка с параметрами фильтрации
     * @return { { id, год, месяц, наименование }, ..}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector getDataFromDB(String sqlsearch) {
	// переменная под результат
	Vector result = new Vector();
	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT id,year,month,name FROM set_title "
		    + sqlsearch + ";");

	    while (rs.next()) {
		// переменная под элементы строки
		Vector<String> element = new Vector<String>();

		// id
		element.add(rs.getString(1));
		// год
		element.add(rs.getString(2));
		// месяц
		element.add(rs.getString(3));
		// наименование
		element.add(rs.getString(4));

		// Присоединяем список к результату
		result.add(element);
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Получаем данные таблицы title в векторной формем
     * 
     * @param current_idid
     *            получаемой записи
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public Vector getDataFromDB_title(String current_id) {
	// переменная под результат
	Vector<String> element = new Vector<String>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT * FROM set_title where id like '"
		    + current_id + "';");

	    while (rs.next()) {
		for (int i = 1; i < 21; i++) {
		    element.add(rs.getString(i));
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return element;
    }

    /**
     * Получаем данные таблицы ПАРА в векторной форме
     * 
     * @param current_id
     *            id получаемой записи
     * @return данные таблицы пара
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector getDataFromDB_Otpusk(String current_id) {
	// переменная под результат
	Vector result = new Vector();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat
		    .executeQuery("SELECT code,set_all,vn,cn1,cn2,nn FROM set_otpusk where set_titleid like '"
			    + current_id + "';");

	    while (rs.next()) {
		Vector<String> element = new Vector<String>();

		if (rs.getString(1).equals("10")) {
		    Vector<String> element2 = new Vector<String>();
		    element2.add("");
		    element2.add("Электроэнергия (тыс. кВт•ч)");
		    result.add(element2);
		}

		if (rs.getString(1).equals("210")) {
		    Vector<String> element3 = new Vector<String>();
		    element3.add("");
		    element3.add("Мощность (МВт) <*>");
		    result.add(element3);
		}

		if (rs.getString(1).equals("400")) {
		    Vector<String> element3 = new Vector<String>();
		    element3.add("");
		    element3.add("Заявленная и присоединенная мощность (МВт)");
		    result.add(element3);
		}
		if (rs.getString(1).equals("500")) {
		    Vector<String> element3 = new Vector<String>();
		    element3.add("");
		    element3.add("Платежи, тыс. руб.");
		    result.add(element3);
		}

		// Код
		element.add(rs.getString(1));
		// Расшифровка кода
		element.add(getStringCode(rs.getString(1)));
		// всего
		element.add(rs.getString(2));
		// ост
		element.add(rs.getString(3));
		element.add(rs.getString(4));
		element.add(rs.getString(5));
		element.add(rs.getString(6));

		// Присоединяем список к результату
		result.add(element);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector<Vector> getDataFromDB_Year(Object year) {
	// переменная под результат
	Vector result = new Vector();

	try {
	    for (int p = 0; p < districts.length; p++) {

		// Выполняем запрос, который у нас в переменной query
		rs = stat
			.executeQuery("SELECT m1.name,m2.* FROM presence AS m1, available AS m2 WHERE m2.year like '"
				+ year.toString()
				+ "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '"
				+ districts[p] + "';");

		while (rs.next()) {
		    if (rs.getRow() == 1) {
			// Первая запись
			// добавляем строка района
			Vector<String> element2 = new Vector<String>();

			// название района
			element2.add(districts[p]);

			// заполняем ост ячейки пустым значениями
			for (int i = 0; i < months_eng.length; i++) {
			    element2.add("");
			}

			result.add(element2);
		    }

		    Vector<String> element = new Vector<String>();

		    // Код
		    element.add(rs.getString(1));
		    // объём
		    for (int i = 0; i < months_eng.length; i++) {
			element.add(rs.getString(i + 5));
		    }

		    // Присоединяем список к результату
		    result.add(element);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Года, которые находятся в бд
     * 
     * @return список годов
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector getYears() {
	// переменная под результат
	Vector result = new Vector();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT year FROM available;");

	    // запись первой даты
	    if (rs.next()) {
		result.add(rs.getString(1));
	    }

	    // добавление ост годов
	    continuebreak: while (rs.next()) {
		// ищем год, который не находится в result
		for (int i = 0; i < result.size(); i++) {
		    if (result.get(i).equals(rs.getString(1))) {
			// переход к след записи
			continue continuebreak;
		    }
		}
		result.add(rs.getString(1));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Список инн Сетевых орг
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector<String> getINN(String year) {
	// переменная под результат
	Vector<String> result = new Vector();

	try {
	    rs = stat
		    .executeQuery("SELECT presenceid FROM available WHERE year LIKE '"
			    + year + "';");

	    Vector<String> id = new Vector<String>();

	    while (rs.next()) {
		id.add(rs.getString(1).toString());
	    }

	    for (int i = 0; i < id.size(); i++) {
		// Выполняем запрос, который у нас в переменной query
		rs = stat
			.executeQuery("SELECT inn, name FROM presence WHERE id LIKE '"
				+ id.get(i) + "';");

		// запись первой даты
		while (rs.next()) {
		    result.add(rs.getString(1));
		    result.add(rs.getString(2));
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings({ "rawtypes" })
    public Vector<Vector> getInfo(String inn, String month, String year) {
	// переменная под результат
	Vector<Vector> result = new Vector<Vector>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    // rs =
	    // stat.executeQuery("SELECT m1.code, m1.set_all, m1.vn, m1.cn1, m1.cn2, m1.nn FROM set_otpusk AS m1, set_title AS m2 WHERE m1.set_titleid LIKE m2.id AND m2.year LIKE '"
	    // + year.toString() + "' AND m2.inn LIKE '" + inn.toString() +
	    // "' AND m2.month LIKE '" + month.toString() + "';");

	    rs = stat.executeQuery("SELECT id FROM set_title WHERE year LIKE '"
		    + year.toString() + "' AND inn LIKE '" + inn.toString()
		    + "' AND month LIKE '" + month.toString() + "';");

	    if (rs.next()) {
		String id = rs.getString(1);

		rs = stat
			.executeQuery("SELECT set_all, vn, cn1, cn2, nn FROM set_otpusk WHERE set_titleid LIKE '"
				+ id + "';");
		while (rs.next()) {
		    Vector<String> element = new Vector<String>();

		    element.add(rs.getString(1));
		    element.add(rs.getString(2));
		    element.add(rs.getString(3));
		    element.add(rs.getString(4));
		    element.add(rs.getString(5));

		    // Присоединяем список к результату
		    result.add(element);
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Расшифровка кода строки шаблона
     * 
     * @param _code
     *            номер строки
     * @return текст строки
     */
    private String getStringCode(String _code) {
	int code = Integer.parseInt(_code);
	switch (code) {
	case 10: {
	    return "Поступление в сеть из других организаций, в том числе: ";
	}
	case 20: {
	    return "- из сетей ФСК";
	}
	case 30: {
	    return "- от генерирующих компаний и блок-станций";
	}
	case 40: {
	    return "Поступление в сеть из других уровней напряжения (трансформация)";
	}
	case 50: {
	    return "ВН";
	}
	case 60: {
	    return "СН1";
	}
	case 70: {
	    return "СН2";
	}
	case 80: {
	    return "НН";
	}
	case 90: {
	    return "Отпуск из сети, в том числе: ";
	}
	case 100: {
	    return "- конечные потребители (кроме совмещающих с передачей)";
	}
	case 110: {
	    return "- другие сети";
	}
	case 120: {
	    return "- поставщики";
	}
	case 130: {
	    return "Отпуск в сеть других уровней напряжения";
	}
	case 140: {
	    return "Хозяйственные нужды сети";
	}
	case 150: {
	    return "Потери, в том числе:";
	}
	case 160: {
	    return "- относимые на собственное потребление ";
	}
	case 170: {
	    return "Генерация на установках организации (совмещение деятельности)";
	}
	case 180: {
	    return "Собственное потребление (совмещение деятельности)";
	}
	case 190: {
	    return "Небаланс";
	}
	case 210: {
	    return "Поступление в сеть из других организаций, в том числе: ";
	}
	case 220: {
	    return "- из сетей ФСК";
	}
	case 230: {
	    return "- от генерирующих компаний и блок-станций";
	}
	case 240: {
	    return "Поступление в сеть из других уровней напряжения (трансформация)";
	}
	case 250: {
	    return "ВН";
	}
	case 260: {
	    return "СН1";
	}
	case 270: {
	    return "СН2";
	}
	case 280: {
	    return "НН";
	}
	case 290: {
	    return "Отпуск из сети, в том числе: ";
	}
	case 300: {
	    return "- конечные потребители (кроме совмещающих с передачей)";
	}
	case 310: {
	    return "- другие сети";
	}
	case 320: {
	    return "- поставщики";
	}
	case 330: {
	    return "Отпуск в сеть других уровней напряжения";
	}
	case 340: {
	    return "Хозяйственные нужды сети";
	}
	case 350: {
	    return "Потери, в том числе:";
	}
	case 360: {
	    return "- относимые на собственное потребление ";
	}
	case 370: {
	    return "Генерация на установках организации (совмещение деятельности)";
	}
	case 380: {
	    return "Собственное потребление (совмещение деятельности)";
	}
	case 390: {
	    return "Небаланс";
	}
	case 400: {
	    return "Заявленная мощность конечных потребителей ";
	}
	case 410: {
	    return "Присоединенная мощность конечных потребителей";
	}
	case 500: {
	    return "Стоимость поставленных организацией услуг по передаче услуг по передаче";
	}
	case 510: {
	    return "Стоимость приобретенных организацией услуг по передаче";
	}
	case 520: {
	    return "Поступления денежных средств в счет стоимости поставленных услуг по передаче";
	}
	case 530: {
	    return "Уплата денежных средств в счет стоимости приобретенных услуг по передаче";
	}
	}
	return _code;
    }

    // **********************************************************************
    // СБЫТОВОЫЕ
    // **********************************************************************

    /**
     * Получение таблицы по запросу "строки поиска"
     * 
     * @param sqlsearch
     * @return { { год, месяц, наименование },..}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector getSbutSearch(String sqlsearch) {
	// переменная под результат
	Vector result = new Vector();
	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT id,year,month,name FROM sbut_title "
		    + sqlsearch + ";");

	    while (rs.next()) {
		// переменная под элементы строки
		Vector<String> element = new Vector<String>();

		// id
		element.add(rs.getString(1));
		// год
		element.add(rs.getString(2));
		// месяц
		element.add(rs.getString(3));
		// наименование
		element.add(rs.getString(4));

		// Присоединяем список к результату
		result.add(element);
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Сбытовые Проверяет вхождение в БД таблицы с "этими параметрами"
     * 
     * @param month
     *            месяц
     * @param year
     *            год
     * @param inn
     *            инн
     * @param district
     *            муниципальный район
     * @return id найденой записи, -1 если запись не найдена
     */
    public int presenceTableSbut(String month, String year, String inn,
	    String district) {
	try {
	    /*
	     * ишем запись с необходимыми параметрами
	     */
	    rs = stat
		    .executeQuery("SELECT id FROM sbut_title WHERE year LIKE '"
			    + year + "' AND month LIKE '" + month
			    + "' AND inn LIKE '" + inn
			    + "' AND district LIKE '" + district + "';");

	    if (rs.next()) {
		/*
		 * возвращшаем id найденой записи
		 */
		return rs.getInt(1);
	    } else {
		/*
		 * такой записи не было
		 */
		return -1;
	    }
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return -1;
    }

    /**
     * Удаление записи из бд Сбытовые комп
     * 
     * @param id
     *            id удаляемой записи
     */
    public void deleteRowSbut(String id) {
	try {
	    /*
	     * определяем инн, год и месяц удаляемой записи
	     */
	    rs = stat
		    .executeQuery("SELECT inn,year,month FROM sbut_title WHERE id LIKE '"
			    + id + "';");

	    if (rs.next()) {
		// инн удаляемой записи
		String title_inn = rs.getString(1);
		// год удаляемой записи
		String title_year = rs.getString(2);
		// месяц удаляемой записи
		String title_month = rs.getString(3);

		// определяем id таблицы с инн(presence)
		rs = stat
			.executeQuery("SELECT id FROM sbut_presence WHERE inn LIKE '"
				+ title_inn + "';");

		if (rs.next()) {
		    // id таблицы с инн(presence)
		    int presence_id = rs.getInt(1);

		    /*
		     * определяем id таблицы available по: presence_id - id инн
		     * title_year - год записи
		     */
		    rs = stat
			    .executeQuery("SELECT id FROM sbut_available WHERE presenceid LIKE '"
				    + presence_id
				    + "' AND year LIKE '"
				    + title_year + "';");

		    if (rs.next()) {
			// id записи в available
			int available_id = rs.getInt(1);

			// определяем месяц, который нужно обновить
			for (int i = 0; i < months.length; i++) {
			    if (title_month.equals(months[i])) {
				// обновляем запись
				stat.executeUpdate("UPDATE sbut_available SET "
					+ months_eng[i]
					+ " = '' WHERE id LIKE '"
					+ available_id + "';");
				break;
			    }
			}
		    }
		}
	    }

	    // удаление записи
	    // с title
	    stat.executeUpdate("DELETE FROM sbut_title WHERE id = '" + id
		    + "';");
	    // с otpusk
	    stat.executeUpdate("DELETE FROM sbut_otpusk WHERE sbut_titleid = '"
		    + id + "';");
	    // продажа
	    stat.executeUpdate("DELETE FROM sbut_sell WHERE sbut_titleid = '"
		    + id + "';");
	    // покупка
	    stat.executeUpdate("DELETE FROM sbut_buy WHERE sbut_titleid = '"
		    + id + "';");
	    // население
	    stat.executeUpdate("DELETE FROM sbut_nas WHERE sbut_titleid = '"
		    + id + "';");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Добавляет новую запись
     * 
     * @param content_title
     * @param content_otpusk
     */
    public void addTableSbut(ArrayList<String> content_title,
	    ArrayList<String> content_otpusk1,
	    ArrayList<String> content_otpusk2,
	    ArrayList<String> content_otpusk3,
	    ArrayList<String> content_otpusk4,
	    ArrayList<String> content_otpusk5,
	    ArrayList<String> content_otpusk6, ArrayList<String> content_otpusk7) {
	try {
	    // добавление записи в таблицу title
	    pst = conn
		    .prepareStatement("INSERT INTO sbut_title VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

	    // считываем данные с контейнера
	    for (int i = 0; i < content_title.size(); i++) {
		// 1 параметр - автоинкремент(id)
		pst.setString(i + 2, content_title.get(i));
	    }

	    pst.addBatch();

	    pst.executeBatch();

	    /*
	     * Определяем id последней добавленной записи
	     */
	    rs = stat.executeQuery("SELECT last_insert_rowid();");
	    int current_id = rs.getInt(1);

	    /*
	     * добавление записи в таблицу sbut_otpusk
	     */
	    pst = conn
		    .prepareStatement("INSERT INTO sbut_otpusk VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < content_otpusk1.size(); i = i + 7) {
		pst.setInt(2, current_id);
		pst.setInt(3, Integer.parseInt(content_otpusk1.get(i + 6)));
		for (int p = 0; p < 6; p++) {
		    pst.setString(4 + p, content_otpusk1.get(i + p));
		}

		pst.addBatch();
	    }

	    for (int i = 0; i < content_otpusk3.size(); i = i + 7) {
		pst.setInt(2, current_id);
		pst.setInt(3, Integer.parseInt(content_otpusk3.get(i + 6)));
		for (int p = 0; p < 6; p++) {
		    pst.setString(4 + p, content_otpusk3.get(i + p));
		}

		pst.addBatch();
	    }

	    for (int i = 0; i < content_otpusk4.size(); i = i + 7) {
		pst.setInt(2, current_id);
		pst.setInt(3, Integer.parseInt(content_otpusk4.get(i + 6)));
		for (int p = 0; p < 6; p++) {
		    pst.setString(4 + p, content_otpusk4.get(i + p));
		}

		pst.addBatch();
	    }

	    for (int i = 0; i < content_otpusk5.size(); i = i + 7) {
		pst.setInt(2, current_id);
		pst.setInt(3, Integer.parseInt(content_otpusk5.get(i + 6)));
		for (int p = 0; p < 6; p++) {
		    pst.setString(4 + p, content_otpusk5.get(i + p));
		}

		pst.addBatch();
	    }

	    pst.executeBatch();

	    pst = conn
		    .prepareStatement("INSERT INTO sbut_nas VALUES (?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < content_otpusk2.size(); i = i + 5) {
		pst.setInt(2, current_id);
		pst.setInt(3, Integer.parseInt(content_otpusk2.get(i + 4)));
		for (int p = 0; p < 4; p++) {
		    pst.setString(4 + p, content_otpusk2.get(i + p));
		}

		pst.addBatch();
	    }

	    pst.executeBatch();

	    pst = conn
		    .prepareStatement("INSERT INTO sbut_sell VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < content_otpusk6.size(); i = i + 6) {
		pst.setInt(2, current_id);
		for (int p = 0; p < 6; p++) {
		    pst.setString(3 + p, content_otpusk6.get(i + p));
		}

		pst.addBatch();
	    }

	    pst.executeBatch();

	    pst = conn
		    .prepareStatement("INSERT INTO sbut_buy VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < content_otpusk7.size(); i = i + 6) {
		pst.setInt(2, current_id);
		for (int p = 0; p < 6; p++) {
		    pst.setString(3 + p, content_otpusk7.get(i + p));
		}

		pst.addBatch();
	    }

	    pst.executeBatch();

	    /*
	     * Работа с таблицами presence и available
	     */

	    // определяем нахождения инн в presence
	    rs = stat
		    .executeQuery("SELECT id FROM sbut_presence WHERE inn LIKE '"
			    + content_title.get(3) + "';");

	    if (rs.next()) {
		// инн уже включён

		// сохраняем тек id инн
		int presence_id = rs.getInt(1);

		// ищем год новый записи в уже сохранённой записи
		rs = stat
			.executeQuery("SELECT id FROM sbut_available WHERE year LIKE '"
				+ content_title.get(1)
				+ "' AND presenceid LIKE '"
				+ presence_id
				+ "';");

		if (rs.next()) {
		    // год существует

		    // получаем id этого года
		    int available_id = rs.getInt(1);

		    // определяем месяц, который нужно обновить
		    for (int i = 0; i < months.length; i++) {
			if (content_title.get(0).equals(months[i])) {
			    // обновляем запись
			    stat.executeUpdate("UPDATE sbut_available SET "
				    + months_eng[i] + " = '+' WHERE id LIKE '"
				    + available_id + "';");
			    break;
			}
		    }
		} else {
		    // необходимо добавить новый год, т.к. этого нету

		    pst = conn
			    .prepareStatement("INSERT INTO sbut_available VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		    // presence_id - id инн
		    pst.setString(2, Integer.toString(presence_id));
		    // год
		    pst.setString(3, content_title.get(1));

		    // заполняем строку
		    for (int i = 0; i < months.length; i++) {
			if (content_title.get(0).equals(months[i])) {
			    // нужный месяц
			    pst.setString(4 + i, "+");
			} else {
			    // все ост месяца заполняются пустой строкой
			    pst.setString(4 + i, "");
			}
		    }

		    // добавляем в бд
		    pst.addBatch();
		    pst.executeBatch();
		}

	    } else {
		// инн ещё не включали

		// создаём новую запись инн
		pst = conn
			.prepareStatement("INSERT INTO sbut_presence VALUES (?, ?, ?, ?);");
		// наименование организации
		pst.setString(2, content_title.get(2));
		// инн
		pst.setString(3, content_title.get(3));
		// район
		pst.setString(4, content_title.get(6));
		// добавление записи в бд
		pst.addBatch();
		pst.executeBatch();

		// Определяем id последней записи
		rs = stat.executeQuery("SELECT last_insert_rowid();");
		int presence_id = rs.getInt(1);

		// создаём новый год для нового инн
		pst = conn
			.prepareStatement("INSERT INTO sbut_available VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		// presence_id - id инн
		pst.setString(2, Integer.toString(presence_id));
		// год
		pst.setString(3, content_title.get(1));

		// заполняем строку
		for (int i = 0; i < months.length; i++) {
		    if (content_title.get(0).equals(months[i])) {
			// нужный месяц
			pst.setString(4 + i, "+");
		    } else {
			// все ост месяца заполняются пустой строкой
			pst.setString(4 + i, "");
		    }
		}

		// добавляем в бд
		pst.addBatch();
		pst.executeBatch();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		pst.close();
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Получает данные титульнка current_id Сбыт. комп.
     * 
     * @param current_id
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public Vector getSbutTitle(String current_id) {
	// переменная под результат
	Vector<String> element = new Vector<String>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT * FROM sbut_title where id like '"
		    + current_id + "';");

	    while (rs.next()) {
		for (int i = 1; i < 21; i++) {
		    element.add(rs.getString(i));
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return element;
    }

    /**
     * Года, которые находятся в бд
     * 
     * @return список годов
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector getYearsSbut() {
	// переменная под результат
	Vector result = new Vector();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT year FROM sbut_available;");

	    // запись первой даты
	    if (rs.next()) {
		result.add(rs.getString(1));
	    }

	    // добавление ост годов
	    continuebreak: while (rs.next()) {
		// ищем год, который не находится в result
		for (int i = 0; i < result.size(); i++) {
		    if (result.get(i).equals(rs.getString(1))) {
			// переход к след записи
			continue continuebreak;
		    }
		}
		result.add(rs.getString(1));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector<Vector> getDataFromDB_YearSbut(Object year) {
	// переменная под результат
	Vector result = new Vector();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat
		    .executeQuery("SELECT m1.name,m2.* FROM sbut_presence AS m1, sbut_available AS m2 WHERE m2.year like '"
			    + year.toString()
			    + "' AND m1.id LIKE m2.presenceid;");

	    while (rs.next()) {
		Vector<String> element = new Vector<String>();

		// Код
		element.add(rs.getString(1));
		// объём
		for (int i = 0; i < months_eng.length; i++) {
		    element.add(rs.getString(i + 5));
		}

		// Присоединяем список к результату
		result.add(element);
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    /**
     * Список инн Сбытовых компаний
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Vector<String> getNameSbut() {
	// переменная под результат
	Vector<String> result = new Vector();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    rs = stat.executeQuery("SELECT name, inn FROM sbut_presence;");

	    // запись первой даты
	    while (rs.next()) {
		result.add(rs.getString(1));
		result.add(rs.getString(2));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings({ "rawtypes" })
    public Vector<Vector> getInfoSbut(String name, String month, String year,
	    Integer table_begin, Integer table_end) {
	// переменная под результат
	Vector<Vector> result = new Vector<Vector>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    // rs =
	    // stat.executeQuery("SELECT m1.code, m1.set_all, m1.vn, m1.cn1, m1.cn2, m1.nn FROM set_otpusk AS m1, set_title AS m2 WHERE m1.set_titleid LIKE m2.id AND m2.year LIKE '"
	    // + year.toString() + "' AND m2.inn LIKE '" + inn.toString() +
	    // "' AND m2.month LIKE '" + month.toString() + "';");

	    rs = stat
		    .executeQuery("SELECT id FROM sbut_title WHERE year LIKE '"
			    + year.toString() + "' AND name LIKE '"
			    + name.toString() + "' AND month LIKE '"
			    + month.toString() + "';");

	    if (rs.next()) {
		String id = rs.getString(1);

		for (int table = table_begin; table <= table_end; table++) {
		    rs = stat
			    .executeQuery("SELECT set_all, vn, cn1, cn2, nn FROM sbut_otpusk WHERE sbut_titleid LIKE '"
				    + id
				    + "' AND table_id LIKE '"
				    + Integer.toString(table) + "';");

		    while (rs.next()) {
			Vector<String> element = new Vector<String>();

			element.add(rs.getString(1));
			element.add(rs.getString(2));
			element.add(rs.getString(3));
			element.add(rs.getString(4));
			element.add(rs.getString(5));

			// Присоединяем список к результату
			result.add(element);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return result;
    }

    @SuppressWarnings("rawtypes")
    public Vector<Vector> getInfoSbut_nas(String name, String month,
	    String year, Integer table_begin, Integer table_end) {
	// переменная под результат
	Vector<Vector> result = new Vector<Vector>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    // rs =
	    // stat.executeQuery("SELECT m1.code, m1.set_all, m1.vn, m1.cn1, m1.cn2, m1.nn FROM set_otpusk AS m1, set_title AS m2 WHERE m1.set_titleid LIKE m2.id AND m2.year LIKE '"
	    // + year.toString() + "' AND m2.inn LIKE '" + inn.toString() +
	    // "' AND m2.month LIKE '" + month.toString() + "';");

	    rs = stat
		    .executeQuery("SELECT id FROM sbut_title WHERE year LIKE '"
			    + year.toString() + "' AND name LIKE '"
			    + name.toString() + "' AND month LIKE '"
			    + month.toString() + "';");

	    if (rs.next()) {
		String id = rs.getString(1);

		for (int table = table_begin; table <= table_end; table++) {
		    rs = stat
			    .executeQuery("SELECT atr1, atr2, atr3 FROM sbut_nas WHERE sbut_titleid LIKE '"
				    + id
				    + "' AND table_id LIKE '"
				    + Integer.toString(table) + "';");

		    while (rs.next()) {
			Vector<String> element = new Vector<String>();

			element.add(rs.getString(1));
			element.add(rs.getString(2));
			element.add(rs.getString(3));

			// Присоединяем список к результату
			result.add(element);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings("rawtypes")
    public Vector<Vector> getInfoSbut_sell(String name, String month,
	    String year) {
	// переменная под результат
	Vector<Vector> result = new Vector<Vector>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    // rs =
	    // stat.executeQuery("SELECT m1.code, m1.set_all, m1.vn, m1.cn1, m1.cn2, m1.nn FROM set_otpusk AS m1, set_title AS m2 WHERE m1.set_titleid LIKE m2.id AND m2.year LIKE '"
	    // + year.toString() + "' AND m2.inn LIKE '" + inn.toString() +
	    // "' AND m2.month LIKE '" + month.toString() + "';");

	    rs = stat
		    .executeQuery("SELECT id FROM sbut_title WHERE year LIKE '"
			    + year.toString() + "' AND name LIKE '"
			    + name.toString() + "' AND month LIKE '"
			    + month.toString() + "';");

	    if (rs.next()) {
		String id = rs.getString(1);

		rs = stat
			.executeQuery("SELECT atr1, atr2, atr3, atr4, atr5 FROM sbut_sell WHERE sbut_titleid LIKE '"
				+ id + "';");

		while (rs.next()) {
		    Vector<String> element = new Vector<String>();

		    element.add(rs.getString(1));
		    element.add(rs.getString(2));
		    element.add(rs.getString(3));
		    element.add(rs.getString(4));
		    element.add(rs.getString(5));

		    // Присоединяем список к результату
		    result.add(element);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

    @SuppressWarnings("rawtypes")
    public Vector<Vector> getInfoSbut_buy(String name, String month, String year) {
	// переменная под результат
	Vector<Vector> result = new Vector<Vector>();

	try {
	    // Выполняем запрос, который у нас в переменной query
	    // rs =
	    // stat.executeQuery("SELECT m1.code, m1.set_all, m1.vn, m1.cn1, m1.cn2, m1.nn FROM set_otpusk AS m1, set_title AS m2 WHERE m1.set_titleid LIKE m2.id AND m2.year LIKE '"
	    // + year.toString() + "' AND m2.inn LIKE '" + inn.toString() +
	    // "' AND m2.month LIKE '" + month.toString() + "';");

	    rs = stat
		    .executeQuery("SELECT id FROM sbut_title WHERE year LIKE '"
			    + year.toString() + "' AND name LIKE '"
			    + name.toString() + "' AND month LIKE '"
			    + month.toString() + "';");

	    if (rs.next()) {
		String id = rs.getString(1);

		rs = stat
			.executeQuery("SELECT atr1, atr2, atr3, atr4, atr5 FROM sbut_buy WHERE sbut_titleid LIKE '"
				+ id + "';");

		while (rs.next()) {
		    Vector<String> element = new Vector<String>();

		    element.add(rs.getString(1));
		    element.add(rs.getString(2));
		    element.add(rs.getString(3));
		    element.add(rs.getString(4));
		    element.add(rs.getString(5));

		    // Присоединяем список к результату
		    result.add(element);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		rs.close();
		stat.close();
		conn.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return result;
    }

}
