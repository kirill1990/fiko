package ru.fiko.purchase.windows.organization;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import ru.fiko.purchase.windows.purchase.Data;

public class PurchaseDataTest {
    
    private Data test;
    
    @Before
    public void init() throws SQLException, ClassNotFoundException{
	test = new Data(1, null);
    }

    @Test
    public void testCalcEco(){

	assertEquals(
		"100.00",
		test.calcEco(BigDecimal.valueOf(1000.0),
			BigDecimal.valueOf(900.0)).toString());
	assertEquals(
		"300.16",
		test.calcEco(BigDecimal.valueOf(1200.50),
			BigDecimal.valueOf(900.34)).toString());
	assertEquals(
		"19.50",
		test.calcEco(BigDecimal.valueOf(40.0), BigDecimal.valueOf(20.5))
			.toString());
    }

    @Test
    public void testCalcPerEco(){

	assertEquals(
		"11.00",
		test.calcPerEco(BigDecimal.valueOf(1000.0),
			BigDecimal.valueOf(900.0)).toString());
	assertEquals(
		"33.00",
		test.calcPerEco(BigDecimal.valueOf(1200.50),
			BigDecimal.valueOf(900.34)).toString());
	assertEquals(
		"95.00",
		test.calcPerEco(BigDecimal.valueOf(40.0),
			BigDecimal.valueOf(20.5)).toString());
    }

}
