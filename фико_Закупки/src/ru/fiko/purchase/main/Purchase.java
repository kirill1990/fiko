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

import ru.fiko.purchase.windows.SearchOrg;

public class Purchase extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -7171130112096858890L;

    private static int WIDTH = 800;
    private static int HEIGHT = 500;

    /**
     * Путь к распложению базы данных
     */
    public static String PATHTODB = "purchase.db";

    /**
     * Принадлежность к видам юридическиз лиц, определенных в ч. 2 статьи 1
     * Закона 223-ФЗ<br>
     * <br>
     * Заполняются только при создание бд.
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

    public Purchase() throws SQLException, ClassNotFoundException {
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
	this.getContentPane().add(new SearchOrg());

	this.validate();
    }

    public static void main(String[] args) throws ClassNotFoundException,
	    InstantiationException, IllegalAccessException,
	    UnsupportedLookAndFeelException, SQLException {

	/*
	 * для владельцев ОС windows...
	 */
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	/**
	 * Проверка наличие базы данных
	 */
	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchase.PATHTODB);

	Statement stat = conn.createStatement();

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS organization(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, inn STRING, name STRING, regist INTEGER, polojen_ooc STRING, types_of_org_id INTEGER);");

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS types_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, type_of_org_id INTEGER, organization_id INTEGER);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS type_of_org(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title STRING);");

	/**
	 * Если базы данных нет => заполняются таблицы константы!
	 */
	ResultSet test_type_of_org = stat
		.executeQuery("SELECT * FROM type_of_org");

	if (!test_type_of_org.next()) {
	    /**
	     * Принадлежность к видам юридическиз лиц, определенных в ч. 2
	     * статьи 1 Закона 223-ФЗ
	     */
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

	// stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, presenceid INTEGER, year STRING, month STRING);");
	// stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpusk(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, titleid INTEGER, code STRING, vall STRING, vpribor STRING, vraschet STRING, sall STRING, spribor STRING, sraschet STRING);");
	stat.close();
	conn.close();

	new Purchase();
    }

}
