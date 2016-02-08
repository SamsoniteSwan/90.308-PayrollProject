package com.bluelight.model;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    public static final String firstName = "Penelope";
    public  static final String lastName = "Cruz";
    public static final Timestamp birthDate = new Timestamp(birthDayCalendar.getTimeInMillis());

    private Employee testEmployee;

    /**
     * Testing helper method for generating Person test data
     *
     * @return an Employee object that uses static constants for data.
     */
    public static Employee createEmployee() {
        Employee employee = new Employee();
        employee.setBirthDate(birthDate);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        return employee;
    }

    @Before
    public void setup() {
        testEmployee = createEmployee();
    }

    @Test
    public void testPersonGettersAndSetters() {
        assertEquals("first name matches", firstName, testEmployee.getFirstName());
        assertEquals("last name matches", lastName, testEmployee.getLastName());
        assertEquals("birthday matches", birthDate, testEmployee.getBirthDate());
    }

    @Test
    public void testEquals() {
        assertTrue("both quotes are equal", testEmployee.equals(createEmployee()));
    }

    @Test
    public void testHashCode() {
        assertTrue("quote has hash value", testEmployee.hashCode() != 0);
    }

    @Test
    public void testToString() {
        assertTrue("toString has first name", testEmployee.toString().contains(firstName));
        assertTrue("toString has last name", testEmployee.toString().contains(lastName));
        assertTrue("toString has birthdate", testEmployee.toString().contains(birthDate.toString()));
    }
}
