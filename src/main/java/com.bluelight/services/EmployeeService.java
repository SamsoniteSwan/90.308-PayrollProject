package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;
import org.joda.time.DateTime;

import java.util.List;

/**
 * EmployeeService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 2/06/2016
 */
public interface EmployeeService {


    /**
     * Get a list of all employees
     *
     * @return a list of Employee instances
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<Employee> getEmployees() throws ServiceException;

    /**
     * Get all employees with a given Last Name.
     *
     * @param employeeLast string last name value
     * @return a list of Employees
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<Employee> getEmployeesByLast(String employeeLast) throws ServiceException;

    /**
     * Get the employee with the given ID.
     *
     * @param employeeId string of distict ID.
     * @return single employee instance
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    Employee getEmployeeById(String employeeId) throws ServiceException;

    /**
     * Add a new employee or update an existing employee's data
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
     * Get a list of all a person's pay period records.
     *
     * @return a list of pay periods
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    List<PayPeriod> getPayPeriods(String employeeId, DateTime from, DateTime until) throws ServiceException;

    /**
     * Assign a pay period to an employee.
     *
     * @param payPeriod  The payperiod to assign
     * @param employee The person to assign the payperiod too.
     * @throws ServiceException if a service can not read or write the requested data
     *                                    or otherwise perform the requested operation.
     */
    void addPayPeriod(PayPeriod payPeriod, Employee employee) throws ServiceException;

}
