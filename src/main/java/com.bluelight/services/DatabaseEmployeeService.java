package com.bluelight.services;

import com.bluelight.model.*;

import com.bluelight.utils.DatabaseUtils;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * DatabasePersonService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 10/11/2015
 */
public class DatabaseEmployeeService implements EmployeeService {

    /**
     * get ALL employees in a list
     * @return list of employees
     * @throws ServiceException
     */
    @Override
    public List<Employee> getEmployees() throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        List<Employee> result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(Employee.class);

            result = criteria.list();

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // close transaction
            }
            throw new ServiceException("Could not get employee data. " + e.getMessage(), e);
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }

        session.close();
        return result;
    }

    /**
     * Save or update an employee in the database.
     *
     * @param employee an Employee object to either update or create
     * @throws ServiceException
     */
    @Override
    public void addOrUpdateEmployee(Employee employee) throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(employee);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // close transaction
            }
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }
    }

    /**
     * Get an employee's pay periods that encompass the given dates.
     *
     * @param employeeId String Id of employee to search
     * @param from start date for search
     * @param until end date for search
     * @return List of all PayPeriods between and including the from and until dates
     * @throws ServiceException
     */
    @Override
    public List<PayPeriod> getPayPeriods(String employeeId, DateTime from, DateTime until) throws ServiceException {
        Employee ee = getEmployeeById(employeeId);
        List<PayPeriod> all = getPayPeriods(ee);
        List<PayPeriod> result = new ArrayList<>();
        Timestamp start = new Timestamp(from.getMillis());
        Timestamp end = new Timestamp(until.getMillis());
        for(PayPeriod pp : all) {
            if ((pp.getStartDay().after(start) && pp.getStartDay().before(end)) ||
                    (pp.getEndDay().after(start) && pp.getEndDay().before(end))) {
                result.add(pp);
            }
        }
        return result;
    }


    /**
     * Get a list of all the PayPeriods for a given employee from the database.
     *
     * @param employee Employee to search
     * @return List of PayPeriods
     * @throws ServiceException
     */
    @Override
    public List<PayPeriod> getPayPeriods(Employee employee) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        List<PayPeriod> result = new ArrayList<>();

        try {
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(PayPeriod.class);
            Criteria suppCriteria = criteria.createCriteria("employee");
            suppCriteria.add(Restrictions.eq("employeeId", employee.getEmployeeId()));

            result = criteria.list();
            //transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // close transaction
            }
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }
        session.close();
        return result;
    }

    /**
     * Add a pay period to the database for an employee.
     *
     * @param payPeriod  The payperiod to assign
     * @param employee The person to assign the payperiod too.
     * @throws ServiceException
     */
    @Override
    public void addPayPeriod(PayPeriod payPeriod, Employee employee) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            employee.setPayPeriods(new ArrayList<>());
            employee.getPayPeriods().add(payPeriod);
            session.saveOrUpdate(payPeriod);
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // close transaction
            }
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }
        session.close();
    }

    /**
     * Retrieve an employee object with the provided Id from the database.
     *
     * @param eeID String of employee's Id
     * @return employee object
     * @throws ServiceException
     */
    public Employee getEmployeeById(String eeID) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Employee employee = (Employee) session.get(Employee.class, eeID);
        // Could return null.
        if (employee == null) {
            employee = new Employee();
            employee.setEmployeeId(eeID);
        }
        session.close();
        return employee;
    }

    /**
     * gets a list of all employees with the last name.
     *
     * @param lastName Last name of employee(s) to search
     * @return List of employees
     * @throws ServiceException
     */
    public List<Employee> getEmployeesByLast(String lastName) throws ServiceException {
        List<Employee> result = new ArrayList<>();
        List<Employee> all = this.getEmployees();
        for (Employee ee : all) {
            if (ee.getLastName().compareTo(lastName)==0) {
                result.add(ee);
            }
        }
        return result;
    }

    /**
     *
     * @param employee employee to get values for
     * @param from
     * @param until
     * @return
     * @throws ServiceException
     */
    public BigDecimal getTotalPay(Employee employee, DateTime from, DateTime until) throws ServiceException {
        BigDecimal result = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

        List<PayPeriod> periods = getPayPeriods(employee);
        // loop through pay periods
        for (PayPeriod period : periods) {
            result = result.add(period.grossPay());
        }
        // round decimal value to whole cents
        result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

        return  result;
    }

}
