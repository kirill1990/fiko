package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import output.ToExcelKOyear;
import output.ToExcelKOyearSbut;
import output.ToExcelSbut;
import output.ToExcelSetev;

import basedata.AddTableSbut;
import basedata.AddTableSetev;
import basedata.ConnectionBD;
import basedata.Listener;

public class Main extends JFrame
{
	private static final long	serialVersionUID	= 1L;

	public final static int		WIDTH				= 700;	// ширина окна в px
	public final static int		HEIGHT				= 500;	// высота окна в px

	private String				sqlsearch			= "";
	private JTable				jDataTable			= null;

	private String				sqlsearch_sbut		= "";
	private JTable				jDataTable_sbut		= null;

	public JTabbedPane			tab					= null;

	private Main				main				= null;

	public Main()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		/*
		 * Определяем размер окна
		 * WIDTH - ширина окна
		 * HEIGHT - высота окна
		 */
		setSize(WIDTH, HEIGHT);

		/*
		 * Определяем положение окна на рабочем столе
		 * выставляем по центру
		 */

		/*
		 * получаем данные о разрешение экрана
		 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		/*
		 * ставим окно по середине
		 */
		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		/*
		 * Определяем заголовок окна
		 */
		setTitle("Электроэнергия по 46");

		/*
		 * закрытие окна
		 * принудительное завершение работы программы
		 */
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		getContentPane().add(mainPanel());

		validate();

		main = this;
	}

	/*
	 * Создание основной панели
	 * панели вкладок
	 */
	public JPanel mainPanel()
	{

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		/*
		 * Создание панели вкладок
		 */
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));
		tabbedPane.addTab("Сетевые организации", getSetevPanel());
		tabbedPane.addTab("Сбытовые компании", getSbutPanel());
		tab = tabbedPane;
		mainPanel.add(tabbedPane);

		return mainPanel;
	}

	/**
	 * Вкладка "Сетевые организации"
	 * 
	 * -строка поиска
	 * -таблица
	 * 
	 * @return JPanel
	 */
	@SuppressWarnings("serial")
	private JPanel getSetevPanel()
	{
		/*
		 * панель "Сетевые организации"
		 */
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		/*
		 * панель поиска
		 */
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout(5, 0));
		panel.add(searchPanel, BorderLayout.NORTH);

		/*
		 * надпись строки поиска
		 */
		searchPanel.add(new JLabel("Строка поиска:"), BorderLayout.WEST);

		/*
		 * текстовое поле для ввода данных поиска
		 */
		final JTextField jSearchTextField = new JTextField();
		searchPanel.add(jSearchTextField);
		/*
		 * событие текстового поля
		 */
		jSearchTextField.getDocument().addDocumentListener(new DocumentListener()
		{

			public void changedUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void removeUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void insertUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void updateSearchString()
			{
				// Обнуляем запрос поиска
				sqlsearch = " ";
				// разбиваем поиск на слова

				String[] result = jSearchTextField.getText().split(" ");
				// проверяем на пустую строку
				if (result.length > 0)
				{
					// если не пустая, создаём запрос
					sqlsearch += " where  search like '%" + result[0].toLowerCase() + "%' ";
					// и если не 1 параметр запроса
					for (int i = 1; i < result.length; i++)
					{
						// то добавляем ост параметры к запросу
						sqlsearch += " and search like '%" + result[i].toLowerCase() + "%' ";
					}
				}
				// обновялем данные в таблице
				refreshTable();

				validate();
			}
		});

		/*
		 * панель кнопок
		 * 1. добавление записей
		 * 2. просмотр занесённых организаций
		 */
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		/*
		 * добавление записей
		 */
		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(new BUTTON_add());
		buttonsPanel.add(addButton, null);

		/*
		 * просмотр занесённых организаций
		 */
		JButton svodButton = new JButton("Организации");
		svodButton.addActionListener(new BUTTON_review());
		buttonsPanel.add(svodButton, null);

		/*
		 * панель таблицы
		 */
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		panel.add(tablePanel, BorderLayout.CENTER);

		/*
		 * таблица с данными
		 */
		jDataTable = new JTable()
		{
			/*
			 * Запрет на редактирование ячеек
			 */
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		/*
		 * добавляем скроллбар
		 */
		tablePanel.add(new JScrollPane(jDataTable), null);
		// Открытие подробной информации о записи
		// двойной клик мыши по строчке
		jDataTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				// ждём 2 кликов
				if (e.getClickCount() == 2)
				{
					// пользователь сделал 2 клика

					// получаем инф о выбранной таблице
					JTable target = (JTable) e.getSource();

					// создание формы
					JPanel mainPanel = new JPanel();
					mainPanel.setLayout(new BorderLayout(5, 5));
					mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

					// создание панели кнопок
					JPanel buttonsPanel = new JPanel();
					buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
					mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

					/*
					 * Кнопка "назад"
					 */
					JButton preButton = new JButton("Назад");
					preButton.setFocusable(false);
					buttonsPanel.add(preButton);
					preButton.addActionListener(new ActionListener()
					{
						/*
						 * Очишает форму и возврашает к основной вкладке
						 * (non-Javadoc)
						 * @see
						 * java.awt.event.ActionListener#actionPerformed(java.awt
						 * .event.ActionEvent)
						 */
						public void actionPerformed(ActionEvent e)
						{
							getContentPane().removeAll();
							getContentPane().add(mainPanel());
							validate();
						}
					});

					// Создание панели вкладок
					JTabbedPane tabbedPane = new JTabbedPane();
					tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));
					// доавбление панелей во вкладки
					tabbedPane.addTab("Титульник", getTitlePanel(jDataTable.getValueAt(target.getSelectedRow(), 0).toString()));
					tabbedPane.addTab("Отпуск ЭЭ сет организациями", getOtpuskPanel(jDataTable.getValueAt(target.getSelectedRow(), 0).toString()));

					mainPanel.add(tabbedPane);
					getContentPane().removeAll();
					// добавление всех элементов на форму
					getContentPane().add(mainPanel);
					validate();
				}
			}
		});

		// Реализация PopUp Menu
		jDataTable.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent Me)
			{
				if (0 < jDataTable.getSelectedRows().length && Me.isMetaDown())
				{
					JPopupMenu Pmenu = new JPopupMenu();

					// количество выделенных записей
					// для удобства пользователей
					JMenuItem numberRecords = new JMenuItem("Выделено: " + jDataTable.getSelectedRows().length);
					Pmenu.add(numberRecords);

					if (jDataTable.getSelectedRows().length > 0 && jDataTable.getSelectedRows().length < 2)
					{
						final String year = (String) jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 1);

						JMenuItem svod = new JMenuItem("Создать свод за: " + year);
						Pmenu.add(svod);

						svod.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								new ToExcelSetev(year);
							}
						});
					}
					// удаляем выделенные элементы
					JMenuItem delRecords = new JMenuItem("Удалить:" + jDataTable.getSelectedRows().length);
					Pmenu.add(delRecords);

					// показываем PopUp меню
					Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());

					// удаление записей
					delRecords.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							// Сообщение

							// варианты ответа пользователя
							String[] choices = { "Да", "Нет" };

							// создание сообщения
							int response = JOptionPane.showOptionDialog(null // В
																				// центре
																				// окна
							, "Вы уверены, что хотите удалить " + jDataTable.getSelectedRows().length + " элементов?" // Сообщение
							, "" // Титульник сообщения
							, JOptionPane.YES_NO_OPTION // Option type
							, JOptionPane.PLAIN_MESSAGE // messageType
							, null // Icon (none)
							, choices // Button text as above.
							, "" // Default button's labelF
							);

							// обработка ответа пользователя
							switch (response)
							{
								case 0:
									// удаление
									for (int i = 0; i < jDataTable.getSelectedRows().length; i++)
									{
										new ConnectionBD().deleteRow(jDataTable.getValueAt(jDataTable.getSelectedRows()[i], 0).toString());
									}
									// обновляем таблицу
									refreshTable();
									break;
								case 1:
									// ничего не удаляем
									break;
								case -1:
									// окно было закрыто - ничего не удаляем
								default:
									break;
							}

						}
					});
				}
			}
		});

		refreshTable();
		return panel;
	}

	/**
	 * Кнопка, приводит к сценарию добавления Сетевых орг.
	 * 
	 * @author kirill
	 * 
	 */
	public class BUTTON_add implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			/*
			 * диалоговое окно
			 * фильтр установлен на ПАПКИ
			 */
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			/*
			 * с помошью returnValue определим отклик пользователя
			 * была отмена или выбрана директория
			 */
			int returnValue = fileChooser.showOpenDialog(new JLabel());

			/*
			 * выбранная директория
			 */
			File selectedFile = fileChooser.getSelectedFile();

			/*
			 * далее только с правильной директорией
			 */
			if (returnValue != JFileChooser.CANCEL_OPTION)
			{
				final DefaultListModel listNames = new DefaultListModel();
				final DefaultListModel listPaths = new DefaultListModel();

				/*
				 * если директория существует => получаем список
				 * excel файлов(.xls)
				 * listNames - название файлов
				 * listPaths - пути к файлам
				 * listNames[i] <=> listPaths[i]
				 */
				if (selectedFile != null)
				{
					Listener listener = new Listener(selectedFile.getAbsolutePath());

					for (int i = 0; i < listener.getListNames().size(); i++)
					{
						/*
						 * количество listNames совпадает с количеством
						 * listPaths
						 */
						listNames.addElement(listener.getListNames().get(i));
						listPaths.addElement(listener.getListPaths().get(i));
					}
				}

				/*
				 * создание панели
				 */
				final JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout(5, 5));
				panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

				/*
				 * панель вывода списка
				 */
				JPanel listPanel = new JPanel();
				listPanel.setLayout(new BorderLayout(5, 5));
				listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				panel.add(listPanel, BorderLayout.CENTER);

				/*
				 * прогресс бар
				 * показывает количество внесённых в бд записей
				 */
				final JProgressBar jProgressbar = new JProgressBar();
				listPanel.add(jProgressbar, BorderLayout.SOUTH);

				/*
				 * компонент списка со скроллом
				 */
				final JList list = new JList(listNames);
				listPanel.add(new JScrollPane(list));

				/*
				 * панель кнопок
				 */
				JPanel buttonsPanel = new JPanel();
				buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
				buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
				panel.add(buttonsPanel, BorderLayout.SOUTH);

				/*
				 * Кнопка "Добавить"
				 */
				JButton folderButton = new JButton("Добавить");
				folderButton.setFocusable(false);
				buttonsPanel.add(folderButton);
				folderButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{/*
					 * диалоговое окно
					 * фильтр установлен на ПАПКИ
					 */
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						/*
						 * с помошью returnValue определим отклик пользователя
						 * была отмена или выбрана директория
						 */
						int returnValue = fileChooser.showOpenDialog(new JLabel());

						/*
						 * выбранная директория
						 */
						File selectedFile = fileChooser.getSelectedFile();

						/*
						 * далее только с правильной директорией
						 */
						if (returnValue != JFileChooser.CANCEL_OPTION)
						{
							if (selectedFile != null)
							{
								Listener listener = new Listener(selectedFile.getAbsolutePath());

								/*
								 * добавление элементов к предыдушему списку
								 */
								for (int i = 0; i < listener.getListNames().getSize(); i++)
								{
									/*
									 * listNames - название файлов
									 * listPaths - пути к файлам
									 * listNames[i] <=> listPaths[i]
									 */
									listNames.addElement(listener.getListNames().getElementAt(i));
									listPaths.addElement(listener.getListPaths().getElementAt(i));
								}
							}

							validate();
						}
					}
				});

				/*
				 * Кнопка "Внести"
				 */
				JButton addButton = new JButton("Внести");
				addButton.setFocusable(false);
				buttonsPanel.add(addButton);
				addButton.addActionListener(new ActionListener()
				{
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e)
					{
						main.disable();

						jProgressbar.setMaximum(listPaths.getSize());
						jProgressbar.setMinimum(0);
						jProgressbar.setValue(0);

						AddTableSetev thread = new AddTableSetev();

						thread.setJProgressBar(jProgressbar);
						thread.setMain(main);

						thread.setListPaths(listPaths);
						thread.setListNames(listNames);

						// запускаем поток
						thread.execute();

					}
				});

				/*
				 * Кнопка "удалить из списка"
				 */
				JButton removeButton = new JButton("Удалить из списка");
				removeButton.setFocusable(false);
				buttonsPanel.add(removeButton);
				removeButton.addActionListener(new ActionListener()
				{
					/*
					 * Удаление из списка
					 * (non-Javadoc)
					 * @see
					 * java.awt.event.ActionListener#actionPerformed(java.awt
					 * .event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e)
					{
						/*
						 * Если не удалять, то всегда будет возвращаться true!
						 */
						while (list.isSelectedIndex(list.getSelectedIndex()))
						{
							/*
							 * 1. удаляем элемент из списка адресса файла
							 * 2. удаляем элемент из списка имен файла
							 */
							listPaths.removeElementAt(list.getSelectedIndex());
							listNames.removeElementAt(list.getSelectedIndex());
						}
					}
				});

				/*
				 * Кнопка "назад"
				 */
				JButton preButton = new JButton("Назад");
				preButton.setFocusable(false);
				buttonsPanel.add(preButton);
				preButton.addActionListener(new ActionListener()
				{
					/*
					 * Очишает форму и возврашает к основной вкладке
					 * (non-Javadoc)
					 * @see
					 * java.awt.event.ActionListener#actionPerformed(java.awt
					 * .event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e)
					{
						getContentPane().removeAll();
						getContentPane().add(mainPanel());
						validate();
					}
				});

				getContentPane().removeAll();
				// добавление всех элементов на форму
				getContentPane().add(panel);
				// обновление формы
				validate();
			}
		}
	}

	/**
	 * Кнопка, "Просмотр организаций" показывает сетевые орг, которые подали
	 * отчеты
	 * 
	 * @author kirill
	 * 
	 */
	public class BUTTON_review implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// создание формы
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout(5, 5));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			// создание панели с таблицой
			JPanel tablePanel = new JPanel();
			tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
			// mainPanel.add(tablePanel, BorderLayout.CENTER);

			// Создание панели вкладок
			JTabbedPane tabbedPane = new JTabbedPane();
			// шрифт вкладок
			tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));

			@SuppressWarnings("rawtypes") Vector years = new ConnectionBD().getYears();

			for (int i = 0; i < years.size(); i++)
			{
				// добавление панелей во вкладки
				tabbedPane.addTab(years.get(i).toString(), getYearPanel(years.get(i)));
			}

			setSize(WIDTH + 300, HEIGHT);

			mainPanel.add(tabbedPane);
			getContentPane().removeAll();
			// добавление всех элементов на форму
			getContentPane().add(mainPanel);
			// обновление формы
			validate();
		}
	}

	/**
	 * Показывает содержимое титульника записи Сетевые орг
	 * 
	 * @param current_id
	 *            - id показываемой записи
	 * @return панель
	 */
	private JPanel getTitlePanel(String current_id)
	{
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(22, 2));

		for (int i = 0; i < 22; i++)
		{
			JPanel labelPanel = new JPanel();
			labelPanel.setLayout(new BorderLayout(5, 0));
			// new ConnectionBD()
			@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_title(current_id);

			JTextField textField = new JTextField("");
			textField.setEditable(false);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			// textField.setHorizontalAlignment(JTextField.RIGHT);
			JLabel jLabel = new JLabel("");

			switch (i)
			{
				case 0:
				{
					textField.setText(values.get(3).toString());
					textField.setHorizontalAlignment(JTextField.CENTER);
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					labelPanel.add(textField, null);
					break;
				}
				case 1:
				{
					jLabel.setText("Отчетный период: ");
					textField.setText(values.get(1) + " " + values.get(2));
					break;
				}
				case 2:
				{
					jLabel.setText("Муниципальный район");
					textField.setText(values.get(7).toString());
					break;
				}
				case 3:
				{
					jLabel.setText("Муниципальное образование: ");
					textField.setText(values.get(8).toString());
					break;
				}
				case 4:
				{
					jLabel.setText("ОКТМО: ");
					textField.setText(values.get(9).toString());
					break;
				}
				case 5:
				{
					jLabel.setText("ИНН: ");
					textField.setText(values.get(4).toString());
					break;
				}
				case 6:
				{
					jLabel.setText("КПП: ");
					textField.setText(values.get(5).toString());
					break;
				}
				case 7:
				{
					jLabel.setText("Код по ОКПО: ");
					textField.setText(values.get(6).toString());
					break;
				}
				case 8:
				{
					textField.setText("Адрес организации");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 9:
				{
					jLabel.setText("Юридический адрес: ");
					textField.setText(values.get(10).toString());
					break;
				}
				case 10:
				{
					jLabel.setText("Почтовый адрес: ");
					textField.setText(values.get(11).toString());
					break;
				}
				case 11:
				{
					textField.setText("Руководитель");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 12:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(12).toString());
					break;
				}
				case 13:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(13).toString());
					break;
				}
				case 14:
				{
					textField.setText("Главный бухгалтер");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 15:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(14).toString());
					break;
				}
				case 16:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(15).toString());
					break;
				}
				case 17:
				{
					textField.setText("Должностное лицо, ответственное за составление формы");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 18:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(16).toString());
					break;
				}
				case 19:
				{
					jLabel.setText("Должность: ");
					textField.setText(values.get(17).toString());
					break;
				}
				case 20:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(18).toString());
					break;
				}
				case 21:
				{
					jLabel.setText("e-mail: ");
					textField.setText(values.get(19).toString());
					break;
				}
			}
			labelPanel.add(jLabel, BorderLayout.WEST);
			labelPanel.add(textField, null);
			titlePanel.add(labelPanel, null);
		}
		return titlePanel;
	}

	/**
	 * Показывает содержимое отпуска записи Сетевые орг
	 * 
	 * @param current_id
	 * @return
	 */
	private JPanel getOtpuskPanel(String current_id)
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));

		@SuppressWarnings("serial") JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), null);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_Otpusk(current_id);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Код");
		header.add("Наименование");
		header.add("Всего");
		header.add("ВН");
		header.add("СН1");
		header.add("СН2");
		header.add("НН");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// код
		jtable.getColumnModel().getColumn(0).setMaxWidth(30);
		jtable.getColumnModel().getColumn(0).setMinWidth(30);
		// расшифровка кода
		jtable.getColumnModel().getColumn(1).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(1).setMinWidth(250);
		// информация
		for (int i = 2; i < 7; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(80);
		}

		return tablePanel;
	}

	/**
	 * Панель сетевых организаций за год(создание отд вкладки
	 * 
	 * @param year
	 * @return
	 */
	@SuppressWarnings("serial")
	private JPanel getYearPanel(Object year)
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		// tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.setLayout(new BorderLayout(5, 5));

		// создание панели кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.add(buttonsPanel, BorderLayout.SOUTH);

		// Добавление таблиц
		JButton addButton = new JButton("Сохранить в Excel " + year.toString() + " год.");

		TableToExcel asdsad = new TableToExcel();
		asdsad.setYear(year.toString());
		addButton.addActionListener(asdsad);

		buttonsPanel.add(addButton, null);

		/*
		 * Кнопка "назад"
		 */
		JButton preButton = new JButton("Назад");
		preButton.setFocusable(false);
		buttonsPanel.add(preButton);
		preButton.addActionListener(new ActionListener()
		{
			/*
			 * Очишает форму и возврашает к основной вкладке
			 * (non-Javadoc)
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt
			 * .event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e)
			{
				getContentPane().removeAll();
				getContentPane().add(mainPanel());

				setSize(WIDTH, HEIGHT);

				validate();
			}
		});

		JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		// jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), BorderLayout.CENTER);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_Year(year);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Организация");
		header.add("Январь");
		header.add("Февраль");
		header.add("Март");
		header.add("Апрель");
		header.add("Май");
		header.add("Июнь");
		header.add("Июль");
		header.add("Август");
		header.add("Сентябрь");
		header.add("Октябрь");
		header.add("Ноябрь");
		header.add("Декабрь");
		header.add("Год");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// наименование
		jtable.getColumnModel().getColumn(0).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(0).setMinWidth(300);
		// месяцы
		for (int i = 1; i < 14; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(50);
		}

		// дополнительное редактирование ячеек
		jtable.setDefaultRenderer(jtable.getColumnClass(1), new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };

				if (column < 1)
				{
					// название организации
					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.LEFT);

					for (int i = 0; i < districts.length; i++)
					{
						if (districts[i].equals(value))
						{
							super.setBackground(Color.LIGHT_GRAY);
							break;
						}
					}
				}
				else
				{
					// в ячейках месяцев

					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.CENTER);

					// определение содержимого ячейки
					if (value.equals("+"))
					{
						// если содержит знак +
						super.setBackground(Color.GREEN);
					}
					else
					{
						// ничего в ячейке нету
						super.setBackground(Color.WHITE);
					}
				}

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				return this;
			}

		});
		return tablePanel;
	}

	/**
	 * Кнопка "Запись списка Сетевых орг в ексель"
	 * 
	 * @author kirill
	 * 
	 */
	private class TableToExcel implements ActionListener
	{
		String	year	= null;

		public void actionPerformed(ActionEvent e)
		{
			if (year != null)
			{
				new ToExcelKOyear(year);
			}
		}

		public void setYear(String year)
		{
			this.year = year;
		}
	}

	/**
	 * Обновляет данные в таблице Сетевые организации
	 */
	private void refreshTable()
	{
		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB(sqlsearch);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("id");
		header.add("Год");
		header.add("Месяц");
		header.add("Наименование");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jDataTable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);
		// задаем ширину каждого столбца, кроме наименования
		// id
		jDataTable.getColumnModel().getColumn(0).setMaxWidth(40);
		// год
		jDataTable.getColumnModel().getColumn(1).setMaxWidth(40);
		// месяц
		jDataTable.getColumnModel().getColumn(2).setMaxWidth(80);
		// Под название организации отводится всё оставшиеся пространство
	}

	/**
	 * Вкладка "Сбытовые компании"
	 * 
	 * -строка поиска
	 * -таблица
	 * 
	 * @return JPanel
	 */
	@SuppressWarnings("serial")
	private JPanel getSbutPanel()
	{
		/*
		 * панель "Сбытовые компании"
		 */
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		/*
		 * панель поиска
		 */
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout(5, 0));
		panel.add(searchPanel, BorderLayout.NORTH);

		/*
		 * надпись строки поиска
		 */
		searchPanel.add(new JLabel("Строка поиска:"), BorderLayout.WEST);

		/*
		 * текстовое поле для ввода данных поиска
		 */
		final JTextField jSearchTextField = new JTextField();
		searchPanel.add(jSearchTextField);
		/*
		 * событие текстового поля, обновление sql запроса таблицы сбыт комп
		 */
		jSearchTextField.getDocument().addDocumentListener(new DocumentListener()
		{

			public void changedUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void removeUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void insertUpdate(DocumentEvent e)
			{
				updateSearchString();
			}

			public void updateSearchString()
			{
				// Обнуляем запрос поиска
				sqlsearch_sbut = " ";
				// разбиваем поиск на слова

				String[] result = jSearchTextField.getText().split(" ");
				// проверяем на пустую строку
				if (result.length > 0)
				{
					// если не пустая, создаём запрос
					sqlsearch_sbut += " where  search like '%" + result[0].toLowerCase() + "%' ";
					// и если не 1 параметр запроса
					for (int i = 1; i < result.length; i++)
					{
						// то добавляем ост параметры к запросу
						sqlsearch_sbut += " and search like '%" + result[i].toLowerCase() + "%' ";
					}
				}
				// обновялем данные в таблице
				refreshTableSbut();

				validate();
			}
		});

		/*
		 * панель кнопок
		 * 1. добавление записей
		 * 2. просмотр занесённых организаций
		 */
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		panel.add(buttonsPanel, BorderLayout.SOUTH);

		/*
		 * добавление записей
		 */
		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(new BUTTON_addSbut());
		buttonsPanel.add(addButton, null);

		/*
		 * просмотр занесённых организаций
		 */
		JButton svodButton = new JButton("Организации");
		svodButton.addActionListener(new BUTTON_reviewSbut());
		buttonsPanel.add(svodButton, null);

		/*
		 * панель таблицы
		 */
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		panel.add(tablePanel, BorderLayout.CENTER);

		/*
		 * таблица с данными
		 */
		jDataTable_sbut = new JTable()
		{
			/*
			 * Запрет на редактирование ячеек
			 */
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		/*
		 * добавляем скроллбар
		 */
		tablePanel.add(new JScrollPane(jDataTable_sbut), null);
		// Открытие подробной информации о записи
		// двойной клик мыши по строчке
		jDataTable_sbut.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				// ждём 2 кликов
				if (e.getClickCount() == 2)
				{
					// пользователь сделал 2 клика

					// получаем инф о выбранной таблице
					JTable target = (JTable) e.getSource();

					// создание формы
					JPanel mainPanel = new JPanel();
					mainPanel.setLayout(new BorderLayout(5, 5));
					mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

					// создание панели кнопок
					JPanel buttonsPanel = new JPanel();
					buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
					mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

					/*
					 * Кнопка "назад"
					 */
					JButton preButton = new JButton("Назад");
					preButton.setFocusable(false);
					buttonsPanel.add(preButton);
					preButton.addActionListener(new ActionListener()
					{
						/*
						 * Очишает форму и возврашает к основной вкладке
						 * (non-Javadoc)
						 * @see
						 * java.awt.event.ActionListener#actionPerformed(java.awt
						 * .event.ActionEvent)
						 */
						public void actionPerformed(ActionEvent e)
						{
							getContentPane().removeAll();
							getContentPane().add(mainPanel());
							tab.setSelectedIndex(1);
							validate();
						}
					});

					// Создание панели вкладок
					JTabbedPane tabbedPane = new JTabbedPane();
					tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));
					// доавбление панелей во вкладки
					tabbedPane.addTab("Титульник", getTitlePanelSbut(jDataTable_sbut.getValueAt(target.getSelectedRow(), 0).toString()));
					// tabbedPane.addTab("Отпуск ЭЭ сет организациями",
					// getOtpuskPanel(jDataTable_sbut.getValueAt(target.getSelectedRow(),
					// 0).toString()));

					mainPanel.add(tabbedPane);
					getContentPane().removeAll();
					// добавление всех элементов на форму
					getContentPane().add(mainPanel);
					validate();
				}
			}
		});

		// Реализация PopUp Menu
		jDataTable_sbut.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent Me)
			{
				if (0 < jDataTable_sbut.getSelectedRows().length && Me.isMetaDown())
				{
					JPopupMenu Pmenu = new JPopupMenu();

					// количество выделенных записей
					// для удобства пользователей
					JMenuItem numberRecords = new JMenuItem("Выделено: " + jDataTable_sbut.getSelectedRows().length);
					Pmenu.add(numberRecords);

					if (jDataTable_sbut.getSelectedRows().length > 0 && jDataTable_sbut.getSelectedRows().length < 2)
					{
						final String year = (String) jDataTable_sbut.getValueAt(jDataTable_sbut.getSelectedRows()[0], 1);

						JMenuItem svod = new JMenuItem("Создать свод за: " + year);
						Pmenu.add(svod);

						svod.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								//new ToExcelSetev(year);

								Vector<String> name = new ConnectionBD().getNameSbut();

								for (int i = 0; i < name.size(); i = i + 2)
								{
									new ToExcelSbut(name.get(i), name.get(i + 1), year);
								}

								JOptionPane.showMessageDialog(null, "Сбытовые готовы");
							}
						});
					}

					// удаляем выделенные элементы
					JMenuItem delRecords = new JMenuItem("Удалить:" + jDataTable_sbut.getSelectedRows().length);
					Pmenu.add(delRecords);

					// показываем PopUp меню
					Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());

					// удаление записей
					delRecords.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							// Сообщение

							// варианты ответа пользователя
							String[] choices = { "Да", "Нет" };

							// создание сообщения
							int response = JOptionPane.showOptionDialog(null // В
																				// центре
																				// окна
							, "Вы уверены, что хотите удалить " + jDataTable_sbut.getSelectedRows().length + " элементов?" // Сообщение
							, "" // Титульник сообщения
							, JOptionPane.YES_NO_OPTION // Option type
							, JOptionPane.PLAIN_MESSAGE // messageType
							, null // Icon (none)
							, choices // Button text as above.
							, "" // Default button's labelF
							);

							// обработка ответа пользователя
							switch (response)
							{
								case 0:
									// удаление
									for (int i = 0; i < jDataTable_sbut.getSelectedRows().length; i++)
									{
										new ConnectionBD().deleteRowSbut(jDataTable_sbut.getValueAt(jDataTable_sbut.getSelectedRows()[i], 0).toString());
									}
									// обновляем таблицу
									refreshTableSbut();
									break;
								case 1:
									// ничего не удаляем
									break;
								case -1:
									// окно было закрыто - ничего не удаляем
								default:
									break;
							}

						}
					});
				}
			}
		});

		refreshTableSbut();
		return panel;
	}

	/**
	 * Обновляет данные в таблице Сбытовые компании
	 */
	private void refreshTableSbut()
	{
		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getSbutSearch(sqlsearch_sbut);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("id");
		header.add("Год");
		header.add("Месяц");
		header.add("Наименование");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jDataTable_sbut.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);
		// задаем ширину каждого столбца, кроме наименования
		// id
		jDataTable_sbut.getColumnModel().getColumn(0).setMaxWidth(40);
		// год
		jDataTable_sbut.getColumnModel().getColumn(1).setMaxWidth(40);
		// месяц
		jDataTable_sbut.getColumnModel().getColumn(2).setMaxWidth(80);
		// Под название организации отводится всё оставшиеся пространство
	}

	/**
	 * Кнопка, приводит к сценарию добавления Сбытовых комп.
	 * 
	 * @author kirill
	 * 
	 */
	public class BUTTON_addSbut implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			/*
			 * диалоговое окно
			 * фильтр установлен на ПАПКИ
			 */
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			/*
			 * с помошью returnValue определим отклик пользователя
			 * была отмена или выбрана директория
			 */
			int returnValue = fileChooser.showOpenDialog(new JLabel());

			/*
			 * выбранная директория
			 */
			File selectedFile = fileChooser.getSelectedFile();

			/*
			 * далее только с правильной директорией
			 */
			if (returnValue != JFileChooser.CANCEL_OPTION)
			{
				final DefaultListModel listNames = new DefaultListModel();
				final DefaultListModel listPaths = new DefaultListModel();

				/*
				 * если директория существует => получаем список
				 * excel файлов(.xls)
				 * listNames - название файлов
				 * listPaths - пути к файлам
				 * listNames[i] <=> listPaths[i]
				 */
				if (selectedFile != null)
				{
					Listener listener = new Listener(selectedFile.getAbsolutePath());

					for (int i = 0; i < listener.getListNames().size(); i++)
					{
						/*
						 * количество listNames совпадает с количеством
						 * listPaths
						 */
						listNames.addElement(listener.getListNames().get(i));
						listPaths.addElement(listener.getListPaths().get(i));
					}
				}

				/*
				 * создание панели
				 */
				JPanel panel = new JPanel();
				panel.setLayout(new BorderLayout(5, 5));
				panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

				/*
				 * панель вывода списка
				 */
				JPanel listPanel = new JPanel();
				listPanel.setLayout(new BorderLayout(5, 5));
				listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				panel.add(listPanel, BorderLayout.CENTER);

				/*
				 * прогресс бар
				 * показывает количество внесённых в бд записей
				 */
				final JProgressBar jProgressbar = new JProgressBar();
				listPanel.add(jProgressbar, BorderLayout.SOUTH);

				/*
				 * компонент списка со скроллом
				 */
				final JList list = new JList(listNames);
				listPanel.add(new JScrollPane(list));

				/*
				 * панель кнопок
				 */
				JPanel buttonsPanel = new JPanel();
				buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
				buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
				panel.add(buttonsPanel, BorderLayout.SOUTH);

				/*
				 * Кнопка "Добавить"
				 */
				JButton folderButton = new JButton("Добавить");
				folderButton.setFocusable(false);
				buttonsPanel.add(folderButton);
				folderButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{/*
					 * диалоговое окно
					 * фильтр установлен на ПАПКИ
					 */
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						/*
						 * с помошью returnValue определим отклик пользователя
						 * была отмена или выбрана директория
						 */
						int returnValue = fileChooser.showOpenDialog(new JLabel());

						/*
						 * выбранная директория
						 */
						File selectedFile = fileChooser.getSelectedFile();

						/*
						 * далее только с правильной директорией
						 */
						if (returnValue != JFileChooser.CANCEL_OPTION)
						{
							if (selectedFile != null)
							{
								Listener listener = new Listener(selectedFile.getAbsolutePath());

								/*
								 * добавление элементов к предыдушему списку
								 */
								for (int i = 0; i < listener.getListNames().getSize(); i++)
								{
									/*
									 * listNames - название файлов
									 * listPaths - пути к файлам
									 * listNames[i] <=> listPaths[i]
									 */
									listNames.addElement(listener.getListNames().getElementAt(i));
									listPaths.addElement(listener.getListPaths().getElementAt(i));
								}
							}

							validate();
						}
					}
				});

				/*
				 * Кнопка "Внести"
				 */
				JButton addButton = new JButton("Внести");
				addButton.setFocusable(false);
				buttonsPanel.add(addButton);
				addButton.addActionListener(new ActionListener()
				{
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e)
					{
						main.disable();

						jProgressbar.setMaximum(listPaths.getSize());
						jProgressbar.setMinimum(0);
						jProgressbar.setValue(0);

						AddTableSbut thread = new AddTableSbut();

						thread.setJProgressBar(jProgressbar);
						thread.setMain(main);

						thread.setListPaths(listPaths);
						thread.setListNames(listNames);

						// запускаем поток
						thread.execute();
					}
				});

				/*
				 * Кнопка "удалить из списка"
				 */
				JButton removeButton = new JButton("Удалить из списка");
				removeButton.setFocusable(false);
				buttonsPanel.add(removeButton);
				removeButton.addActionListener(new ActionListener()
				{
					/*
					 * Удаление из списка
					 * (non-Javadoc)
					 * @see
					 * java.awt.event.ActionListener#actionPerformed(java.awt
					 * .event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e)
					{
						/*
						 * Если не удалять, то всегда будет возвращаться true!
						 */
						while (list.isSelectedIndex(list.getSelectedIndex()))
						{
							/*
							 * 1. удаляем элемент из списка адресса файла
							 * 2. удаляем элемент из списка имен файла
							 */
							listPaths.removeElementAt(list.getSelectedIndex());
							listNames.removeElementAt(list.getSelectedIndex());
						}
					}
				});

				/*
				 * Кнопка "назад"
				 */
				JButton preButton = new JButton("Назад");
				preButton.setFocusable(false);
				buttonsPanel.add(preButton);
				preButton.addActionListener(new ActionListener()
				{
					/*
					 * Очишает форму и возврашает к основной вкладке
					 * (non-Javadoc)
					 * @see
					 * java.awt.event.ActionListener#actionPerformed(java.awt
					 * .event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e)
					{
						getContentPane().removeAll();
						getContentPane().add(mainPanel());
						tab.setSelectedIndex(1);
						validate();
					}
				});

				getContentPane().removeAll();
				// добавление всех элементов на форму
				getContentPane().add(panel);
				// обновление формы
				validate();
			}
		}
	}

	/**
	 * Кнопка, "Просмотр организаций" показывает сбытовые комп, которые подали
	 * отчеты
	 * 
	 * @author kirill
	 * 
	 */
	public class BUTTON_reviewSbut implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// создание формы
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout(5, 5));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			// Создание панели вкладок
			JTabbedPane tabbedPane = new JTabbedPane();
			// шрифт вкладок
			tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));

			@SuppressWarnings("rawtypes") Vector years = new ConnectionBD().getYearsSbut();

			for (int i = 0; i < years.size(); i++)
			{
				// добавление панелей во вкладки
				tabbedPane.addTab(years.get(i).toString(), getYearPanelSbut(years.get(i)));
			}

			setSize(WIDTH + 300, HEIGHT);

			getContentPane().removeAll();
			mainPanel.add(tabbedPane);
			// добавление всех элементов на форму
			getContentPane().add(mainPanel);
			validate();
		}
	}

	/**
	 * Панель сетевых организаций за год(создание отд вкладки
	 * 
	 * @param year
	 * @return
	 */
	@SuppressWarnings("serial")
	private JPanel getYearPanelSbut(Object year)
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		// tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.setLayout(new BorderLayout(5, 5));

		// создание панели кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.add(buttonsPanel, BorderLayout.SOUTH);

		// Добавление таблиц
		JButton addButton = new JButton("Сохранить в Excel " + year.toString() + " год.");

		TableToExcelSbut asdsad = new TableToExcelSbut();
		asdsad.setYear(year.toString());
		addButton.addActionListener(asdsad);

		buttonsPanel.add(addButton, null);

		/*
		 * Кнопка "назад"
		 */
		JButton preButton = new JButton("Назад");
		preButton.setFocusable(false);
		buttonsPanel.add(preButton);
		preButton.addActionListener(new ActionListener()
		{
			/*
			 * Очишает форму и возврашает к основной вкладке
			 */
			public void actionPerformed(ActionEvent e)
			{
				getContentPane().removeAll();
				getContentPane().add(mainPanel());

				tab.setSelectedIndex(1);

				setSize(WIDTH, HEIGHT);

				validate();
			}
		});

		JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		// jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), BorderLayout.CENTER);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_YearSbut(year);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Организация");
		header.add("Январь");
		header.add("Февраль");
		header.add("Март");
		header.add("Апрель");
		header.add("Май");
		header.add("Июнь");
		header.add("Июль");
		header.add("Август");
		header.add("Сентябрь");
		header.add("Октябрь");
		header.add("Ноябрь");
		header.add("Декабрь");
		header.add("Год");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// наименование
		jtable.getColumnModel().getColumn(0).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(0).setMinWidth(300);
		// месяцы
		for (int i = 1; i < 14; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(50);
		}

		// дополнительное редактирование ячеек
		jtable.setDefaultRenderer(jtable.getColumnClass(1), new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				if (column < 1)
				{
					// название организации
					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.LEFT);
				}
				else
				{
					// в ячейках месяцев

					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.CENTER);

					// определение содержимого ячейки
					if (value.equals("+"))
					{
						// если содержит знак +
						super.setBackground(Color.GREEN);
					}
					else
					{
						// ничего в ячейке нету
						super.setBackground(Color.WHITE);
					}
				}

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				return this;
			}

		});
		return tablePanel;
	}

	/**
	 * Показывает содержимое титульника записи; Сбытовые компании
	 * 
	 * @param current_id
	 *            - id показываемой записи
	 * @return панель
	 */
	private JPanel getTitlePanelSbut(String current_id)
	{
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(22, 2));

		for (int i = 0; i < 22; i++)
		{
			JPanel labelPanel = new JPanel();
			labelPanel.setLayout(new BorderLayout(5, 0));

			@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getSbutTitle(current_id);

			JTextField textField = new JTextField("");
			textField.setEditable(false);
			textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			// textField.setHorizontalAlignment(JTextField.RIGHT);
			JLabel jLabel = new JLabel("");

			switch (i)
			{
				case 0:
				{
					textField.setText(values.get(3).toString());
					textField.setHorizontalAlignment(JTextField.CENTER);
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					labelPanel.add(textField, null);
					break;
				}
				case 1:
				{
					jLabel.setText("Отчетный период: ");
					textField.setText(values.get(1) + " " + values.get(2));
					break;
				}
				case 2:
				{
					jLabel.setText("Муниципальный район");
					textField.setText(values.get(7).toString());
					break;
				}
				case 3:
				{
					jLabel.setText("Муниципальное образование: ");
					textField.setText(values.get(8).toString());
					break;
				}
				case 4:
				{
					jLabel.setText("ОКТМО: ");
					textField.setText(values.get(9).toString());
					break;
				}
				case 5:
				{
					jLabel.setText("ИНН: ");
					textField.setText(values.get(4).toString());
					break;
				}
				case 6:
				{
					jLabel.setText("КПП: ");
					textField.setText(values.get(5).toString());
					break;
				}
				case 7:
				{
					jLabel.setText("Код по ОКПО: ");
					textField.setText(values.get(6).toString());
					break;
				}
				case 8:
				{
					textField.setText("Адрес организации");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 9:
				{
					jLabel.setText("Юридический адрес: ");
					textField.setText(values.get(10).toString());
					break;
				}
				case 10:
				{
					jLabel.setText("Почтовый адрес: ");
					textField.setText(values.get(11).toString());
					break;
				}
				case 11:
				{
					textField.setText("Руководитель");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 12:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(12).toString());
					break;
				}
				case 13:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(13).toString());
					break;
				}
				case 14:
				{
					textField.setText("Главный бухгалтер");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 15:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(14).toString());
					break;
				}
				case 16:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(15).toString());
					break;
				}
				case 17:
				{
					textField.setText("Должностное лицо, ответственное за составление формы");
					Font font = new Font("", Font.BOLD, 12);
					textField.setFont(font);
					textField.setHorizontalAlignment(JTextField.CENTER);
					break;
				}
				case 18:
				{
					jLabel.setText("Фамилия, имя, отчество: ");
					textField.setText(values.get(16).toString());
					break;
				}
				case 19:
				{
					jLabel.setText("Должность: ");
					textField.setText(values.get(17).toString());
					break;
				}
				case 20:
				{
					jLabel.setText("Контактный телефон: ");
					textField.setText(values.get(18).toString());
					break;
				}
				case 21:
				{
					jLabel.setText("e-mail: ");
					textField.setText(values.get(19).toString());
					break;
				}
			}
			labelPanel.add(jLabel, BorderLayout.WEST);
			labelPanel.add(textField, null);
			titlePanel.add(labelPanel, null);
		}
		return titlePanel;
	}

	/**
	 * Кнопка "Запись списка Сбытовые комп в ексель"
	 * 
	 * @author kirill
	 * 
	 */
	private class TableToExcelSbut implements ActionListener
	{
		String	year	= null;

		public void actionPerformed(ActionEvent e)
		{
			if (year != null)
			{
				new ToExcelKOyearSbut(year);
			}
		}

		public void setYear(String year)
		{
			this.year = year;
		}
	}
}
