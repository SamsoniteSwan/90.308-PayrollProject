
package com.bluelight.apps;

import com.bluelight.apps.BasicPayrollApplication;
import com.bluelight.model.Employee;
import com.bluelight.services.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertNotNull;


/**
 * PayrollApplicationTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/8/2016
 */
public class PayrollApplicationTest {

    private BasicPayrollApplication app;
    private EmployeeService employeeService;

    @Before
    public void setUp() {
        employeeService = mock(EmployeeService.class);
        app = new BasicPayrollApplication(employeeService);
    }

    @Test
    public void testValidConstruction() {

        assertNotNull(app);
    }



}
