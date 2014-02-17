package output;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import basedata.ConnectionBD;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ToExcelSbut {
    WritableCellFormat tahoma9pt = null;
    WritableCellFormat tahoma9ptGreen = null;
    WritableCellFormat tahoma9ptRed = null;
    WritableCellFormat tahoma9ptORANGE = null;
    WritableCellFormat tahoma9ptYellow = null;
    WritableCellFormat tahoma9ptLeft = null;
    WritableCellFormat tahoma12ptNoBold = null;
    WritableCellFormat tahoma12ptBold = null;
    WritableCellFormat tahoma9ptLeftBoldGray = null;

    String year = "2012";
    public static String[] months = {
	    "январь",
	    "февраль",
	    "март",
	    "апрель",
	    "май",
	    "июнь",
	    "июль",
	    "август",
	    "сентябрь",
	    "октябрь",
	    "ноябрь",
	    "декабрь",
	    "год",
	    "итог" };

    String[][] mOtpusk_1 = {
	    {
		    "Потребители с максимальной мощностью принадлежащих им энергопринимающих устройств от 10 МВт",
		    "100" },
	    { "Промышленные и приравненные к ним потребители", "111" },
	    { "Электрифицированный железнодорожный транспорт", "121" },
	    { "Электрифицированный городской транспорт", "131" },
	    { "Непромышленные потребители", "141" },
	    { "Сельскохозяйственные товаропроизводители", "151" },
	    { "Бюджетные потребители", "161" },
	    { "Другие энергоснабжающие организации", "171" },
	    {
		    "Потребители с максимальной мощностью принадлежащих им энергопринимающих устройств от 670 кВт до 10 МВт",
		    "200" },
	    { "Промышленные и приравненные к ним потребители", "211" },
	    { "Электрифицированный железнодорожный транспорт", "221" },
	    { "Электрифицированный городской транспорт", "231" },
	    { "Непромышленные потребители", "241" },
	    { "Сельскохозяйственные товаропроизводители", "251" },
	    { "Бюджетные потребители", "261" },
	    { "Другие энергоснабжающие организации", "271" },
	    {
		    "Потребители с максимальной мощностью принадлежащих им энергопринимающих устройств от 150 кВт до 670 кВт",
		    "300" },
	    { "Промышленные и приравненные к ним потребители", "311" },
	    { "Электрифицированный железнодорожный транспорт", "321" },
	    { "Электрифицированный городской транспорт", "331" },
	    { "Непромышленные потребители", "341" },
	    { "Сельскохозяйственные товаропроизводители", "351" },
	    { "Бюджетные потребители", "361" },
	    { "Другие энергоснабжающие организации", "371" },
	    {
		    "Потребители с максимальной мощностью принадлежащих им энергопринимающих устройств до 150 кВт",
		    "400" },
	    { "Промышленные и приравненные к ним потребители", "411" },
	    { "Электрифицированный железнодорожный транспорт", "421" },
	    { "Электрифицированный городской транспорт", "431" },
	    { "Непромышленные потребители", "441" },
	    { "Сельскохозяйственные товаропроизводители", "451" },
	    { "Бюджетные потребители", "461" },
	    { "Другие энергоснабжающие организации", "471" },
	    {
		    "Компенсация расхода электрической энергии на передачу сетевыми организациями",
		    "500" },
	    { "Полезный отпуск - всего", "600" } };

    String[][] mOtpusk_3 = {
	    { "Население, всего", "100" },
	    { "в пределах социальной нормы", "110" },
	    { "сверх социальной нормы", "120" },
	    {
		    "Население, проживающее в городских населенных пунктах в домах, не оборудованных в установленном порядке стационарными электроплитами и (или) электроотопительными установками",
		    "200" },
	    { "в пределах социальной нормы", "210" },
	    { "сверх социальной нормы", "220" },
	    {
		    "Население, проживающее в городских населенных пунктах в домах, оборудованных в установленном порядке стационарными электроплитами",
		    "230" },
	    { "в пределах социальной нормы", "240" },
	    { "сверх социальной нормы", "250" },
	    {
		    "Население, проживающее в городских населенных пунктах в домах, оборудованных в установленном порядке стационарными электроотопительными установками",
		    "260" },
	    { "в пределах социальной нормы", " 270" },
	    { "сверх социальной нормы", " 280" },
	    {
		    "Население, проживающее в городских населенных пунктах в домах, оборудованных в установленном порядке стационарными электроплитами и электроотопительными установками",
		    "290" },
	    { "в пределах социальной нормы", " 300" },
	    { "сверх социальной нормы", "310" },
	    { "Население, проживающее в сельских населенных пунктах", "320" },
	    { "в пределах социальной нормы", "330" },
	    { "сверх социальной нормы", "340" },
	    { "Потребители, приравненные к населению, всего", "400" },
	    { "в пределах социальной нормы", "410" },
	    { "сверх социальной нормы", "420" },
	    { "в том числе:", "430" },
	    { "Исполнители коммунальных услуг", "440" },
	    { "в пределах социальной нормы", "450" },
	    { "сверх социальной нормы", "460" },
	    {
		    "Садоводческие, огороднические или дачные некоммерческие объединения граждан",
		    "470" },
	    { "в пределах социальной нормы", "480" },
	    { "сверх социальной нормы", "490" },
	    { "Религиозные организации", "500" },
	    { "в пределах социальной нормы", "510" },
	    { "сверх социальной нормы", "520" },
	    {
		    "Бюджетные организации (проживание военнослужащих, содержание осужденных)",
		    "530" },
	    { "в пределах социальной нормы", "540" },
	    { "сверх социальной нормы", "550" },
	    {
		    "Некоммерческие объединения граждан  (гаражно-строительные, гаражные кооперативы)",
		    "560" },
	    { "в пределах социальной нормы", "570" },
	    { "сверх социальной нормы", "580" },
	    { "Хозяйственные постройки физических лиц", "590" },
	    { "в пределах социальной нормы", "600" },
	    { "сверх социальной нормы", "610" },
	    {
		    "Гарантирующие поставщики, энергосбытовые, энергоснабжающие организации, приобретающие электрическую энергию (мощность) в целях дальнейшей продажи населению",
		    "700" },
	    { "в пределах социальной нормы", "710" },
	    { "сверх социальной нормы", "720" }

    };

    String[][] mOtpusk_6 = {
	    { "Продажа", "300" },
	    { "В обеспечение СД", "301" },
	    { "В обеспечение регулируемых договоров (РД)", "302" },
	    { "В обеспечение биржевых СДМ", "303" },
	    { "В обеспечение внебиржевых СДМ", "304" },
	    { "По договорам предоставления мощности ДПМ", "305" },
	    { "По ценам РСВ", "306" },
	    { "БР", "307" },
	    { "Экспортно-импортная и приграничная торговля", "308" },
	    { "По результатам КОМ", "309" },
	    { "На оптовом рынке по регулируемым ценам", "310" },
	    { "На оптовом рынке по нерегулируемым ценам", "311" },
	    { "На розничном рынке по регулируемым тарифам (ценам)", "312" },
	    { "На розничном рынке по свободным (нерегулируемым) ценам", "313" },
	    { "Собственное производство", "400" },
	    { "Мощность, заявленная на КОМ", "500" },
	    { "Аттестованная мощность", "600" },
	    { "Штрафные санкции ЦФР", "700" } };

    String[][] mOtpusk_7 = {
	    { "Покупка", "100" },
	    { "В обеспечение СД", "101" },
	    { "В обеспечение регулируемых договоров (РД)", "102" },
	    { "В обеспечение биржевых СДМ", "103" },
	    { "В обеспечение внебиржевых СДМ", "104" },
	    { "По договорам предоставления мощности ДПМ", "105" },
	    { "По ценам РСВ", "106" },
	    { "БР", "107" },
	    { "Экспортно-импортная и приграничная торговля", "108" },
	    { "По результатам КОМ", "109" },
	    { "На оптовом рынке по регулируемым ценам", "110" },
	    { "На оптовом рынке по нерегулируемым ценам", "111" },
	    { "На розничном рынке по регулируемым тарифам (ценам)", "112" },
	    { "На розничном рынке по свободным (нерегулируемым) ценам", "113" },
	    { "Итого покупка с учетом продажи", "114" },
	    { "Собственное потребление", "200" } };

    String[] arg = { "Всего", "ВН", "СН1", "СН2", "НН", "ФСК", "ГН" };
    String[] arg2 = { "Всего", "Прочие", "ФСК", "ГН" };

    String[] o1 = {
	    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей, осуществляющих оплату по трехставочным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по трехставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической мощности потребителей, осуществляющих оплату услуг по передаче электрической энергии по трехставочным ценам за отчетный месяц (год), МВт",
	    "Стоимость электрической мощности потребителей, осуществляющих оплату услуг по передаче электрической энергии по трехставочным ценам за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость электрической энергии (мощности) без учета стоимости отклонений за отчетный месяц (год) без НДС, по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость отклонений фактических объемов потребления электрической энергии от плановых (договорных) значений за отчетный  месяц (год) без НДС, тыс руб" };

    String[] o2 = {
	    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей, осуществляющих оплату по трехставочным тарифам (ценам) за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по трехставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость электрической энергии (мощности) без учета стоимости отклонений за отчетный месяц (год) без НДС, по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость отклонений фактических объемов потребления электрической энергии от плановых (договорных) значений за отчетный месяц (год) без НДС, тыс руб" };

    String[] o3 = {
	    "Объем электрической энергии за отчетный месяц (год), тыс кВт ч всего",
	    "Стоимость электрической энергии за отчетный месяц (год) с НДС, тыс руб всего",
	    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс руб всего",
	    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год), тыс кВт ч всего",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) с НДС, тыс руб всего",
	    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) без НДС,  тыс руб всего" };

    String[] o4 = {
	    "Объем электрической энергии (мощности) потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии (мощности) потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии (мощности) потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии (мощности) потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической мощности за отчетный месяц (год), МВт",
	    "Стоимость электрической мощности за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической мощности за отчетный месяц (год), МВт",
	    "Стоимость электрической мощности за отчетный месяц (год) без НДС, тыс руб",
	    "Объем мощности услуг по передаче электроэнергии потребителей за отчетный месяц (год), МВт",
	    "Стоимость мощности услуг по передаче электроэнергии потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость электрической энергии (мощности) по 3-6 ценовой категории без учета стоимости отклонений за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость отклонений фактических объемов потребления электрической энергии по 5 и 6 ценовой категории от плановых (договорных) значений за отчетный месяц (год) без НДС, тыс руб" };

    String[] o5 = {
	    "Объем электрической энергии (мощности) потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии (мощности) потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии (мощности) потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии (мощности) потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии потребителей за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии потребителей за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической мощности за отчетный месяц (год), МВт",
	    "Стоимость электрической мощности за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической энергии за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс руб",
	    "Объем электрической мощности за отчетный месяц (год), МВт",
	    "Стоимость электрической мощности за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость электрической энергии по 3-6 ценовой категории без учета стоимости отклонений за отчетный месяц (год) без НДС, тыс руб",
	    "Стоимость отклонений фактических объемов потребления электрической энергии по 5 и 6 ценовой категории от плановых (договорных) значений за отчетный месяц (год) без НДС, тыс руб" };

    String[] o6 = {
	    "Объем электрической энергии за отчетный месяц (год), тыс кВт ч",
	    "Стоимость электрической энергии за отчетный месяц (год), тыс руб",
	    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
	    "Стоимость электрической мощности за отчетный месяц (год), тыс руб",
	    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс руб" };

    private ConnectionBD connect;

    public ToExcelSbut(String name, String inn, String year) {
	WorkbookSettings ws = new WorkbookSettings();
	ws.setLocale(new Locale("ru", "RU"));

	this.year = year;

	try {
	    /*
	     * Основной формат ячеек Tahoma 9pt, no bold выравнивание по
	     * горизонтале: центр выравнивание по вертикале: центр перенос по
	     * словам стиль границы - все цвет фона - без цвета
	     */
	    tahoma9pt = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9pt.setAlignment(Alignment.CENTRE);
	    tahoma9pt.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9pt.setWrap(true);
	    tahoma9pt.setBorder(Border.ALL, BorderLineStyle.THIN);

	    /*
	     * формат ячеек зелёного цвета Tahoma 9pt, no bold выравнивание по
	     * горизонтале: по правому краю выравнивание по вертикале: центр
	     * перенос по словам стиль границы - все цвет фона - легкий зелёный
	     */
	    tahoma9ptGreen = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9ptGreen.setAlignment(Alignment.RIGHT);
	    tahoma9ptGreen.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptGreen.setWrap(true);
	    tahoma9ptGreen.setBorder(Border.ALL, BorderLineStyle.THIN);
	    tahoma9ptGreen.setBackground(Colour.LIGHT_GREEN);

	    tahoma9ptRed = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9ptRed.setAlignment(Alignment.RIGHT);
	    tahoma9ptRed.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptRed.setWrap(true);
	    tahoma9ptRed.setBorder(Border.ALL, BorderLineStyle.THIN);
	    tahoma9ptRed.setBackground(Colour.RED);

	    tahoma9ptORANGE = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9ptORANGE.setAlignment(Alignment.RIGHT);
	    tahoma9ptORANGE.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptORANGE.setWrap(true);
	    tahoma9ptORANGE.setBorder(Border.ALL, BorderLineStyle.THIN);
	    tahoma9ptORANGE.setBackground(Colour.LIGHT_ORANGE);

	    /*
	     * формат ячеек жёлтого цвета Tahoma 9pt, no bold выравнивание по
	     * горизонтале: по правому краю выравнивание по вертикале: центр
	     * перенос по словам стиль границы - все цвет фона - легкий жёлтый
	     */
	    tahoma9ptYellow = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9ptYellow.setAlignment(Alignment.RIGHT);
	    tahoma9ptYellow.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptYellow.setWrap(true);
	    tahoma9ptYellow.setBorder(Border.ALL, BorderLineStyle.THIN);
	    tahoma9ptYellow.setBackground(Colour.VERY_LIGHT_YELLOW);

	    /*
	     * Основной с выравниванием по левому краю Tahoma 9pt, no bold
	     * выравнивание по горизонтале: по левому краю выравнивание по
	     * вертикале: центр перенос по словам стиль границы: все цвет фона:
	     * без цвета
	     */
	    tahoma9ptLeft = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
	    tahoma9ptLeft.setAlignment(Alignment.LEFT);
	    tahoma9ptLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptLeft.setWrap(true);
	    tahoma9ptLeft.setBorder(Border.ALL, BorderLineStyle.THIN);

	    /*
	     * Основной с выравниванием по центру без рамки Tahoma 9pt, no bold
	     * выравнивание по горизонтале: центр выравнивание по вертикале:
	     * центр перенос по словам стиль границы: без рамки цвет фона: без
	     * цвета
	     */
	    tahoma12ptNoBold = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 12, WritableFont.NO_BOLD));
	    tahoma12ptNoBold.setAlignment(Alignment.CENTRE);
	    tahoma12ptNoBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma12ptNoBold.setWrap(true);
	    tahoma12ptNoBold.setBorder(null, null);

	    /*
	     * Основной с выравниванием по центру без рамки Tahoma 9pt, no bold
	     * выравнивание по горизонтале: центр выравнивание по вертикале:
	     * центр перенос по словам стиль границы: без рамки цвет фона: без
	     * цвета
	     */
	    tahoma12ptBold = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 12, WritableFont.BOLD));
	    tahoma12ptBold.setAlignment(Alignment.CENTRE);
	    tahoma12ptBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma12ptBold.setWrap(true);
	    tahoma12ptBold.setBorder(Border.ALL, BorderLineStyle.THIN);

	    /*
	     * Основной жирный c серым оттенком, по левому краю Tahoma 9pt, bold
	     * выравнивание по горизонтале: по левому краю выравнивание по
	     * вертикале: центр перенос по словам стиль границы: все цвет фона:
	     * 25% серого
	     */
	    tahoma9ptLeftBoldGray = new WritableCellFormat(new WritableFont(
		    WritableFont.TAHOMA, 9, WritableFont.BOLD));
	    tahoma9ptLeftBoldGray.setAlignment(Alignment.LEFT);
	    tahoma9ptLeftBoldGray
		    .setVerticalAlignment(VerticalAlignment.CENTRE);
	    tahoma9ptLeftBoldGray.setWrap(true);
	    tahoma9ptLeftBoldGray.setBorder(Border.ALL, BorderLineStyle.THIN);
	    tahoma9ptLeftBoldGray.setBackground(Colour.GRAY_25);

	    String fileName = inn;

	    if (inn.equals("4632116134"))
		fileName = "ГРИНН Энергосбыт";

	    if (inn.equals("4028033356"))
		fileName = "Каскад Энергосбыт";

	    if (inn.equals("4029030252"))
		fileName = "КСК";

	    if (inn.equals("7704181109"))
		fileName = "МАРЭМ";

	    if (inn.equals("4029027570"))
		fileName = "Облэнергосбыт";

	    if (inn.equals("7704731218"))
		fileName = "Оборонэнергосбыт";

	    if (inn.equals("7706284124"))
		fileName = "Русэнергосбыт";

	    if (inn.equals("4633017746"))
		fileName = "Региональная энергосбытовая комп";

	    if (inn.equals("6829012680"))
		fileName = "КВАДРА";

	    if (inn.equals("7736520080"))
		fileName = "Мосэнергосбыт";

	    String dt = new SimpleDateFormat("dd.MM.yy").format(Calendar
		    .getInstance().getTime());

	    String dir = "Сбытовые комп. - структура факт. сети  " + this.year
		    + "(" + dt + ")";

	    new File(dir).mkdirs();
	    // создание книги
	    WritableWorkbook workbook = Workbook.createWorkbook(new File(dir
		    + "/" + fileName + ".xls"), ws);

	    connect = new ConnectionBD();

	    otpusk_1(workbook.createSheet("Раздел I. А", 0), inn);
	    otpusk_2(workbook.createSheet("Раздел I. Б", 1), inn);
	    otpusk_3(workbook.createSheet("Раздел I. В", 2), inn);
	    otpusk_4(workbook.createSheet("Раздел II. А", 3), inn);
	    otpusk_5(workbook.createSheet("Раздел II. Б", 4), inn);
	    otpusk_6(workbook.createSheet("Раздел III", 5), inn);
	    otpusk_7(workbook.createSheet("Раздел IV", 6), inn);
	    // otpusk2(workbook.createSheet("Отпуск ээ по рег тар (насел)", 1),
	    // name);
	    // otpusk3(workbook.createSheet("Отпуск мощности по рег тар", 2),
	    // name);
	    // otpusk4(workbook.createSheet("Отпуск ээ по нерег ценам", 3),
	    // name);
	    // otpusk5(workbook.createSheet("Отпуск мощности по нерег ценам",
	    // 4),
	    // name);
	    // otpusk6(workbook.createSheet("Продажа", 5), name);
	    // otpusk7(workbook.createSheet("Покупка", 6), name);

	    connect.close();
	    // закрываем книгу
	    workbook.write();
	    workbook.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (WriteException e) {
	    e.printStackTrace();
	}
    }

    private void otpusk_1(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел I. Полезный отпуск электроэнергии и мощности, реализуемой по регулируемым тарифам (ценам)";
	int countRows = 40;
	int countLines = 14;

	int countTables = 12;
	int countColInTable = 7;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut(inn,
		year, 0, 11);

	sbut_otpusk(title, re, arg1, sheet, inn, o1);
    }

    private void otpusk_2(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел I. Полезный отпуск электроэнергии и мощности, реализуемой по регулируемым тарифам (ценам)";
	int countRows = 40;
	int countLines = 14;

	int countTables = 10;
	int countColInTable = 7;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut(inn,
		year, 12, 21);

	sbut_otpusk(title, re, arg1, sheet, inn, o2);
    }

    private void otpusk_3(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел I. Полезный отпуск электроэнергии и мощности, реализуемой по регулируемым тарифам (ценам)";
	int countRows = 50;
	int countLines = 14;

	int countTables = 1;
	int countColInTable = 15;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut_nas(
		inn, year, 0, 0);

	sbut_nas(title, re, arg1, sheet, inn, o3);
    }

    private void otpusk_4(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел II. Полезный отпуск электроэнергии и мощности, реализуемой по нерегулируемым ценам";
	int countRows = 40;
	int countLines = 14;

	int countTables = 16;
	int countColInTable = 7;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut(inn,
		year, 22, 37);

	sbut_otpusk(title, re, arg1, sheet, inn, o4);
    }

    private void otpusk_5(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел II. Полезный отпуск электроэнергии и мощности, реализуемой по нерегулируемым ценам";
	int countRows = 40;
	int countLines = 14;

	int countTables = 14;
	int countColInTable = 4;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut_2(inn,
		year, 0, 13);

	sbut_otpusk2(title, re, arg1, sheet, inn, o5);
    }

    private void otpusk_6(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел III. Продажа электрической энергии и мощности";
	int countRows = 25;
	int countLines = 14;

	int countTables = 1;
	int countColInTable = 5;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut_sb(inn,
		year, 0, 0, "sell");

	sbut_sell_buy(title, re, arg1, sheet, inn, o6, "sell");
    }
    
    private void otpusk_7(WritableSheet sheet, String inn)
	    throws RowsExceededException, WriteException {

	String title = "Раздел III. Покупка электрической энергии и мощности";
	int countRows = 25;
	int countLines = 14;

	int countTables = 1;
	int countColInTable = 5;

	int startRow = 0;
	int startCol = 0;

	int[] arg1 = {
		countRows,
		countLines,
		countTables,
		countColInTable,
		startRow,
		startCol };

	Vector<Vector<Vector<Vector<String>>>> re = connect.getInfoSbut_sb(inn,
		year, 0, 0, "buy");

	sbut_sell_buy(title, re, arg1, sheet, inn, o6, "buy");
    }

    private void sbut_otpusk(String title,
	    Vector<Vector<Vector<Vector<String>>>> re, int[] arg1,
	    WritableSheet sheet, String inn, String[] ot)
	    throws RowsExceededException, WriteException {

	int countRows = arg1[0];
	int countLines = arg1[1];

	int countTables = arg1[2];
	int countColInTable = arg1[3];

	int startRow = arg1[4];
	int startCol = arg1[5];

	int titleCol = startCol;
	int titleRow = startRow + 1;

	sheet.addCell(new Label(titleCol, titleRow, title, tahoma12ptBold));

	sheet.mergeCells(titleCol, titleRow, titleCol + 10, titleRow);
	sheet.setRowView(titleRow, 750);

	for (int p = 3; p < 3 + countRows * countLines; p++)
	    sheet.setRowView(p, 450);

	sheet.setColumnView(0, 75);

	startRow += 3;
	for (int p = startRow - 1; p < startRow + countTables * countColInTable; p++)
	    sheet.setColumnView(p, 15);

	for (int index = 0; index < months.length; index++) {

	    String month = months[index];

	    Vector<Vector<Vector<String>>> result = re.get(index);

	    int dindex = index * countRows;

	    sheet.addCell(new Label(startCol, startRow + dindex, month,
		    tahoma12ptBold));

	    sheet.addCell(new Label(startCol, startRow + 1 + dindex,
		    "Потребители", tahoma9pt));
	    sheet.mergeCells(startCol, startRow + 1 + dindex, startCol,
		    startRow + 2 + dindex);

	    sheet.addCell(new Label(startCol + 1, 3 + dindex, "Код строки",
		    tahoma9pt));
	    sheet.mergeCells(startCol + 1, startRow + dindex, startCol + 1,
		    startRow + 2 + dindex);

	    for (int num = 0; num < mOtpusk_1.length; num++) {
		int column = startCol;
		int row = startRow + 3 + num + dindex;

		sheet.addCell(new Label(column, row, mOtpusk_1[num][0],
			tahoma9ptLeft));
		sheet.addCell(new Label(column + 1, row, mOtpusk_1[num][1],
			tahoma9pt));
	    }

	    for (int table = 0; table < result.size(); table++) {

		int column = startCol + countColInTable * table;

		Vector<Vector<String>> r = result.get(table);

		for (int num = 0; num < r.size(); num++) {
		    int row = startRow + 3 + num + dindex;

		    for (int p = 0; p < r.get(num).size(); p++) {

			WritableCellFormat css = tahoma9ptYellow;
			String value = r.get(num).get(p);

			if (p == 0)
			    css = tahoma9ptORANGE;

			if (month == "итог") {
			    String itsgod = "";

			    try {
				itsgod = re.get(index - 1).get(table).get(num)
					.get(p);
			    } catch (Exception e) {
			    }

			    if (parseStringToDouble(value).doubleValue() != parseStringToDouble(
				    itsgod).doubleValue())
				css = tahoma9ptRed;
			}

			sheet.addCell(new Label(column + 2 + p, row, value, css));
		    }
		}
	    }

	    for (int i = 0; i < ot.length; i++) {
		int column = startCol + 2;
		int row = startRow + dindex;
		int di = i * countColInTable;

		sheet.addCell(new Label(column + di, row, ot[i], tahoma9pt));
		sheet.mergeCells(column + di, row, column + di
			+ countColInTable - 1, row + 1);

		row += 2;
		for (int p = 0; p < arg.length; p++) {
		    int dp = di + p;

		    sheet.addCell(new Label(column + dp, row, arg[p], tahoma9pt));
		}
	    }
	}
    }

    private void sbut_otpusk2(String title,
	    Vector<Vector<Vector<Vector<String>>>> re, int[] arg1,
	    WritableSheet sheet, String inn, String[] ot)
	    throws RowsExceededException, WriteException {

	int countRows = arg1[0];
	int countLines = arg1[1];

	int countTables = arg1[2];
	int countColInTable = arg1[3];

	int startRow = arg1[4];
	int startCol = arg1[5];

	int titleCol = startCol;
	int titleRow = startRow + 1;

	sheet.addCell(new Label(titleCol, titleRow, title, tahoma12ptBold));

	sheet.mergeCells(titleCol, titleRow, titleCol + 10, titleRow);
	sheet.setRowView(titleRow, 750);

	for (int p = 3; p < 3 + countRows * countLines; p++)
	    sheet.setRowView(p, 450);

	sheet.setColumnView(0, 75);

	startRow += 3;
	for (int p = startRow - 1; p < startRow + countTables * countColInTable; p++)
	    sheet.setColumnView(p, 15);

	for (int index = 0; index < months.length; index++) {

	    String month = months[index];

	    Vector<Vector<Vector<String>>> result = re.get(index);

	    int dindex = index * countRows;

	    sheet.addCell(new Label(startCol, startRow + dindex, month,
		    tahoma12ptBold));

	    sheet.addCell(new Label(startCol, startRow + 1 + dindex,
		    "Потребители", tahoma9pt));
	    sheet.mergeCells(startCol, startRow + 1 + dindex, startCol,
		    startRow + 2 + dindex);

	    sheet.addCell(new Label(startCol + 1, 3 + dindex, "Код строки",
		    tahoma9pt));
	    sheet.mergeCells(startCol + 1, startRow + dindex, startCol + 1,
		    startRow + 2 + dindex);

	    for (int num = 0; num < mOtpusk_1.length; num++) {
		int column = startCol;
		int row = startRow + 3 + num + dindex;

		sheet.addCell(new Label(column, row, mOtpusk_1[num][0],
			tahoma9ptLeft));
		sheet.addCell(new Label(column + 1, row, mOtpusk_1[num][1],
			tahoma9pt));
	    }

	    for (int table = 0; table < result.size(); table++) {

		int column = startCol + countColInTable * table;

		Vector<Vector<String>> r = result.get(table);

		for (int num = 0; num < r.size(); num++) {
		    int row = startRow + 3 + num + dindex;

		    for (int p = 0; p < r.get(num).size(); p++) {

			WritableCellFormat css = tahoma9ptYellow;
			String value = r.get(num).get(p);

			if (p == 0)
			    css = tahoma9ptORANGE;

			if (month == "итог") {
			    String itsgod = "";

			    try {
				itsgod = re.get(index - 1).get(table).get(num)
					.get(p);
			    } catch (Exception e) {
			    }

			    if (parseStringToDouble(value).doubleValue() != parseStringToDouble(
				    itsgod).doubleValue())
				css = tahoma9ptRed;
			}

			sheet.addCell(new Label(column + 2 + p, row, value, css));
		    }
		}
	    }

	    for (int i = 0; i < ot.length; i++) {
		int column = startCol + 2;
		int row = startRow + dindex;
		int di = i * countColInTable;

		sheet.addCell(new Label(column + di, row, ot[i], tahoma9pt));
		sheet.mergeCells(column + di, row, column + di
			+ countColInTable - 1, row + 1);

		row += 2;
		for (int p = 0; p < arg2.length; p++) {
		    int dp = di + p;

		    sheet.addCell(new Label(column + dp, row, arg2[p],
			    tahoma9pt));
		}
	    }
	}
    }

    private void sbut_nas(String title,
	    Vector<Vector<Vector<Vector<String>>>> re, int[] arg1,
	    WritableSheet sheet, String inn, String[] ot)
	    throws RowsExceededException, WriteException {

	int countRows = arg1[0];
	int countLines = arg1[1];

	int countTables = arg1[2];
	int countColInTable = arg1[3];

	int startRow = arg1[4];
	int startCol = arg1[5];

	int titleCol = startCol;
	int titleRow = startRow + 1;

	sheet.addCell(new Label(titleCol, titleRow, title, tahoma12ptBold));

	sheet.mergeCells(titleCol, titleRow, titleCol + 10, titleRow);
	sheet.setRowView(titleRow, 750);

	for (int p = 3; p < 3 + countRows * countLines; p++)
	    sheet.setRowView(p, 450);

	sheet.setColumnView(0, 75);

	startRow += 3;
	for (int p = startRow - 1; p < startRow + countTables * countColInTable; p++)
	    sheet.setColumnView(p, 15);

	for (int index = 0; index < months.length; index++) {

	    String month = months[index];

	    Vector<Vector<Vector<String>>> result = re.get(index);

	    int dindex = index * countRows;

	    sheet.addCell(new Label(startCol, startRow + dindex, month,
		    tahoma12ptBold));

	    sheet.addCell(new Label(startCol, startRow + 1 + dindex,
		    "Потребители", tahoma9pt));
	    sheet.mergeCells(startCol, startRow + 1 + dindex, startCol,
		    startRow + 2 + dindex);

	    sheet.addCell(new Label(startCol + 1, 3 + dindex, "Код строки",
		    tahoma9pt));
	    sheet.mergeCells(startCol + 1, startRow + dindex, startCol + 1,
		    startRow + 2 + dindex);

	    for (int num = 0; num < mOtpusk_3.length; num++) {
		int column = startCol;
		int row = startRow + 3 + num + dindex;

		sheet.addCell(new Label(column, row, mOtpusk_3[num][0],
			tahoma9ptLeft));
		sheet.addCell(new Label(column + 1, row, mOtpusk_3[num][1],
			tahoma9pt));
	    }

	    for (int table = 0; table < result.size(); table++) {

		int column = startCol + countColInTable * table;

		Vector<Vector<String>> r = result.get(table);

		for (int num = 0; num < r.size(); num++) {
		    int row = startRow + 3 + num + dindex;

		    for (int p = 0; p < r.get(num).size(); p++) {

			WritableCellFormat css = tahoma9ptYellow;
			String value = r.get(num).get(p);

			if (p == 0)
			    css = tahoma9ptORANGE;

			if (month == "итог") {
			    String itsgod = "";

			    try {
				itsgod = re.get(index - 1).get(table).get(num)
					.get(p);
			    } catch (Exception e) {
			    }

			    if (parseStringToDouble(value).doubleValue() != parseStringToDouble(
				    itsgod).doubleValue())
				css = tahoma9ptRed;
			}

			sheet.addCell(new Label(column + 2 + p, row, value, css));
		    }
		}
	    }

	    countColInTable = 1;
	    for (int i = 0; i < ot.length; i++) {
		int column = startCol + 2;
		int row = startRow + dindex;
		int di = i * countColInTable;

		sheet.addCell(new Label(column + di, row, ot[i], tahoma9pt));
		sheet.mergeCells(column + di, row, column + di
			+ countColInTable - 1, row + 2);
	    }

	    String[] ooooo = {
		    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год), тыс кВт ч",
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) с НДС, тыс руб всего",
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) без НДС, тыс руб всего" };

	    String[] arg = { "ночь", "пик", "полупик (день)" };

	    countColInTable = 3;
	    for (int i = 0; i < ooooo.length; i++) {
		int column = startCol + 2;
		int row = startRow + dindex;
		int di = i * countColInTable + ot.length;

		sheet.addCell(new Label(column + di, row, ooooo[i], tahoma9pt));
		sheet.mergeCells(column + di, row, column + di
			+ countColInTable - 1, row + 1);

		row += 2;
		for (int p = 0; p < arg.length; p++) {
		    int dp = di + p;

		    sheet.addCell(new Label(column + dp, row, arg[p], tahoma9pt));
		}
	    }
	}
    }

    private void sbut_sell_buy(String title,
	    Vector<Vector<Vector<Vector<String>>>> re, int[] arg1,
	    WritableSheet sheet, String inn, String[] ot, String sb)
	    throws RowsExceededException, WriteException {

	int countRows = arg1[0];
	int countLines = arg1[1];

	int countTables = arg1[2];
	int countColInTable = arg1[3];

	int startRow = arg1[4];
	int startCol = arg1[5];

	int titleCol = startCol;
	int titleRow = startRow + 1;

	sheet.addCell(new Label(titleCol, titleRow, title, tahoma12ptBold));

	sheet.mergeCells(titleCol, titleRow, titleCol + 10, titleRow);
	sheet.setRowView(titleRow, 750);

	for (int p = 3; p < 3 + countRows * countLines; p++)
	    sheet.setRowView(p, 450);

	sheet.setColumnView(0, 75);

	startRow += 3;
	for (int p = startRow - 1; p < startRow + countTables * countColInTable; p++)
	    sheet.setColumnView(p, 15);

	for (int index = 0; index < months.length; index++) {

	    String month = months[index];

	    Vector<Vector<Vector<String>>> result = re.get(index);

	    int dindex = index * countRows;

	    sheet.addCell(new Label(startCol, startRow + dindex, month,
		    tahoma12ptBold));

	    sheet.addCell(new Label(startCol, startRow + 1 + dindex,
		    "Потребители", tahoma9pt));
	    sheet.mergeCells(startCol, startRow + 1 + dindex, startCol,
		    startRow + 2 + dindex);

	    sheet.addCell(new Label(startCol + 1, 3 + dindex, "Код строки",
		    tahoma9pt));
	    sheet.mergeCells(startCol + 1, startRow + dindex, startCol + 1,
		    startRow + 2 + dindex);
	    
	    
	    String[][] toptop = mOtpusk_6;
	    if(sb.equals("buy"))
		toptop = mOtpusk_7;

	    for (int num = 0; num < toptop.length; num++) {
		int column = startCol;
		int row = startRow + 3 + num + dindex;

		sheet.addCell(new Label(column, row, toptop[num][0],
			tahoma9ptLeft));
		sheet.addCell(new Label(column + 1, row, toptop[num][1],
			tahoma9pt));
	    }

	    for (int table = 0; table < result.size(); table++) {

		int column = startCol + countColInTable * table;

		Vector<Vector<String>> r = result.get(table);

		for (int num = 0; num < r.size(); num++) {
		    int row = startRow + 3 + num + dindex;

		    for (int p = 0; p < r.get(num).size(); p++) {

			WritableCellFormat css = tahoma9ptYellow;
			String value = r.get(num).get(p);

			if (p == 0)
			    css = tahoma9ptORANGE;

			if (month == "итог") {
			    String itsgod = "";

			    try {
				itsgod = re.get(index - 1).get(table).get(num)
					.get(p);
			    } catch (Exception e) {
			    }

			    if (parseStringToDouble(value).doubleValue() != parseStringToDouble(
				    itsgod).doubleValue())
				css = tahoma9ptRed;
			}

			sheet.addCell(new Label(column + 2 + p, row, value, css));
		    }
		}
	    }

	    countColInTable = 1;
	    for (int i = 0; i < ot.length; i++) {
		int column = startCol + 2;
		int row = startRow + dindex;
		int di = i * countColInTable;

		sheet.addCell(new Label(column + di, row, ot[i], tahoma9pt));
		sheet.mergeCells(column + di, row, column + di
			+ countColInTable - 1, row + 2);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk1(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(
		0,
		1,
		"Полезный отпуск электроэнергии, реализуемой по регулируемым тарифам (ценам)",
		tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 120; p++) {
	    sheet.setRowView(p, 450);
	}

	for (int p = 0; p < 4; p++) {
	    sheet.addCell(new Label(0, 3 + p * 25, "Потребители", tahoma9pt));
	    sheet.mergeCells(0, 3 + p * 25, 0, 5 + p * 25);

	    sheet.addCell(new Label(1, 3 + p * 25, "Код строки", tahoma9pt));
	    sheet.mergeCells(1, 3 + p * 25, 1, 5 + p * 25);

	    sheet.addCell(new Label(0, 6 + p * 25,
		    "Базовые потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    7 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    8 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    11 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 25,
		    "Прочие потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    13 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    14 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 25,
		    "Электрифицированный городской транспорт ", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    17 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    18 + p * 25,
		    "Непромышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 19 + p * 25,
		    "Сельскохозяйственные товаро-производители", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 20 + p * 25, "Бюджетные потребители",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 21 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    22 + p * 25,
		    "Собственные и производственные нужды сторонних электростанций и районных котельных",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    23 + p * 25,
		    "Компенсация расхода электрической энергии на передачу сетевыми организациями",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 24 + p * 25, "Полезный отпуск - всего ",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 6 + p * 25, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 25, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 25, "121", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 25, "131", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 25, "141", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 25, "151", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 25, "200", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 25, "211", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 25, "221", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 25, "231", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 25, "241", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 25, "251", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 25, "261", tahoma9pt));
	    sheet.addCell(new Label(1, 19 + p * 25, "271", tahoma9pt));
	    sheet.addCell(new Label(1, 20 + p * 25, "281", tahoma9pt));
	    sheet.addCell(new Label(1, 21 + p * 25, "301", tahoma9pt));
	    sheet.addCell(new Label(1, 22 + p * 25, "311", tahoma9pt));
	    sheet.addCell(new Label(1, 23 + p * 25, "321", tahoma9pt));
	    sheet.addCell(new Label(1, 24 + p * 25, "491", tahoma9pt));

	    sheet.setRowView(4 + p * 25, 850);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 6 * 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}
	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();

	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 19 * 6; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 5; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	/*
	 * заполнение формул
	 */

	for (int p = 0; p < months.length; p++) {
	    // смещение по строчно
	    int x = p;
	    // смещение по столбцам
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		for (int a = 0; a < 19; a++) {
		    for (int a2 = 0; a2 < 5; a2++) {
			String res = "";
			if (done.get(res_i).get(a2).equals("")) {
			    res = getColumnExcel(2 + i * 5 + x * 6 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			} else {
			    res = done.get(res_i).get(a2)
				    + " + "
				    + getColumnExcel(2 + i * 5 + x * 6 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			}

			done.get(res_i).set(a2, res);
		    }
		    res_i++;
		}
	    }
	}
	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name,
		    months[p], year, 0, 5);

	    // смещение по строчно
	    int x = p;
	    // смещение по столбцам
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    // название месяца
	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 5:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i * 5
			+ x * 6 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0) {
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double sum = new BigDecimal(done_num.get(res_i)
				    .get(a2)
				    + parseStringToDouble(result.get(res_i)
					    .get(a2).toString())).setScale(4,
				    RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(a2, sum);
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5 + a2,
				    6 + y * 25 + a, toNumberString(result
					    .get(res_i).get(a2).toString()),
				    tahoma9ptYellow));
			}

			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);
			sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6 + y
				* 25 + a, toNumberString(result.get(res_i++)
				.get(0).toString()), tahoma9ptGreen));
		    }
		}
	    }
	}

	// итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 5:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i * 5
			+ x * 6 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		for (int a = 0; a < 19; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + i * 5 + x * 6 * 5 + a2, 6
				+ y * 25 + a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }
		    // ВН
		    sheet.addCell(new Formula(2 + i * 5 + x * 6 * 5, 6 + y * 25
			    + a, "SUM(" + done.get(res_i++).get(0).toString()
			    + ")", tahoma9ptGreen));
		}
	    }
	}

	// Год
	{
	    int x = 0;
	    int y = 3;

	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name, "год",
		    year, 0, 5);

	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 5:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i * 5
			+ x * 6 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0)
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double res = parseStringToDouble(result.get(res_i)
				    .get(a2).toString());

			    if (res.equals(done_num.get(res_i).get(a2))) {
				sheet.addCell(new Label(2 + i * 5 + x * 6 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptYellow));
			    } else {
				sheet.addCell(new Label(2 + i * 5 + x * 6 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptORANGE));
			    }
			}
			// ВН
			Double res = parseStringToDouble(result.get(res_i)
				.get(0).toString());

			if (res.equals(done_num.get(res_i).get(0))) {
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptGreen));
			} else {
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptRed));
			}
		    }
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk2(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(
		0,
		1,
		"Полезный отпуск электроэнергии, реализуемой населению и приравненным к нему категориям потребителей",
		tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 60; p++) {
	    sheet.setRowView(p, 450);
	}

	for (int p = 0; p < 4; p++) {
	    sheet.addCell(new Label(0, 3 + p * 12, "Потребители", tahoma9pt));
	    sheet.mergeCells(0, 3 + p * 12, 0, 5 + p * 12);

	    sheet.addCell(new Label(1, 3 + p * 12, "Код строки", tahoma9pt));
	    sheet.mergeCells(1, 3 + p * 12, 1, 5 + p * 12);

	    sheet.addCell(new Label(0, 6 + p * 12, "Население, всего",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    7 + p * 12,
		    "Население, проживающее в городских населенных пунктах в домах, не оборудованных в установленном порядке стационарными электроплитами и (или) электроотопительными установками",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    8 + p * 12,
		    "Население, проживающее в городских населенных пунктах в домах, оборудованных в установленном порядке стационарными электроплитами и (или) электроотопительными установками",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 12,
		    "Население, проживающее в сельских населенных пунктах",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 12,
		    "Потребители, приравненные к населению", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 11 + p * 12, "Полезный отпуск - всего",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 6 + p * 12, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 12, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 12, "121", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 12, "131", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 12, "200", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 12, "491", tahoma9pt));

	    sheet.setRowView(4 + p * 12, 1500);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 15 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();

	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 6 * 5; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el = new Vector<Double>();
	    for (int r = 0; r < 5; r++) {
		element.add("");
		el.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }
	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		for (int a = 0; a < 6; a++) {
		    for (int a2 = 0; a2 < 3; a2++) {
			String res = "";

			if (done.get(res_i).get(a2).equals("")) {
			    res = getColumnExcel(2 + i * 3 + x * 3 * 5 + a2)
				    + Integer.toString(7 + y * 12 + a);
			} else {
			    res = done.get(res_i).get(a2)
				    + " + "
				    + getColumnExcel(2 + i * 3 + x * 3 * 5 + a2)
				    + Integer.toString(7 + y * 12 + a);
			}

			done.get(res_i).set(a2, res);
		    }
		    res_i++;
		}
	    }
	}
	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_nas(name,
		    months[p], year, 0, 4);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 3 * 5, 3 + y * 12, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 3 * 5, 3 + y * 12, 1 + (x + 1) * 3 * 5,
		    3 + y * 12);

	    sheet.addCell(new Label(
		    2 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии за отчетный месяц (год), тыс кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(2 + x * 3 * 5, 4 + y * 12, 2 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    3 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(3 + x * 3 * 5, 4 + y * 12, 3 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    4 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(4 + x * 3 * 5, 4 + y * 12, 4 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    5 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год), тыс. кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(5 + x * 3 * 5, 4 + y * 12, 5 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    6 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(6 + x * 3 * 5, 4 + y * 12, 6 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    7 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(7 + x * 3 * 5, 4 + y * 12, 7 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    8 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));
	    sheet.mergeCells(8 + x * 3 * 5, 4 + y * 12, 10 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    11 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(11 + x * 3 * 5, 4 + y * 12, 13 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    14 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(14 + x * 3 * 5, 4 + y * 12, 16 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(8 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(9 + x * 3 * 5, 5 + y * 12, "пик", tahoma9pt));
	    sheet.addCell(new Label(10 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(11 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(12 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(13 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(14 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(15 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(16 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		if (result.size() > 0) {
		    for (int a = 0; a < 6; a++) {
			for (int a2 = 1; a2 < 3; a2++) {
			    Double sum = new BigDecimal(done_num.get(res_i)
				    .get(a2)
				    + parseStringToDouble(result.get(res_i)
					    .get(a2).toString())).setScale(4,
				    RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(a2, sum);
			    sheet.addCell(new Label(2 + i * 3 + x * 3 * 5 + a2,
				    6 + y * 12 + a, toNumberString(result
					    .get(res_i).get(a2).toString()),
				    tahoma9ptYellow));
			}
			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);
			sheet.addCell(new Label(2 + i * 3 + x * 3 * 5, 6 + y
				* 12 + a, toNumberString(result.get(res_i++)
				.get(0).toString()), tahoma9ptGreen));
		    }
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 3 * 5, 3 + y * 12, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 3 * 5, 3 + y * 12, 1 + (x + 1) * 3 * 5,
		    3 + y * 12);

	    sheet.addCell(new Label(
		    2 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии за отчетный месяц (год), тыс кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(2 + x * 3 * 5, 4 + y * 12, 2 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    3 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(3 + x * 3 * 5, 4 + y * 12, 3 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    4 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(4 + x * 3 * 5, 4 + y * 12, 4 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    5 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год), тыс. кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(5 + x * 3 * 5, 4 + y * 12, 5 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    6 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(6 + x * 3 * 5, 4 + y * 12, 6 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    7 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(7 + x * 3 * 5, 4 + y * 12, 7 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    8 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));
	    sheet.mergeCells(8 + x * 3 * 5, 4 + y * 12, 10 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    11 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(11 + x * 3 * 5, 4 + y * 12, 13 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    14 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(14 + x * 3 * 5, 4 + y * 12, 16 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(8 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(9 + x * 3 * 5, 5 + y * 12, "пик", tahoma9pt));
	    sheet.addCell(new Label(10 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(11 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(12 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(13 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(14 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(15 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(16 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		for (int a = 0; a < 6; a++) {
		    for (int a2 = 1; a2 < 3; a2++) {
			sheet.addCell(new Formula(2 + i * 3 + x * 3 * 5 + a2, 6
				+ y * 12 + a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + i * 3 + x * 3 * 5, 6 + y * 12
			    + a, "SUM(" + done.get(res_i++).get(0).toString()
			    + ")", tahoma9ptGreen));
		}

	    }
	}

	// Год
	{
	    int x = 0;
	    int y = 3;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_nas(name,
		    "год", year, 0, 4);

	    sheet.addCell(new Label(2 + x * 3 * 5, 3 + y * 12, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 3 * 5, 3 + y * 12, 1 + (x + 1) * 3 * 5,
		    3 + y * 12);

	    sheet.addCell(new Label(
		    2 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии за отчетный месяц (год), тыс кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(2 + x * 3 * 5, 4 + y * 12, 2 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    3 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(3 + x * 3 * 5, 4 + y * 12, 3 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    4 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(4 + x * 3 * 5, 4 + y * 12, 4 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    5 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год), тыс. кВтч всего",
		    tahoma9pt));
	    sheet.mergeCells(5 + x * 3 * 5, 4 + y * 12, 5 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    6 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(6 + x * 3 * 5, 4 + y * 12, 6 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    7 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочному тарифу за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(7 + x * 3 * 5, 4 + y * 12, 7 + x * 3 * 5,
		    5 + y * 12);

	    sheet.addCell(new Label(
		    8 + x * 3 * 5,
		    4 + y * 12,
		    "Объем электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));
	    sheet.mergeCells(8 + x * 3 * 5, 4 + y * 12, 10 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    11 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) с НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(11 + x * 3 * 5, 4 + y * 12, 13 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(
		    14 + x * 3 * 5,
		    4 + y * 12,
		    "Стоимость электрической энергии потребителей, осуществляющих оплату по зонным тарифам за отчетный месяц (год) без НДС, тыс. руб. всего",
		    tahoma9pt));
	    sheet.mergeCells(14 + x * 3 * 5, 4 + y * 12, 16 + x * 3 * 5,
		    4 + y * 12);

	    sheet.addCell(new Label(8 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(9 + x * 3 * 5, 5 + y * 12, "пик", tahoma9pt));
	    sheet.addCell(new Label(10 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(11 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(12 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(13 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    sheet.addCell(new Label(14 + x * 3 * 5, 5 + y * 12, "ночь",
		    tahoma9pt));
	    sheet.addCell(new Label(15 + x * 3 * 5, 5 + y * 12, "пик",
		    tahoma9pt));
	    sheet.addCell(new Label(16 + x * 3 * 5, 5 + y * 12, "полупик",
		    tahoma9pt));

	    int res_i = 0;
	    if (result.size() > 0)
		for (int i = 0; i < 5; i++) {
		    for (int a = 0; a < 6; a++) {
			for (int a2 = 1; a2 < 3; a2++) {
			    Double res = parseStringToDouble(result.get(res_i)
				    .get(a2).toString());

			    if (res.equals(done_num.get(res_i).get(a2))) {
				sheet.addCell(new Label(2 + i * 3 + x * 3 * 5
					+ a2, 6 + y * 12 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptYellow));
			    } else {
				sheet.addCell(new Label(2 + i * 3 + x * 3 * 5
					+ a2, 6 + y * 12 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptORANGE));
			    }
			}
			Double res = parseStringToDouble(result.get(res_i)
				.get(0).toString());

			if (res.equals(done_num.get(res_i).get(0))) {
			    sheet.addCell(new Label(2 + i * 3 + x * 3 * 5, 6
				    + y * 12 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptGreen));
			} else {
			    sheet.addCell(new Label(2 + i * 3 + x * 3 * 5, 6
				    + y * 12 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptRed));
			}
		    }

		}
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk3(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(
		0,
		1,
		"Полезный отпуск электрической мощности, реализуемой по регулируемым тарифам (ценам)",
		tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 120; p++) {
	    sheet.setRowView(p, 450);
	}
	for (int p = 0; p < 4; p++) {
	    sheet.addCell(new Label(0, 3 + p * 25, "Потребители", tahoma9pt));
	    sheet.mergeCells(0, 3 + p * 25, 0, 5 + p * 25);

	    sheet.addCell(new Label(1, 3 + p * 25, "Код строки", tahoma9pt));
	    sheet.mergeCells(1, 3 + p * 25, 1, 5 + p * 25);

	    sheet.addCell(new Label(0, 6 + p * 25,
		    "Базовые потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    7 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    8 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    11 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 25,
		    "Прочие потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    13 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    14 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 25,
		    "Электрифицированный городской транспорт ", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    17 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    18 + p * 25,
		    "Непромышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 19 + p * 25,
		    "Сельскохозяйственные товаро-производители", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 20 + p * 25, "Бюджетные потребители",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 21 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    22 + p * 25,
		    "Собственные и производственные нужды сторонних электростанций и районных котельных",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    23 + p * 25,
		    "Компенсация расхода электрической энергии на передачу сетевыми организациями",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 24 + p * 25, "Полезный отпуск - всего ",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 6 + p * 25, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 25, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 25, "121", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 25, "131", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 25, "141", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 25, "151", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 25, "200", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 25, "211", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 25, "221", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 25, "231", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 25, "241", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 25, "251", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 25, "261", tahoma9pt));
	    sheet.addCell(new Label(1, 19 + p * 25, "271", tahoma9pt));
	    sheet.addCell(new Label(1, 20 + p * 25, "281", tahoma9pt));
	    sheet.addCell(new Label(1, 21 + p * 25, "301", tahoma9pt));
	    sheet.addCell(new Label(1, 22 + p * 25, "311", tahoma9pt));
	    sheet.addCell(new Label(1, 23 + p * 25, "321", tahoma9pt));
	    sheet.addCell(new Label(1, 24 + p * 25, "491", tahoma9pt));

	    sheet.setRowView(4 + p * 25, 850);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 5 * 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();
	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 19 * 5; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 6; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;
	    for (int i = 0; i < 5; i++) {
		for (int a = 0; a < 19; a++) {
		    for (int a2 = 0; a2 < 5; a2++) {
			String res = "";
			if (done.get(res_i).get(a2).equals("")) {
			    res = getColumnExcel(2 + i * 5 + x * 5 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			} else {
			    res = done.get(res_i).get(a2)
				    + " + "
				    + getColumnExcel(2 + i * 5 + x * 5 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			}
			done.get(res_i).set(a2, res);

		    }
		    res_i++;
		}
	    }
	}

	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name,
		    months[p], year, 6, 10);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0) {
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double sum = new BigDecimal(done_num.get(res_i)
				    .get(a2)
				    + parseStringToDouble(result.get(res_i)
					    .get(a2).toString())).setScale(4,
				    RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(a2, sum);
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5 + a2,
				    6 + y * 25 + a, toNumberString(result
					    .get(res_i).get(a2).toString()),
				    tahoma9ptYellow));
			}
			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);
			sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6 + y
				* 25 + a, toNumberString(result.get(res_i++)
				.get(0).toString()), tahoma9ptGreen));
		    }
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		for (int a = 0; a < 19; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + i * 5 + x * 5 * 5 + a2, 6
				+ y * 25 + a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + i * 5 + x * 5 * 5, 6 + y * 25
			    + a, "SUM(" + done.get(res_i++).get(0).toString()
			    + ")", tahoma9ptGreen));
		}
	    }
	}

	// год
	{
	    int x = 0;
	    int y = 3;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name, "год",
		    year, 6, 10);

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0)
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double res = parseStringToDouble(result.get(res_i)
				    .get(a2).toString());

			    if (res.equals(done_num.get(res_i).get(a2))) {
				sheet.addCell(new Label(2 + i * 5 + x * 5 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptYellow));
			    } else {
				sheet.addCell(new Label(2 + i * 5 + x * 5 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptORANGE));
			    }
			}

			Double res = parseStringToDouble(result.get(res_i)
				.get(0).toString());

			if (res.equals(done_num.get(res_i).get(0))) {
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptGreen));
			} else {
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptRed));
			}
		    }
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk4(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(
		0,
		1,
		"Полезный отпуск электроэнергии, реализуемой по нерегулируемым ценам",
		tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 120; p++) {
	    sheet.setRowView(p, 450);
	}
	for (int p = 0; p < 4; p++) {
	    sheet.addCell(new Label(0, 3 + p * 25, "Потребители", tahoma9pt));
	    sheet.mergeCells(0, 3 + p * 25, 0, 5 + p * 25);

	    sheet.addCell(new Label(1, 3 + p * 25, "Код строки", tahoma9pt));
	    sheet.mergeCells(1, 3 + p * 25, 1, 5 + p * 25);

	    sheet.addCell(new Label(0, 6 + p * 25,
		    "Базовые потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    7 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    8 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    11 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 25,
		    "Прочие потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    13 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    14 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 25,
		    "Электрифицированный городской транспорт ", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    17 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    18 + p * 25,
		    "Непромышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 19 + p * 25,
		    "Сельскохозяйственные товаро-производители", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 20 + p * 25, "Бюджетные потребители",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 21 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    22 + p * 25,
		    "Собственные и производственные нужды сторонних электростанций и районных котельных",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    23 + p * 25,
		    "Компенсация расхода электрической энергии на передачу сетевыми организациями",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 24 + p * 25, "Полезный отпуск - всего ",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 6 + p * 25, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 25, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 25, "121", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 25, "131", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 25, "141", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 25, "151", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 25, "200", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 25, "211", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 25, "221", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 25, "231", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 25, "241", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 25, "251", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 25, "261", tahoma9pt));
	    sheet.addCell(new Label(1, 19 + p * 25, "271", tahoma9pt));
	    sheet.addCell(new Label(1, 20 + p * 25, "281", tahoma9pt));
	    sheet.addCell(new Label(1, 21 + p * 25, "301", tahoma9pt));
	    sheet.addCell(new Label(1, 22 + p * 25, "311", tahoma9pt));
	    sheet.addCell(new Label(1, 23 + p * 25, "321", tahoma9pt));
	    sheet.addCell(new Label(1, 24 + p * 25, "491", tahoma9pt));

	    sheet.setRowView(4 + p * 25, 850);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 6 * 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();
	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 19 * 6; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 5; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		for (int a = 0; a < 19; a++) {
		    for (int a2 = 0; a2 < 5; a2++) {
			String res = "";
			if (done.get(res_i).get(a2).equals("")) {
			    res = getColumnExcel(2 + i * 5 + x * 6 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			} else {
			    res = done.get(res_i).get(a2)
				    + " + "
				    + getColumnExcel(2 + i * 5 + x * 6 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			}
			done.get(res_i).set(a2, res);
		    }
		    res_i++;
		}
	    }
	}

	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name,
		    months[p], year, 11, 16);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 5:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i * 5
			+ x * 6 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0) {
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double sum = new BigDecimal(done_num.get(res_i)
				    .get(a2)
				    + parseStringToDouble(result.get(res_i)
					    .get(a2).toString())).setScale(4,
				    RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(a2, sum);
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5 + a2,
				    6 + y * 25 + a, toNumberString(result
					    .get(res_i).get(a2).toString()),
				    tahoma9ptYellow));
			}

			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);
			sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6 + y
				* 25 + a, toNumberString(result.get(res_i++)
				.get(0).toString()), tahoma9ptGreen));
		    }
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 6; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
			    tahoma9pt));
		    break;
		case 5:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 6 * 5,
			    4 + y * 25,
			    "Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i * 5
			+ x * 6 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		for (int a = 0; a < 19; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + i * 5 + x * 6 * 5 + a2, 6
				+ y * 25 + a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + i * 5 + x * 6 * 5, 6 + y * 25
			    + a, "SUM(" + done.get(res_i++).get(0).toString()
			    + ")", tahoma9ptGreen));
		}
	    }
	}

	// год
	{
	    int x = 0;
	    int y = 3;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name, "год",
		    year, 11, 16);

	    sheet.addCell(new Label(2 + x * 5 * 6, 3 + y * 25, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 6, 3 + y * 25, 1 + (x + 1) * 5 * 6,
		    3 + y * 25);

	    int res_i = 0;

	    if (result.size() > 0)
		for (int i = 0; i < 6; i++) {
		    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 5 + y * 25,
			    "Всего", tahoma9pt));

		    switch (i) {
		    case 0:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
				tahoma9pt));
			break;
		    case 1:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Стоимость электрической энергии за отчетный месяц (год) без НДС, тыс. руб.",
				tahoma9pt));
			break;
		    case 2:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Объем электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
				tahoma9pt));
			break;
		    case 3:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Стоимость электрической энергии потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
				tahoma9pt));
			break;
		    case 4:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Объем электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), тыс. кВтч",
				tahoma9pt));
			break;
		    case 5:
			sheet.addCell(new Label(
				2 + i * 5 + x * 6 * 5,
				4 + y * 25,
				"Стоимость электрической энергии потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
				tahoma9pt));
			break;
		    }
		    sheet.mergeCells(2 + i * 5 + x * 6 * 5, 4 + y * 25, 6 + i
			    * 5 + x * 6 * 5, 4 + y * 25);

		    sheet.addCell(new Label(3 + i * 5 + x * 6 * 5, 5 + y * 25,
			    "ВН", tahoma9pt));
		    sheet.addCell(new Label(4 + i * 5 + x * 6 * 5, 5 + y * 25,
			    "СН1", tahoma9pt));
		    sheet.addCell(new Label(5 + i * 5 + x * 6 * 5, 5 + y * 25,
			    "СН2", tahoma9pt));
		    sheet.addCell(new Label(6 + i * 5 + x * 6 * 5, 5 + y * 25,
			    "НН", tahoma9pt));

		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double res = parseStringToDouble(result.get(res_i)
				    .get(a2).toString());

			    if (res.equals(done_num.get(res_i).get(a2))) {
				sheet.addCell(new Label(2 + i * 5 + x * 6 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptYellow));
			    } else {
				sheet.addCell(new Label(2 + i * 5 + x * 6 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptORANGE));
			    }
			}
			Double res = parseStringToDouble(result.get(res_i)
				.get(0).toString());

			if (res.equals(done_num.get(res_i).get(0))) {
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptGreen));
			} else {
			    sheet.addCell(new Label(2 + i * 5 + x * 6 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptRed));
			}
		    }
		}
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk5(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(
		0,
		1,
		"Полезный отпуск электрической мощности, реализуемой по нерегулируемым ценам",
		tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 120; p++) {
	    sheet.setRowView(p, 450);
	}
	for (int p = 0; p < 4; p++) {
	    sheet.addCell(new Label(0, 3 + p * 25, "Потребители", tahoma9pt));
	    sheet.mergeCells(0, 3 + p * 25, 0, 5 + p * 25);

	    sheet.addCell(new Label(1, 3 + p * 25, "Код строки", tahoma9pt));
	    sheet.mergeCells(1, 3 + p * 25, 1, 5 + p * 25);

	    sheet.addCell(new Label(0, 6 + p * 25,
		    "Базовые потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    7 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    8 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    11 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 25,
		    "Прочие потребители, в т.ч.", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    13 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    14 + p * 25,
		    "Промышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 25,
		    "Электрифицированный железнодорожный транспорт",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 25,
		    "Электрифицированный городской транспорт ", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    17 + p * 25,
		    "Непромышленные потребители с присоединенной мощностью 750 кВа и выше",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    18 + p * 25,
		    "Непромышленные и приравненные к ним потребители с присоединенной мощностью до 750 кВа",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 19 + p * 25,
		    "Сельскохозяйственные товаро-производители", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 20 + p * 25, "Бюджетные потребители",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 21 + p * 25,
		    "Другие энергоснабжающие организации", tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    22 + p * 25,
		    "Собственные и производственные нужды сторонних электростанций и районных котельных",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(
		    0,
		    23 + p * 25,
		    "Компенсация расхода электрической энергии на передачу сетевыми организациями",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 24 + p * 25, "Полезный отпуск - всего ",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 6 + p * 25, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 25, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 25, "121", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 25, "131", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 25, "141", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 25, "151", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 25, "200", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 25, "211", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 25, "221", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 25, "231", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 25, "241", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 25, "251", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 25, "261", tahoma9pt));
	    sheet.addCell(new Label(1, 19 + p * 25, "271", tahoma9pt));
	    sheet.addCell(new Label(1, 20 + p * 25, "281", tahoma9pt));
	    sheet.addCell(new Label(1, 21 + p * 25, "301", tahoma9pt));
	    sheet.addCell(new Label(1, 22 + p * 25, "311", tahoma9pt));
	    sheet.addCell(new Label(1, 23 + p * 25, "321", tahoma9pt));
	    sheet.addCell(new Label(1, 24 + p * 25, "491", tahoma9pt));

	    sheet.setRowView(4 + p * 25, 850);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 5 * 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();
	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 19 * 5; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 6; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		for (int a = 0; a < 19; a++) {
		    for (int a2 = 0; a2 < 5; a2++) {
			String res = "";
			if (done.get(res_i).get(a2).equals("")) {
			    res = getColumnExcel(2 + i * 5 + x * 5 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			} else {
			    res = done.get(res_i).get(a2)
				    + " + "
				    + getColumnExcel(2 + i * 5 + x * 5 * 5 + a2)
				    + Integer.toString(7 + y * 25 + a);
			}
			done.get(res_i).set(a2, res);
		    }
		    res_i++;
		}
	    }
	}

	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name,
		    months[p], year, 17, 21);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0) {
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double sum = new BigDecimal(done_num.get(res_i)
				    .get(a2)
				    + parseStringToDouble(result.get(res_i)
					    .get(a2).toString())).setScale(4,
				    RoundingMode.HALF_UP).doubleValue();

			    done_num.get(res_i).set(a2, sum);
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5 + a2,
				    6 + y * 25 + a, toNumberString(result
					    .get(res_i).get(a2).toString()),
				    tahoma9ptYellow));
			}

			Double sum = new BigDecimal(done_num.get(res_i).get(0)
				+ parseStringToDouble(result.get(res_i).get(0)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(0, sum);
			sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6 + y
				* 25 + a, toNumberString(result.get(res_i++)
				.get(0).toString()), tahoma9ptGreen));
		    }
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		for (int a = 0; a < 19; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + i * 5 + x * 5 * 5 + a2, 6
				+ y * 25 + a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + i * 5 + x * 5 * 5, 6 + y * 25
			    + a, "SUM(" + done.get(res_i++).get(0).toString()
			    + ")", tahoma9ptGreen));
		}
	    }
	}

	// год
	{
	    int x = 0;
	    int y = 3;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut(name, "год",
		    year, 17, 21);

	    sheet.addCell(new Label(2 + x * 5 * 5, 3 + y * 25, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5 * 5, 3 + y * 25, 1 + (x + 1) * 5 * 5,
		    3 + y * 25);

	    int res_i = 0;

	    for (int i = 0; i < 5; i++) {
		sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 5 + y * 25,
			"Всего", tahoma9pt));

		switch (i) {
		case 0:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 1:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности  за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		case 2:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по одноставочным и зонным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 3:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Объем электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год), мВт",
			    tahoma9pt));
		    break;
		case 4:
		    sheet.addCell(new Label(
			    2 + i * 5 + x * 5 * 5,
			    4 + y * 25,
			    "Стоимость электрической мощности потребителей, осуществляющих оплату по двухставочным тарифам (ценам) за отчетный месяц (год) без НДС, тыс. руб.",
			    tahoma9pt));
		    break;
		}
		sheet.mergeCells(2 + i * 5 + x * 5 * 5, 4 + y * 25, 6 + i * 5
			+ x * 5 * 5, 4 + y * 25);

		sheet.addCell(new Label(3 + i * 5 + x * 5 * 5, 5 + y * 25,
			"ВН", tahoma9pt));
		sheet.addCell(new Label(4 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН1", tahoma9pt));
		sheet.addCell(new Label(5 + i * 5 + x * 5 * 5, 5 + y * 25,
			"СН2", tahoma9pt));
		sheet.addCell(new Label(6 + i * 5 + x * 5 * 5, 5 + y * 25,
			"НН", tahoma9pt));

		if (result.size() > 0)
		    for (int a = 0; a < 19; a++) {
			for (int a2 = 1; a2 < 5; a2++) {
			    Double res = parseStringToDouble(result.get(res_i)
				    .get(a2).toString());

			    if (res.equals(done_num.get(res_i).get(a2))) {
				sheet.addCell(new Label(2 + i * 5 + x * 5 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptYellow));
			    } else {
				sheet.addCell(new Label(2 + i * 5 + x * 5 * 5
					+ a2, 6 + y * 25 + a, result.get(res_i)
					.get(a2).toString(), tahoma9ptORANGE));
			    }
			}
			// ВН
			Double res = parseStringToDouble(result.get(res_i)
				.get(0).toString());

			if (res.equals(done_num.get(res_i).get(0))) {
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptGreen));
			} else {
			    sheet.addCell(new Label(2 + i * 5 + x * 5 * 5, 6
				    + y * 25 + a, result.get(res_i++).get(0)
				    .toString(), tahoma9ptRed));
			}
		    }
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk6(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(0, 1,
		"Продажа электрической энергии и мощности", tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 90; p++) {
	    sheet.setRowView(p, 450);
	}

	for (int p = 0; p < 3; p++) {
	    sheet.addCell(new Label(0, 4 + p * 20, "Потребители", tahoma9pt));

	    sheet.addCell(new Label(1, 4 + p * 20, "Код строки", tahoma9pt));

	    sheet.addCell(new Label(0, 5 + p * 20, "Продажа", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 6 + p * 20,
		    "В обеспечение регулируемых договоров (РД)", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 7 + p * 20,
		    "В обеспечение биржевых СДЭМ", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 8 + p * 20,
		    "В обеспечение внебиржевых СДЭМ", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 20, "В обеспечение СДД",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 20,
		    "В обеспечение договоров с ГЭС/АЭС", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 11 + p * 20, "По ценам РСВ",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 20, "БР", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 13 + p * 20,
		    "Экспортно-импортная и приграничная торговля",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 14 + p * 20, "По результатам КОМ",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 20,
		    "На розничном рынке по регулируемым тарифам (ценам)",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 20,
		    "На розничном рынке по свободным ценам", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 17 + p * 20, "Прочее", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 18 + p * 20, "Собственное производство",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 19 + p * 20,
		    "Мощность, заявленная на КОМ", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 20 + p * 20, "Аттестованная мощность",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 21 + p * 20, "Штрафные санкции ЦФР",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 5 + p * 20, "300", tahoma9pt));
	    sheet.addCell(new Label(1, 6 + p * 20, "301", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 20, "302", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 20, "303", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 20, "304", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 20, "305", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 20, "306", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 20, "307", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 20, "308", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 20, "309", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 20, "310", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 20, "311", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 20, "312", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 20, "400", tahoma9pt));
	    sheet.addCell(new Label(1, 19 + p * 20, "500", tahoma9pt));
	    sheet.addCell(new Label(1, 20 + p * 20, "600", tahoma9pt));
	    sheet.addCell(new Label(1, 21 + p * 20, "700", tahoma9pt));

	    sheet.setRowView(4 + p * 20, 1500);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();
	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 17; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 5; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;

	    for (int a = 0; a < 17; a++) {
		for (int a2 = 0; a2 < 5; a2++) {
		    String res = "";
		    if (done.get(res_i).get(a2).equals("")) {
			res = getColumnExcel(2 + x * 5 + a2)
				+ Integer.toString(6 + y * 20 + a);
		    } else {
			res = done.get(res_i).get(a2) + " + "
				+ getColumnExcel(2 + x * 5 + a2)
				+ Integer.toString(6 + y * 20 + a);
		    }
		    done.get(res_i).set(a2, res);
		}
		res_i++;
	    }
	}

	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_sell(name,
		    months[p], year);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (result.size() > 0) {
		for (int a = 0; a < 17; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			Double sum = new BigDecimal(done_num.get(res_i).get(a2)
				+ parseStringToDouble(result.get(res_i).get(a2)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(a2, sum);
			sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20 + a,
				toNumberString(result.get(res_i).get(a2)
					.toString()), tahoma9ptYellow));
		    }
		    Double sum = new BigDecimal(done_num.get(res_i).get(0)
			    + parseStringToDouble(result.get(res_i).get(0)
				    .toString())).setScale(4,
			    RoundingMode.HALF_UP).doubleValue();

		    done_num.get(res_i).set(0, sum);
		    sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
			    toNumberString(result.get(res_i++).get(0)
				    .toString()), tahoma9ptGreen));
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (done.size() > 0) {
		for (int a = 0; a < 17; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + x * 5 + a2, 5 + y * 20
				+ a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + x * 5, 5 + y * 20 + a, "SUM("
			    + done.get(res_i++).get(0).toString() + ")",
			    tahoma9ptGreen));
		}
	    }
	}

	// год
	{
	    int x = 1;
	    int y = 2;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_sell(name,
		    "год", year);

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (result.size() > 0) {
		for (int a = 0; a < 17; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			Double res = parseStringToDouble(result.get(res_i)
				.get(a2).toString());

			if (res.equals(done_num.get(res_i).get(a2))) {
			    sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20
				    + a, result.get(res_i).get(a2).toString(),
				    tahoma9ptYellow));
			} else {
			    sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20
				    + a, result.get(res_i).get(a2).toString(),
				    tahoma9ptORANGE));
			}
		    }

		    // ВН
		    Double res = parseStringToDouble(result.get(res_i).get(0)
			    .toString());

		    if (res.equals(done_num.get(res_i).get(0))) {
			sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
				result.get(res_i++).get(0).toString(),
				tahoma9ptGreen));
		    } else {
			sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
				result.get(res_i++).get(0).toString(),
				tahoma9ptRed));
		    }

		}
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private void otpusk7(WritableSheet sheet, String name)
	    throws RowsExceededException, WriteException {
	sheet.addCell(new Label(0, 1,
		"Продажа электрической энергии и мощности", tahoma12ptBold));
	sheet.mergeCells(0, 1, 10, 1);
	sheet.setRowView(1, 750);

	for (int p = 3; p < 90; p++) {
	    sheet.setRowView(p, 450);
	}

	for (int p = 0; p < 3; p++) {
	    sheet.addCell(new Label(0, 4 + p * 20, "Потребители", tahoma9pt));

	    sheet.addCell(new Label(1, 4 + p * 20, "Код строки", tahoma9pt));

	    sheet.addCell(new Label(0, 5 + p * 20, "Покупка", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 6 + p * 20, "В обеспечение РД",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 7 + p * 20,
		    "В обеспечение биржевых СДЭМ", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 8 + p * 20,
		    "В обеспечение внебиржевых СДЭМ", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 9 + p * 20, "В обеспечение СДД",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 10 + p * 20,
		    "В обеспечение договоров с ГЭС/АЭС", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 11 + p * 20, "По ценам РСВ",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 12 + p * 20, "БР", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 13 + p * 20,
		    "Экспортно-импортная и приграничная торговля",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 14 + p * 20, "По результатам КОМ",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 15 + p * 20,
		    "На розничном рынке по регулируемым тарифам (ценам)",
		    tahoma9ptLeft));
	    sheet.addCell(new Label(0, 16 + p * 20,
		    "На розничном рынке по свободным ценам", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 17 + p * 20, "Прочее", tahoma9ptLeft));
	    sheet.addCell(new Label(0, 18 + p * 20, "Собственное потребление",
		    tahoma9ptLeft));

	    sheet.addCell(new Label(1, 5 + p * 20, "100", tahoma9pt));
	    sheet.addCell(new Label(1, 6 + p * 20, "101", tahoma9pt));
	    sheet.addCell(new Label(1, 7 + p * 20, "102", tahoma9pt));
	    sheet.addCell(new Label(1, 8 + p * 20, "103", tahoma9pt));
	    sheet.addCell(new Label(1, 9 + p * 20, "104", tahoma9pt));
	    sheet.addCell(new Label(1, 10 + p * 20, "105", tahoma9pt));
	    sheet.addCell(new Label(1, 11 + p * 20, "106", tahoma9pt));
	    sheet.addCell(new Label(1, 12 + p * 20, "107", tahoma9pt));
	    sheet.addCell(new Label(1, 13 + p * 20, "108", tahoma9pt));
	    sheet.addCell(new Label(1, 14 + p * 20, "109", tahoma9pt));
	    sheet.addCell(new Label(1, 15 + p * 20, "110", tahoma9pt));
	    sheet.addCell(new Label(1, 16 + p * 20, "111", tahoma9pt));
	    sheet.addCell(new Label(1, 17 + p * 20, "112", tahoma9pt));
	    sheet.addCell(new Label(1, 18 + p * 20, "200", tahoma9pt));

	    sheet.setRowView(4 + p * 20, 1500);
	}

	sheet.setColumnView(0, 50);
	for (int p = 2; p < 5 * 7 + 2; p++) {
	    sheet.setColumnView(p, 15);
	}

	@SuppressWarnings("rawtypes")
	Vector<Vector> done = new Vector<Vector>();
	Vector<Vector<Double>> done_num = new Vector<Vector<Double>>();

	for (int v = 0; v < 14; v++) {
	    Vector<String> element = new Vector<String>();
	    Vector<Double> el_num = new Vector<Double>();
	    for (int r = 0; r < 5; r++) {
		element.add("");
		el_num.add((Double) 0.0);
	    }
	    done.add(element);
	    done_num.add(el_num);
	}

	for (int p = 0; p < months.length; p++) {
	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    int res_i = 0;

	    for (int a = 0; a < 14; a++) {
		for (int a2 = 0; a2 < 5; a2++) {
		    String res = "";
		    if (done.get(res_i).get(a2).equals("")) {
			res = getColumnExcel(2 + x * 5 + a2)
				+ Integer.toString(6 + y * 20 + a);
		    } else {
			res = done.get(res_i).get(a2) + " + "
				+ getColumnExcel(2 + x * 5 + a2)
				+ Integer.toString(6 + y * 20 + a);
		    }
		    done.get(res_i).set(a2, res);
		}
		res_i++;
	    }
	}

	for (int p = 0; p < months.length; p++) {
	    // @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	    @SuppressWarnings({ "rawtypes" })
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_buy(name,
		    months[p], year);

	    int x = p;
	    int y = 0;

	    if (p > 5) {
		x = x - 6;
		y = 1;
	    }

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, months[p],
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (result.size() > 0) {
		for (int a = 0; a < 14; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			Double sum = new BigDecimal(done_num.get(res_i).get(a2)
				+ parseStringToDouble(result.get(res_i).get(a2)
					.toString())).setScale(4,
				RoundingMode.HALF_UP).doubleValue();

			done_num.get(res_i).set(a2, sum);
			sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20 + a,
				toNumberString(result.get(res_i).get(a2)
					.toString()), tahoma9ptYellow));
		    }
		    Double sum = new BigDecimal(done_num.get(res_i).get(0)
			    + parseStringToDouble(result.get(res_i).get(0)
				    .toString())).setScale(4,
			    RoundingMode.HALF_UP).doubleValue();

		    done_num.get(res_i).set(0, sum);
		    sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
			    toNumberString(result.get(res_i++).get(0)
				    .toString()), tahoma9ptGreen));
		}
	    }
	}

	// Итог
	{
	    int x = 0;
	    int y = 2;

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, "Итог",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (done.size() > 0) {
		for (int a = 0; a < 14; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			sheet.addCell(new Formula(2 + x * 5 + a2, 5 + y * 20
				+ a, "SUM("
				+ done.get(res_i).get(a2).toString() + ")",
				tahoma9ptYellow));
		    }

		    sheet.addCell(new Formula(2 + x * 5, 5 + y * 20 + a, "SUM("
			    + done.get(res_i++).get(0).toString() + ")",
			    tahoma9ptGreen));
		}
	    }
	}

	// год
	{
	    int x = 1;
	    int y = 2;

	    @SuppressWarnings("rawtypes")
	    Vector<Vector> result = new ConnectionBD().getInfoSbut_buy(name,
		    "год", year);

	    sheet.addCell(new Label(2 + x * 5, 3 + y * 20, "Год",
		    tahoma12ptBold));
	    sheet.mergeCells(2 + x * 5, 3 + y * 20, 1 + (x + 1) * 5, 3 + y * 20);

	    sheet.addCell(new Label(
		    2 + x * 5,
		    4 + y * 20,
		    "Объем электрической энергии за отчетный месяц (год), тыс. кВтч",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    3 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической энергии за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    4 + x * 5,
		    4 + y * 20,
		    "Величина электрической мощности за отчетный месяц (в среднем за год), МВт",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    5 + x * 5,
		    4 + y * 20,
		    "Стоимость электрической мощности за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    sheet.addCell(new Label(
		    6 + x * 5,
		    4 + y * 20,
		    "Стоимость без дифференциации на энергию и мощность за отчетный месяц (год), тыс. руб.",
		    tahoma9pt));

	    int res_i = 0;

	    if (result.size() > 0) {
		for (int a = 0; a < 14; a++) {
		    for (int a2 = 1; a2 < 5; a2++) {
			Double res = parseStringToDouble(result.get(res_i)
				.get(a2).toString());

			if (res.equals(done_num.get(res_i).get(a2))) {
			    sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20
				    + a, result.get(res_i).get(a2).toString(),
				    tahoma9ptYellow));
			} else {
			    sheet.addCell(new Label(2 + x * 5 + a2, 5 + y * 20
				    + a, result.get(res_i).get(a2).toString(),
				    tahoma9ptORANGE));
			}
		    }

		    // ВН
		    Double res = parseStringToDouble(result.get(res_i).get(0)
			    .toString());

		    if (res.equals(done_num.get(res_i).get(0))) {
			sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
				result.get(res_i++).get(0).toString(),
				tahoma9ptGreen));
		    } else {
			sheet.addCell(new Label(2 + x * 5, 5 + y * 20 + a,
				result.get(res_i++).get(0).toString(),
				tahoma9ptRed));
		    }
		}
	    }
	}
    }

    private String getColumnExcel(Integer value) {
	// промежуточный результат
	String result = "";
	// для определения первого символа
	boolean first = true;

	while (value / 26 > 0) {
	    if (first) {
		result += (char) (65 + value % 26);
		first = false;
	    } else {
		result += (char) (64 + value % 26);
	    }

	    value = value / 26;
	}

	if (first) {
	    result += (char) (65 + value % 26);
	} else {
	    result += (char) (64 + value % 26);
	}

	// переварачиваем результат EFA = > AFE
	String res = "";

	for (int i = 0; i < result.length(); i++) {
	    res += result.substring(result.length() - i - 1, result.length()
		    - i);
	}

	return res;
    }

    private String toNumberString(String value) {
	// проверяем на наличие информации в ячейке
	if (value != null) {
	    // если что-то есть, возвращаем строковок представление
	    value = value.replace(" ", "");
	    return value;
	}
	// пустая строка, то возвращаем пустую строку
	return "";
    }

    private Double parseStringToDouble(String value) {
	if (value != null) {
	    value = value.replace(" ", "");
	    value = value.replace(" ", "");
	    value = value.replace(",", ".");

	    try {
		return new BigDecimal(value).setScale(4, RoundingMode.HALF_UP)
			.doubleValue();
	    } catch (Exception e) {
		return 0.0;
	    }
	}

	return 0.0;
    }
}
