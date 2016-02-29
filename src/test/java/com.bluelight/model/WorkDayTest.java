package com.bluelight.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * WorkDayTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/14/2016
 */
public class WorkDayTest {

    public static final BigDecimal STANDARDDAY_HOURS = new BigDecimal(8.00);
    public static final Calendar startCalendar = Calendar.getInstance();

    static {
        startCalendar.set(2016, Calendar.JANUARY, 2);
    }
    public static final Timestamp FIRSTTEST_DAY = new Timestamp(startCalendar.getTimeInMillis());

    public static WorkDay createStandardWorkDay() {
        WorkDay day = new WorkDay();
        day.setEmployee(EmployeeTest.createEmployee());
        //day.setPeriod(PayPeriodTest.samplePeriod());
        day.setHoursWorked(STANDARDDAY_HOURS);
        day.setDate(FIRSTTEST_DAY);
        day.setVacationUsed(new BigDecimal(2.0));
        return day;
    }

    @Test
    public void testWorkdayGettersAndSetters() {
        WorkDay day = createStandardWorkDay();
        assertEquals("8 hours worked", STANDARDDAY_HOURS, day.getHoursWorked());
        assertEquals("Date is correct", FIRSTTEST_DAY, day.getDate());

    }
}
