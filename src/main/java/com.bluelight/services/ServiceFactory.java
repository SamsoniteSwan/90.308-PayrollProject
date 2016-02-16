package com.bluelight.services;

/**
 * ServiceFactory
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/7/2016
 */
public class ServiceFactory {

    /**
     * Prevent instantiations
     */
    private ServiceFactory() {}

    /**
     * @return get a DatabasePayrollService instance
     */
    public static PayrollService getDBPayrollServiceInstance() { return new DatabasePayrollService(); }

    /**
     * @return get a DatabaseEmployeeService instance
     */
    public static EmployeeService getDBEmployeeServiceInstance() { return new DatabaseEmployeeService(); }

    /**
     * @return get DatabaseWorkdayService instance
     */
    public static WorkdayService getDBWorkdayServiceInstance() { return new DatabaseWorkdayService(); }
}
