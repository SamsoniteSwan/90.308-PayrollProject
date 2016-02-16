package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;
import com.bluelight.model.Person;
import com.bluelight.model.WorkDay;

import java.util.List;
import java.util.Set;

/**
 * EmployeeService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/06/2016
 */
public interface EmployeeService {


    /**
     * Get a list of all people
     *
     * @return a list of Person instances
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<Employee> getEmployees() throws ServiceException;

    /**
     * Add a new person or update an existing Person's data
     *
     * @param employee a person object to either update or create
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    void addOrUpdateEmployee(Employee employee) throws ServiceException;

    /**
     * Get a list of all a person's pay period records.
     *
     * @return a list of pay periods
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<PayPeriod> getPayPeriods(Employee employee) throws ServiceException;

    /**
     * Assign a pay period to a person.
     *
     * @param payPeriod  The payperiod to assign
     * @param employee The person to assign the payperiod too.
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    void addPayPeriod(PayPeriod payPeriod, Employee employee) throws ServiceException;

    List<WorkDay> getWorkdays(Employee employee) throws ServiceException;

    void addWorkDay(Employee employee, WorkDay day) throws ServiceException;
}
