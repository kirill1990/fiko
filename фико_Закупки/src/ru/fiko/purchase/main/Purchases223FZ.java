/**
 * 
 */
package ru.fiko.purchase.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ru.fiko.purchase.supports.ComboItemBooleanValue;
import ru.fiko.purchase.supports.ComboItemIntValue;
import ru.fiko.purchase.windows.Organization;
import ru.fiko.purchase.windows.SearchOrg;
import ru.fiko.purchase.windows.Settings;

public class Purchases223FZ extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7171130112096858890L;

    private static int WIDTH = 800;
    private static int HEIGHT = 500;

    private SearchOrg searchOrg;
    private Organization organization;

    /**
     * Путь к распложению базы данных
     */
    public static String PATHTODB = "purchase.db";

    /**
     * Принадлежность к видам юридическиз лиц, определенных в ч. 2 статьи 1
     * Закона 223-ФЗ<br>
     * <br>
     * !!! Предупреждение: используется только при создание бд!
     */
    private static String[] type_of_org_title = {
	    "Государственная корпорация",
	    "Государственная компания",
	    "Субъект естественной монополии",
	    "4",
	    "5",
	    "6",
	    "7",
	    "8",
	    "9",
	    "10",
	    "11",
	    "12",
	    "13",
	    "14" };

    /**
     * Cпособы размещения заказа<br>
     * <br>
     * !!! Предупреждение: используется только при создание бд!
     */
    private static String[] aspect_title = {
	    "",
	    "Закупка у единственного поставщика (исполнителя, подрядчика)",
	    "Запрос котировок",
	    "Запрос предложений",
	    "Запрос цен",
	    "Открытый аукцион",
	    "Открытый аукцион в электронной форме",
	    "Открытый конкурс" };

    /**
     * Классификация закупки<br>
     * <br>
     * !!! Предупреждение: используется только при создание бд!
     */
    private static String[] type_title = { "", "Товары", "Услуги", "Работа", };

    /**
     * Предмет закупки<br>
     * <br>
     * !!! Предупреждение: используется только при создание бд!
     */
    private static String[] subject_title = {
	    "",
	    "Медоборудование",
	    "Капитальные работы",
	    "Компьютерная техника",
	    "Проектные работы",
	    "Продукт питания"};

    /**
     * Список возможных статусов регистриации организации
     */
    public static Object[] registr_items = {
	    new ComboItemBooleanValue(true, "Зарегистрирована"),
	    new ComboItemBooleanValue(false, "Не зарегистрирована") };

    /**
     * Список возможных статусов закупки
     */
    public static Object[] status_items = {
	    new ComboItemIntValue(0, "Состоялся"),
	    new ComboItemIntValue(1, "1 участник"),
	    new ComboItemIntValue(2, "Нет заявок") };

    /**
     * Список возможных статусов договора
     */
    public static Object[] dogovor_items = {
	    new ComboItemBooleanValue(true, "Заключен"),
	    new ComboItemBooleanValue(false, "Не заключен") };

    public Purchases223FZ() throws SQLException, ClassNotFoundException {
	/*
	 * Инициализация параметров окна
	 */
	this.setSize(WIDTH, HEIGHT);

	// всегда по центру экрана
	// this.setLocation(
	// (Toolkit.getDefaultToolkit().getScreenSize().width - WIDTH) / 2,
	// (Toolkit.getDefaultToolkit().getScreenSize().height - HEIGHT) / 2);
	this.setLocation(150, 100);
	this.setTitle("Закупки - 223 ФЗ");
	// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	this.setVisible(true);

	/*
	 * ssssss Уничтожение процесса после закрытия окна
	 */
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	/**
	 * Первое окно - окно фильтрации организаций
	 */
	setPanelSearchOrg();
    }

    public static void main(String[] args) throws ClassNotFoundException,
	    InstantiationException, IllegalAccessException,
	    UnsupportedLookAndFeelException, SQLException {

	/*
	 * для владельцев ОС windows...
	 */
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	/**
	 * Проверка наличие таблиц базы данных
	 */
	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchases223FZ.PATHTODB);

	Statement stat = conn.createStatement();

	/**
	 * Организация
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS organization(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "inn STRING, name STRING, name_low STRING, regist STRING, polojen_ooc STRING);");

	/**
	 * Список видов деятельности организации
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS types_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "type_of_org_id INTEGER, organization_id INTEGER);");

	/**
	 * Вид деятельности организации
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS type_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "title STRING);");

	/**
	 * Закупки
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS purchase(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "organization_id INTEGER, subject_id INTEGER, aspect_id INTEGER, type_id INTEGER, "
		+ "date STRING, number STRING, subject_title STRING, subject_title_low STRING, status INTEGER, "
		+ "count_all INTERGER, count_do INTGER, torgi_start_cost STRING, torgi_finish_cost STRING, dogovor STRING);");

	/**
	 * Отчет
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS day_d(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "organization_id INTEGER, month STRING, year STRING, count_dogovors INTEGER, summa STRING);");

	/**
	 * Предмет закупки
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS subject(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	/**
	 * Классификация закуки
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS type(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	/**
	 * Способ размещения заказа
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS aspect(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	/**
	 * Если базы данных нет => заполняются таблицы константы!
	 */

	/**
	 * Принадлежность к видам юридическиз лиц, определенных в ч. 2 статьи 1
	 * Закона 223-ФЗ
	 */
	ResultSet test_type_of_org = stat
		.executeQuery("SELECT * FROM type_of_org");

	if (!test_type_of_org.next()) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO type_of_org VALUES (?, ?);");

	    for (String title : type_of_org_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_type_of_org.close();

	/**
	 * Способы размещения заказа
	 */
	ResultSet test_aspect = stat.executeQuery("SELECT * FROM aspect");

	if (!test_aspect.next()) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO aspect VALUES (?, ?);");

	    for (String title : aspect_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_aspect.close();

	/**
	 * Классификация закупки
	 */
	ResultSet test_type = stat.executeQuery("SELECT * FROM type");

	if (!test_type.next()) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO type VALUES (?, ?);");

	    for (String title : type_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_type.close();

	/**
	 * Предмет закупки
	 */
	ResultSet test_subject = stat.executeQuery("SELECT * FROM subject");

	if (!test_subject.next()) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO subject VALUES (?, ?);");

	    for (String title : subject_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_subject.close();

	stat.close();
	conn.close();

	new Purchases223FZ();
    }

    public void setPanelSearchOrg() throws SQLException, ClassNotFoundException {
	this.getContentPane().removeAll();

	if (searchOrg == null)
	    searchOrg = new SearchOrg(this);

	this.getContentPane().add(searchOrg);
	this.validate();
	this.repaint();
    }

    public void updateSearchOrgTable() throws SQLException {
	if (searchOrg != null)
	    searchOrg.updateTable();
    }

    public void setPanelOrganization(int id) throws ClassNotFoundException,
	    SQLException {
	this.getContentPane().removeAll();

	// if (organization == null || organization.getId() != id)
	organization = new Organization(this, id);

	this.getContentPane().add(organization);
	this.validate();
	this.repaint();
    }

    /**
     * Действие на панели "Организация". Открытие/Скрытие панели с
     * дополнительной информацией о организации.
     */
    public void invertAdditonBtnOrganization() {
	if (organization != null)
	    organization.getInfo().ivertAdditionBtn();
    }

    public void setPanelPurchase() {
	this.getContentPane().removeAll();

	this.validate();
	this.repaint();
    }

    public void setPanelSettings() {
	this.getContentPane().removeAll();
	try {
	    this.getContentPane().add(new Settings(this));
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	this.validate();
	this.repaint();
    }

}
