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
     * @return get a DatabaseEmployeeService instance
     */
    public static EmployeeService getDBEmployeeServiceInstance() { return new DatabaseEmployeeService(); }


    /**
     * @return get a CSVImportService instance
     */
    public static CSVImportService getCsvImportServiceInstance() { return new CSVImportService(); }
}
