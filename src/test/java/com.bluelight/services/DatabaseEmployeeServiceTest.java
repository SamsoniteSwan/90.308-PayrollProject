package com.bluelight.services;

import com.bluelight.model.*;
import com.bluelight.utils.DatabaseUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * DatabaseEmployeeServiceTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 2, 2/23/2016
 */
public class DatabaseEmployeeServiceTest {

    private EmployeeService employeeService;
    private Employee testEmployee;
    public static final DateTime min = new DateTime(1900,1,1,1,1);
    public static final DateTime max = new DateTime(2100,1,1,1,1);

    /**
     * Initializes the database from the initialization file
     * @throws Exception
     */
    private void initDb() throws Exception {
        DatabaseUtils.initializeDatabase(DatabaseUtils.initializationFile);
    }

    // do not assume db is correct
    @Before
    public void setUp() throws Exception {
        // we could also copy db state here for later restore before initializing
        initDb();
        employeeService = ServiceFactory.getDBEmployeeServiceInstance();
        testEmployee = EmployeeTest.createEmployee();
    }

    @Test
    public void addOrUpdateEmployeeTest() throws ServiceException {

        employeeService.addOrUpdateEmployee(testEmployee);
        List<Employee> eeList = employeeService.getEmployees();

        boolean found = false;

        for (Employee employee : eeList) {
            if (employee.getEmployeeId().compareTo(EmployeeTest.stringId)== 0) {
                found = true;
                break;
            }
        }

        assertTrue("listsize=" + eeList.size(), eeList.size() == 3);
        assertTrue("Employee added and found", found);
    }

    @Test
    public void addPayPeriodTest() throws ServiceException {

        employeeService.addOrUpdateEmployee(testEmployee);
        employeeService.addPayPeriod(PayPeriodTest.samplePeriod(), testEmployee);
        List<PayPeriod> list = employeeService.getPayPeriods(testEmployee);
        assertTrue("testEmployee has 1 pay period", list.size() == 1);

    }

    @Test
    public void addOrUpdateWorkdayTest() throws ServiceException {


        employeeService.addOrUpdateEmployee(testEmployee);
        List<WorkDay> startList = employeeService.getWorkdays(testEmployee.getEmployeeId(),
                min,
                max);
        employeeService.addOrUpdateWorkday(WorkDayTest.createStandardWorkDay(), testEmployee);
        List<WorkDay> endList = employeeService.getAllWorkdays(testEmployee);


        assertTrue("startct=" + startList.size() + "; endct=" + endList.size(), startList.size() < endList.size());

    }

    /*
    @Test
    public void testGetAll() {
        List<Employee> eelist = employeeService.getAll();
        assertTrue("list size=" + eelist.size(), eelist.size() == 2);

    }
    */
}
