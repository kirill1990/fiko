package ru.fiko.purchase;


import ru.fiko.purchase.supports.ComboItemBooleanValue;
import ru.fiko.purchase.supports.ComboItemIntValue;

public class Constant {

    /**
     * Путь к распложению базы данных
     */
    public static String PATHTODB = "purchase.db";
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

    public static Object[] year_items = {
	    new ComboItemIntValue(2013, "2013     "),
	    new ComboItemIntValue(2014, "2014     "),
	    new ComboItemIntValue(2015, "2015     "),
	    new ComboItemIntValue(2016, "2016     ") };

    public static Object[] month_items = {
	    new ComboItemIntValue(0, "Январь"),
	    new ComboItemIntValue(1, "Февраль"),
	    new ComboItemIntValue(2, "Март"),
	    new ComboItemIntValue(3, "Апрель"),
	    new ComboItemIntValue(4, "Май"),
	    new ComboItemIntValue(5, "Июнь"),
	    new ComboItemIntValue(6, "Июль"),
	    new ComboItemIntValue(7, "Август"),
	    new ComboItemIntValue(8, "Сентябрь"),
	    new ComboItemIntValue(9, "Октябрь"),
	    new ComboItemIntValue(10, "Ноябрь"),
	    new ComboItemIntValue(11, "Декабрь") };

}
