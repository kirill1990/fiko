/**
 * 
 */
package ru.fiko.purchase.main;

import java.awt.Toolkit;
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

import ru.fiko.purchase.supports.ComboItemRegistr;
import ru.fiko.purchase.windows.Organization;
import ru.fiko.purchase.windows.SearchOrg;

public class Zakon223_FZ extends JFrame {

    /**
     * 
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
    private static String[] type_title = { "Товары", "Услуги", "Работа", };

    /**
     * Предмет закупки<br>
     * <br>
     * !!! Предупреждение: используется только при создание бд!
     */
    private static String[] subject_title = {
	    "Медоборудование",
	    "Капитальные работы",
	    "Компьютерная техника",
	    "Проектные работы" };

    public static Object[] registr_items = {
	    new ComboItemRegistr(true, "Зарегистрирована"),
	    new ComboItemRegistr(false, "Не зарегистрирована") };

    public Zakon223_FZ() throws SQLException, ClassNotFoundException {
	/*
	 * Инициализация параметров окна
	 */
	this.setSize(WIDTH, HEIGHT);

	// всегда по центру экрана
	this.setLocation(
		(Toolkit.getDefaultToolkit().getScreenSize().width - WIDTH) / 2,
		(Toolkit.getDefaultToolkit().getScreenSize().height - HEIGHT) / 2);
	this.setTitle("Тест");
	// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	this.setVisible(true);

	/*
	 * Уничтожение процесса после закрытия окна
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
		+ Zakon223_FZ.PATHTODB);

	Statement stat = conn.createStatement();

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS organization(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, inn STRING, name STRING, regist STRING, polojen_ooc STRING);");

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS types_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, type_of_org_id INTEGER, organization_id INTEGER);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS type_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS purchase(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, organization_id INTEGER, subject_id INTEGER, aspect_id INTEGER, type_id INTEGER, date STRING, number STRING, subject_title STRING, status INTEGER, count_all INTERGER, count_do INTGER, torgi_start_cost STRING, torgi_finish_cost STRING);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS day_d(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, organization_id INTEGER, month STRING, year STRING, count_dogovors INTEGER, summa STRING);");

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS subject(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS type(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");
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

	new Zakon223_FZ();
    }

    public void setPanelSearchOrg() throws SQLException, ClassNotFoundException {
	this.getContentPane().removeAll();

	if (searchOrg != null)
	    this.getContentPane().add(searchOrg);
	else
	    this.getContentPane().add(searchOrg = new SearchOrg(this));

	this.validate();
	this.repaint();
    }

    public void setPanelOrganization(int id) throws ClassNotFoundException,
	    SQLException {
	this.getContentPane().removeAll();

	if (organization != null && organization.getId() == id)
	    this.getContentPane().add(organization);
	else
	    this.getContentPane()
		    .add(organization = new Organization(this, id));

	this.validate();
	this.repaint();
    }

    public void setPanelPurchase() {
	this.getContentPane().removeAll();

	this.validate();
	this.repaint();
    }

}
