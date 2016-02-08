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
     * @return get a <CODE>DatabaseStockService</CODE> instance
     */
    public static PayrollService getDBPayrollServiceInstance() { return new DatabasePayrollService(); }

}
