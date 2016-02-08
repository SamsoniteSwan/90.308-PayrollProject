package com.bluelight.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * PayPeriodTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
public class PayPeriodTest {

    private static BigDecimal TEST_WAGE = new BigDecimal(10.00);
    private static BigDecimal TEST_HOURS = new BigDecimal(40.00);

    private PayPeriod testPeriod;

    @Before
    public void setup() {
        testPeriod = new PayPeriod();
        testPeriod.setHourlyRate(TEST_WAGE);
        testPeriod.setHoursWorked(TEST_HOURS);
    }

    @Test
    public void testInstatiation() {
        assertTrue(testPeriod.getHourlyRate().compareTo(TEST_WAGE)== 0);
        assertEquals(testPeriod.getHoursWorked(), TEST_HOURS);

    }

    @Test
    public void testPayCalculations() {
        BigDecimal expectedGrossPay = TEST_WAGE.multiply(TEST_HOURS);
        BigDecimal expectedTaxesWithheld = expectedGrossPay.multiply(new BigDecimal(PayPeriod.taxRate));
        assertTrue("test the gross pay: testperiod", testPeriod.grossPay().equals(expectedGrossPay));
        assertTrue("test the tax rate", testPeriod.taxesWithheld().equals(expectedTaxesWithheld));
        assertTrue("test take home pay", testPeriod.takeHomePay().equals(expectedGrossPay.subtract(expectedTaxesWithheld)));
    }


}
