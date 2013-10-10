/**
 * 
 */
package ru.fiko.purchase;

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

import ru.fiko.purchase.windows.Statistics;
import ru.fiko.purchase.windows.TableOrg;
import ru.fiko.purchase.windows.Settings;
import ru.fiko.purchase.windows.organization.Organization;

public class Main extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7171130112096858890L;

    private static int WIDTH = 800;
    private static int HEIGHT = 500;

    private TableOrg searchOrg;
    private Organization organization;
//    private Statistics statistics;

    public Main() throws SQLException, ClassNotFoundException {
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
		+ Constant.PATHTODB);

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
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS report(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
		+ "organization_id INTEGER, report_type_id INTEGER, month INTEGER, year STRING, count_dogovors INTEGER, summa STRING);");

	/**
	 * Вид отчета
	 */
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS report_type(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	
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

	    /**
	     * Принадлежность к видам юридическиз лиц, определенных в ч. 2
	     * статьи 1 Закона 223-ФЗ<br>
	     * <br>
	     * !!! Предупреждение: используется только при создание бд!
	     */
	    String[] type_of_org_title = {
		    "Государственная корпорация",
		    "Государственная компания",
		    "Субъект естественной монополии" };

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

	    /**
	     * Cпособы размещения заказа<br>
	     * <br>
	     * !!! Предупреждение: используется только при создание бд!
	     */
	    String[] aspect_title = {
		    "",
		    "Закупка у единственного поставщика (исполнителя, подрядчика)",
		    "Запрос котировок",
		    "Запрос предложений",
		    "Запрос цен",
		    "Открытый аукцион",
		    "Открытый аукцион в электронной форме",
		    "Открытый конкурс" };

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

	    /**
	     * Классификация закупки<br>
	     * <br>
	     * !!! Предупреждение: используется только при создание бд!
	     */
	    String[] type_title = { "", "Товары", "Услуги", "Работа", };

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

	    /**
	     * Предмет закупки<br>
	     * <br>
	     * !!! Предупреждение: используется только при создание бд!
	     */
	    String[] subject_title = {
		    "",
		    "Медоборудование",
		    "Капитальные работы",
		    "Компьютерная техника",
		    "Проектные работы",
		    "Продукт питания" };

	    for (String title : subject_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_subject.close();
	
	/**
	 * Предмет закупки
	 */
	ResultSet test_report = stat.executeQuery("SELECT * FROM report_type");

	if (!test_report.next()) {
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO report_type VALUES (?, ?);");

	    /**
	     * Предмет закупки<br>
	     * <br>
	     * !!! Предупреждение: используется только при создание бд!
	     */
	    String[] subject_title = {
		    "Договоры по результатам закупок",
		    "Договоры по рез. закупок у единственного поставщика",
		    "Договоры по рез. закупки(гос. тайна)" };

	    for (String title : subject_title) {
		pst.setString(2, title);
		pst.addBatch();
	    }
	    pst.executeBatch();
	    pst.close();
	}

	test_report.close();

	stat.close();
	conn.close();

	new Main();
    }

    public void setPanelSearchOrg() throws SQLException, ClassNotFoundException {
	this.getContentPane().removeAll();

	if (searchOrg == null)
	    searchOrg = new TableOrg(this);

	this.getContentPane().add(searchOrg);
	this.validate();
	this.repaint();
    }
    
    public void setPanelStatistics() throws SQLException, ClassNotFoundException {
   	this.getContentPane().removeAll();


   	this.getContentPane().add(new Statistics(this));
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
