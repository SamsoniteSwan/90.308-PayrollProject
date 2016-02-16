package com.bluelight.services;

import com.bluelight.model.WorkDay;

import java.util.List;

/**
 * EmployeeService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/06/2016
 */
public interface WorkdayService {


    /**
     * Get a list of all WorkDay records.
     *
     * @return a list of WorkDay instances.
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<WorkDay> getWorkday() throws ServiceException;

    /**
     * Add a WorkDay or change an existing one.
     *
     * @param date a WorkDay object to either update or create
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    void addOrUpdateWorkday(WorkDay date) throws ServiceException;

}
