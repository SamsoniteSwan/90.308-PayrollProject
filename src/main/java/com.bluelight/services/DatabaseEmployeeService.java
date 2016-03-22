package com.bluelight.services;

import com.bluelight.model.*;

import com.bluelight.utils.DatabaseConnectionException;
import com.bluelight.utils.DatabaseUtils;
import com.bluelight.utils.Interval;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * DatabasePersonService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 10/11/2015
 */
public class DatabaseEmployeeService implements EmployeeService, WorkdayService {

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
        return result;
    }

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

    @Override
    public List<WorkDay> getWorkdays(String employeeId, DateTime start, DateTime end) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        List<WorkDay> days = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(WorkDay.class);
            Criteria suppCriteria = criteria.createCriteria("employee");
            suppCriteria.add(Restrictions.eq("employeeId", employeeId));
            criteria.add(Restrictions.ge("workday", new Timestamp(start.getMillis())));
            criteria.add(Restrictions.lt("workday", new Timestamp(end.getMillis())));

            List<WorkDay> list = criteria.list();
            for (WorkDay day : list) {
                days.add(day);
            }

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
        return days;
    }

    /**
     * Get all the workdays assigned to the employee in the database.
     *
     * @param employee employee to query
     * @return List of employee's workdays
     * @throws ServiceException
     */
    public List<WorkDay> getAllWorkdays(Employee employee) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        List<WorkDay> days = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(WorkDay.class);
            Criteria suppCriteria = criteria.createCriteria("employee");
            suppCriteria.add(Restrictions.eq("employeeId", employee.getEmployeeId()));

            List<WorkDay> list = criteria.list();
            for (WorkDay day : list) {
                days.add(day);
            }

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
        return days;
    }
    @Override
    public void addWorkDay(Employee employee, WorkDay day) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            employee.setWorkDays(new ArrayList<>());
            employee.getWorkDays().add(day);
            session.saveOrUpdate(day);
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

    public ArrayList<WorkDay> getWorkDays(String employeeId, DateTime from, DateTime until) throws ServiceException {

        ArrayList<WorkDay> workDays = null;
        try {
            Connection connection = DatabaseUtils.getConnection();
            Statement statement = connection.createStatement();

            String fromString = from.toString(PayrollData.dateFormat);
            String untilString = until.toString(PayrollData.dateFormat);

            String queryString = "select * from tblWorkLog where employee = '" + employeeId + "'"
                    + "and workday BETWEEN '" + fromString + "' and '" + untilString + "'";

            ResultSet resultSet = statement.executeQuery(queryString);
            workDays = new ArrayList<>();
            WorkDay previousDay = null;

            while (resultSet.next()) {
                WorkDay currentDay = new WorkDay();
                Timestamp timeStamp = resultSet.getTimestamp("workday");
                BigDecimal work = resultSet.getBigDecimal("hours");
                BigDecimal vaca = resultSet.getBigDecimal("vacationhrs");
                //DateTime time = new DateTime(timeStamp);
                //WorkDay currentDay = new WorkDay(employeeId, timeStamp, work, vaca);

                workDays.add(currentDay);

            }

        } catch (DatabaseConnectionException | SQLException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }
        if (workDays.isEmpty()) {
            throw new ServiceException("There are no workdays for employee ID:" + employeeId);
        }


        return workDays;
    }

    /**
     * Returns true of the currentStockQuote has a date that is later by the time
     * specified in the interval value from the previousStockQuote time.
     *
     * @param endDate   the end time
     * @param interval  the period of time that must exist between previousStockQuote and currentStockQuote
     *                  in order for this method to return true.
     * @param startDate the starting date
     * @return
     */
    private boolean isInterval(DateTime endDate, Interval interval, DateTime startDate) {

        startDate = startDate.plusMinutes(interval.getMinutes());

        return endDate.toInstant().isAfter(startDate.toInstant());
    }

    @Override
    public void addOrUpdateWorkday(WorkDay date, Employee ee) throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ee.setWorkDays(new ArrayList<>());
            ee.getWorkDays().add(date);
            session.saveOrUpdate(date);
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
    @Override
    public List<WorkDay> getWorkday() throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        List<WorkDay> result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(WorkDay.class);

            /**
             * PROFESSOR NOTE
             * criteria.list(); generates unchecked warning so SuppressWarnings
             * is used - HOWEVER, this about the only @SuppressWarnings I think it is OK
             * to suppress them - in almost all other cases they should be fixed not suppressed
             */
            result = criteria.list();

        } catch (HibernateException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();  // close transaction
            }
            throw new ServiceException("Could not get Workday data. " + e.getMessage(), e);
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }

        return result;
    }

    public BigDecimal getTotalPay(Employee employee, DateTime from, DateTime until) throws ServiceException {
        BigDecimal result = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

        List<PayPeriod> periods = getPayPeriods(employee);
        //List<WorkDay> days = getAllWorkdays(employee);
        // loop through pay periods
        for (PayPeriod period : periods) {

            result = result.add(period.grossPay());
            /*
            for (WorkDay day : days) {
                if (period.hasDay(day)) {

                    BigDecimal rate = period.getHourlyRate();
                    BigDecimal hrs = day.getHoursWorked();
                    BigDecimal tmp = hrs.multiply(rate);
                    result = result.add(tmp);
                }
            }
            */
        }
        // round decimal value to whole cents
        result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

        return  result;
    }

    /*
    public List getAll() {
        //Session session = DatabaseUtils.getSessionFactory().openSession();
        GenericDatabaseService gdbs = new GenericDatabaseService<Employee>();

        return gdbs.getAll();
    }
    */
}
