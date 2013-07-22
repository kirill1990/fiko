package ru.fiko.purchase.windows.organization;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ru.fiko.purchase.main.Purchases223FZ;
import ru.fiko.purchase.windows.Organization;

public class Purchases extends JPanel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3877244814326820864L;

    /**
     * Выбранная организия
     */
    private Organization org;

    /**
     * Отфильтрованный список закупок организации
     */
    private JTable purchase_table;

    private PurchaseData data;

    private Filter filter;

    /**
     * @param organization
     * @throws SQLException
     */
    public Purchases(Organization organization) throws SQLException {
	this.org = organization;
	main();
    }

    /**
     * Формирование панели с таблицей закупок организации.
     * 
     * @return панель с таблицей закупок организации
     * @throws SQLException
     */
    private void main() throws SQLException {

	purchase_table = new JTable() {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -5957489805838255399L;

	    // Запрет на редактирование ячеек
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};

	purchase_table.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		// ждём 2 кликов
		if (e.getClickCount() == 2) {
		    // пользователь сделал 2 клика

		    // получаем инф о выбранной таблице
		    JTable target = (JTable) e.getSource();

		    addPurchaseData(Integer.parseInt(target.getValueAt(
			    target.getSelectedRow(), 0).toString()));
		}
	    }

	    public void mouseReleased(MouseEvent e) {
		// TODO доделать PopUp
		final JTable target = (JTable) e.getSource();

		if (0 < target.getSelectedRows().length && e.isMetaDown()) {

		    /**
		     * Удаление выделенных закупок
		     */
		    JMenuItem del = new JMenuItem("Удалить");
		    del.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			    /**
			     * Защита от случайного нажатия
			     */
			    int quest = JOptionPane.showConfirmDialog(null,
				    "Вы уверены, что хотите удалить закупку?",
				    "Вопрос", JOptionPane.YES_NO_OPTION);

			    if (quest != JOptionPane.YES_OPTION)
				return;

			    /**
			     * Выбран пункт удалить
			     */

			    for (int index : target.getSelectedRows()) {

				/**
				 * id удаляемой организации
				 */
				int id = Integer.parseInt(target.getValueAt(
					index, 0).toString());
				try {

				    /**
				     * Удаляем не только организацию, но и
				     * связанные с ней записи
				     */
				    Connection conn = DriverManager
					    .getConnection("jdbc:sqlite:"
						    + Purchases223FZ.PATHTODB);
				    Statement stat = conn.createStatement();

				    stat.executeUpdate("DELETE FROM purchase WHERE id = '"
					    + id + "';");

				    stat.close();
				    conn.close();
				} catch (Exception e) {
				    e.printStackTrace();
				}
			    }

			    updateTable();
			}
		    });

		    JPopupMenu popup = new JPopupMenu();

		    popup.add(del);
		    popup.show(e.getComponent(), e.getX(), e.getY());
		}
	    }
	});

	filter = new Filter(org);
	updateTable();

	this.setLayout(new BorderLayout());
	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
	this.add(filter, BorderLayout.NORTH);
	this.add(new JScrollPane(purchase_table), BorderLayout.CENTER);
    }

    /**
     * Добавление к закупкам реализации данных о закупке.
     * 
     * @param id
     *            - id закупки
     */
    private void addPurchaseData(int id) {
	try {
	    data = new PurchaseData(id, this);

//	    this.add(data, BorderLayout.SOUTH);
	    this.repaint();
	    this.validate();
	} catch (Exception e1) {
	    e1.printStackTrace();
	}
    }

    /**
     * Удаление панели с данными о закупке
     */
    public void removePurchaseData() {
	this.remove(data);
	this.repaint();
	this.validate();
    }

    public void newPurchase() throws SQLException {
	org.getId();

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Purchases223FZ.PATHTODB);

	PreparedStatement pst = conn
		.prepareStatement("INSERT INTO purchase VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

	// ID организации
	pst.setInt(2, org.getId());

	// ID предмета закупки
	pst.setInt(3, 1);
	// ID способа размещения закупки
	pst.setInt(4, 1);
	// ID классификации закупки
	pst.setInt(5, 1);

	// дата
	pst.setString(6, Long.toString(System.currentTimeMillis()));
	// № закупки
	pst.setString(7, "");

	// Наименование закупки
	pst.setString(8, "");
	// Наименование закупки в нижнем регистре
	pst.setString(9, "");

	// Статус - нет заяовк
	pst.setInt(10, 2);

	// Количество заявок
	pst.setInt(11, 0);

	// Количество участников
	pst.setInt(12, 0);

	// Начальная цена
	pst.setString(13, "0");

	// Конечная цена
	pst.setString(14, "0");

	// Договор - не заключен
	pst.setString(15, "false");

	pst.addBatch();

	pst.executeBatch();
	pst.close();

	Statement stat = conn.createStatement();

	/**
	 * Получение id новой организации
	 */
	int id_new_purchase = 0;

	ResultSet get_new_id = stat.executeQuery("SELECT last_insert_rowid();");

	if (get_new_id.next())
	    id_new_purchase = get_new_id.getInt(1);

	get_new_id.close();
	conn.close();

	updateTable();
	addPurchaseData(id_new_purchase);
    }

    /**
     * Обновление данных в таблице закупок
     */
    public void updateTable() {
	try {
	    if (filter != null)
		filter.updatePurchaseTable();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Возвращает таблицу с закупками
     * 
     * @return таблица с закупками
     */
    public JTable getPurchase_table() {
	return purchase_table;
    }
}
