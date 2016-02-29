package com.bluelight.services;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * ServiceFactoryTest
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/9/2016
 */
public class ServiceFactoryTest {

    @Test
    public void testGetDBEmployeeInstance() {
        EmployeeService payrollService = ServiceFactory.getDBEmployeeServiceInstance();
        assertNotNull(payrollService);
    }

}
