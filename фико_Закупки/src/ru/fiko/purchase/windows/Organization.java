package ru.fiko.purchase.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import ru.fiko.purchase.Main;
import ru.fiko.purchase.windows.organization.Info;
import ru.fiko.purchase.windows.organization.Purchases;

public class Organization extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4404715103010867587L;

    /**
     * Указатель на родителя.<br>
     * Используется для смены компонентов окна.
     */
    private Main zakon223_FZ;

    /**
     * ID организация в базе данных
     */
    private int id;

    /**
     * Панель с данными организации
     */
    private Info info;

    /**
     * Панель с таблицей закупок и подробными данными о закупке.
     */
    private Purchases purchase;

    /**
     * Формирует панель с всеми данными о организации.
     * 
     * @param parent
     *            - Указатель на родителя. Используется для смены компонентов
     *            окна.
     * @param id
     *            - id организации
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Organization(Main parent, int id) throws SQLException {

	this.zakon223_FZ = parent;
	this.id = id;

	/**
	 * Сборка панели с даннымми организации и панели фильтрации закупок
	 */
	JPanel top = new JPanel(new BorderLayout());
	top.add(this.info = new Info(this), BorderLayout.NORTH);

	/**
	 * Настройки панели организации
	 */
	this.setLayout(new BorderLayout(5, 5));
	this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	this.add(top, BorderLayout.NORTH);
	this.add(data_panel(), BorderLayout.CENTER);
	this.add(foot_btns(), BorderLayout.SOUTH);

    }

    /**
     * Возвращает id Организации, которая открыта в данной панели
     * 
     * @return id организации
     */
    public int getId() {
	return id;
    }

    /**
     * Возвращение к панели поиска организации
     */
    public void backToSearchOrg() {
	try {
	    zakon223_FZ.setPanelSearchOrg();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Формирование панели с управляющими кнопками.
     * 
     * @return панель с управляющими кнопкамии
     */
    private JPanel foot_btns() {
	/**
	 * Управляющие кнопки
	 */

	JButton btn_update = new JButton("Обновить данные");
	btn_update.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    info.save_Org_Info();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	});

	JButton btn_prev2 = new JButton("Добавить закупку");
	btn_prev2.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		try {
		    purchase.newPurchase();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	});

	JButton btn_prev = new JButton("Назад");
	btn_prev.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		backToSearchOrg();
	    }
	});

	/**
	 * Сборка управляющих кнопок
	 */
	JPanel foot_btns = new JPanel(new GridLayout(1, 3, 5, 5));
	// foot_btns.add(btn_update);
	foot_btns.add(btn_prev2);
	foot_btns.add(btn_prev);

	JPanel foot = new JPanel(new BorderLayout());
	foot.add(foot_btns, BorderLayout.EAST);

	return foot;
    }

    /**
     * Формирование панели с данными организации о закупках и отчетности
     * 
     * @return data_panel - панель с двумя вкладками "закупки" и "отчетность"
     * @throws SQLException
     */
    private JPanel data_panel() throws SQLException {

	JTabbedPane tabbed = new JTabbedPane();
	tabbed.add("Закупки", this.purchase = new Purchases(this));
	tabbed.add("Отчет", new JLabel("В разработке"));

	JPanel data_panel = new JPanel(new BorderLayout());
	data_panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
	data_panel.add(tabbed);

	return data_panel;
    }

    public Main getZakon223_FZ() {
	return zakon223_FZ;
    }

    /**
     * Возвращает панель с данными организации
     * 
     * @return info - панель с данными организации
     */
    public Info getInfo() {
	return info;
    }

    /**
     * Возвращает панель с таблицей закупок и подробными данными о закупке.
     * 
     * @return purchase - панель с таблицей закупок и подробными данными о
     *         закупке.
     */
    public Purchases getPurchase() {
	return purchase;
    }
}
