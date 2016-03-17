package com.bluelight.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * PayPeriodTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
public class PayPeriodTest {

    public static final Calendar periodCalendar = Calendar.getInstance();

     static {
        periodCalendar.set(2015, Calendar.FEBRUARY, 2);
    }

    private static BigDecimal TEST_WAGE = new BigDecimal("10.00");
    private static BigDecimal TEST_HOURS = new BigDecimal("40.00");
    private static BigDecimal TEST_VACA = new BigDecimal("0.00");

    private PayPeriod testPeriod;

    public static PayPeriod samplePeriod() {
        PayPeriod period = new PayPeriod();
        period.setEmployee(EmployeeTest.createEmployee());
        period.setHourlyRate(TEST_WAGE);
        period.setHoursWorked(TEST_HOURS);
        period.setVacationUsed(TEST_VACA);
        Timestamp start = new Timestamp(periodCalendar.getTimeInMillis());
        period.setStartDay(start);
        periodCalendar.add(Calendar.DAY_OF_MONTH, 7);
        Timestamp end = new Timestamp(periodCalendar.getTimeInMillis());
        period.setEndDay(end);
        return period;
    }

    @Before
    public void setup() {
        testPeriod = samplePeriod();
        //testPeriod.setHoursWorked(TEST_HOURS);
    }

    @Test
    public void testInstatiation() {
        assertTrue(testPeriod.getHourlyRate().compareTo(TEST_WAGE)== 0);
        assertTrue(testPeriod.getStartDay() != testPeriod.getEndDay());
        //assertEquals(testPeriod.getHoursWorked(), TEST_HOURS);

    }


    @Test
    public void testPayCalculations() {
        BigDecimal expectedGrossPay = TEST_WAGE.multiply(TEST_HOURS);
        BigDecimal expectedTaxesWithheld = expectedGrossPay.multiply(PayPeriod.TAX_RATE);
        assertTrue("test the gross pay: testperiod", testPeriod.grossPay().equals(expectedGrossPay));
        assertTrue("test the tax rate", testPeriod.taxesWithheld().equals(expectedTaxesWithheld));
        assertTrue("test take home pay", testPeriod.takeHomePay().equals(expectedGrossPay.subtract(expectedTaxesWithheld)));
    }


}
