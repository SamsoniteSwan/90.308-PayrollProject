package com.bluelight.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * EmployeeTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/5/2016
 */
public class EmployeeTest {

    public static final Calendar birthDayCalendar = Calendar.getInstance();

    static {
        birthDayCalendar.set(1990, Calendar.JANUARY, 12);
    }

    public static final String status = "active";
    public static final String stringId = "012345";
    public static final String firstName = "Penelope";
    public  static final String lastName = "Cruz";
    public static final Timestamp birthDate = new Timestamp(birthDayCalendar.getTimeInMillis());

    private Employee testEmployee;

    /**
     * Testing helper method for generating Employee test data
     *
     * @return an Employee object that uses static constants for data.
     */
    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId(stringId);
        employee.setBirthDate(birthDate);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setStatus(status);

        return employee;
    }

    @Before
    public void setup() {
        testEmployee = createEmployee();
    }

    @Test
    public void testPersonGettersAndSetters() {
        assertEquals("employeeId matches", stringId, testEmployee.getEmployeeId());
        assertEquals("first name matches", firstName, testEmployee.getFirstName());
        assertEquals("last name matches", lastName, testEmployee.getLastName());
        assertEquals("birthday matches", birthDate, testEmployee.getBirthDate());
        assertEquals("status matches", status, testEmployee.getStatus());
    }

    @Test
    public void testCollectionGettersAndSetters() {
        // Add a number of mock PayPeriods
        PayPeriod mockPeriod = Mockito.mock(PayPeriod.class);
        when(mockPeriod.getVacationUsed()).thenReturn(new BigDecimal("2.00"));
        when(mockPeriod.vacationEarned()).thenReturn(new BigDecimal("3.00"));
        ArrayList<PayPeriod> mockPeriodList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockPeriodList.add(mockPeriod);
        }
        testEmployee.setPayPeriods(mockPeriodList);
        assertTrue("employee has " + mockPeriodList.size(),
                testEmployee.getPayPeriods().size() == 5);
        assertTrue("vacation balance equals" + testEmployee.getVacationBalance(),
                testEmployee.getVacationBalance().compareTo(new BigDecimal("5.00"))==0);
    }

    @Test
    public void testEquals() {
        assertTrue("both employee instances are the same",
                testEmployee.equals(createEmployee()));
    }

    @Test
    public void testHashCode() {
        assertTrue("employee has hash value", testEmployee.hashCode() != 0);
    }

    @Test
    public void testToString() {
        assertTrue("toString has first name", testEmployee.toString().contains(firstName));
        assertTrue("toString has last name", testEmployee.toString().contains(lastName));
        assertTrue("toString has birthdate", testEmployee.toString().contains(birthDate.toString()));
    }
}
