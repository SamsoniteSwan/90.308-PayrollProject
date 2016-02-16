package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;

import com.bluelight.model.Person;
import com.bluelight.model.WorkDay;
import com.bluelight.utils.DatabaseUtils;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * DatabasePersonService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 10/11/2015
 */
public class DatabaseEmployeeService implements EmployeeService {

    @Override
    public List<Employee> getEmployees() throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        List<Employee> result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(Employee.class);

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
            throw new ServiceException("Could not get Person data. " + e.getMessage(), e);
        } finally {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        }

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
            //criteria.add(Restrictions.eq("employeeId", employee));
            /**
             * NOTE criteria.list(); generates unchecked warning so SuppressWarnings
             * is used - HOWEVER, this about the only @SuppressWarnings I think it is OK
             * to suppress them - in almost all other cases they should be fixed not suppressed
             */
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
    }

    @Override
    public List<WorkDay> getWorkdays(Employee employee) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        List<WorkDay> days = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(WorkDay.class);
            criteria.add(Restrictions.eq("employee", employee));

            /**
             * NOTE criteria.list(); generates unchecked warning so SuppressWarnings
             * is used - HOWEVER, this about the only @SuppressWarnings I think it is OK
             * to suppress them - in almost all other cases they should be fixed not suppressed
             */
            /* Favorites diabled until proper substitute determined */
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
    }

}
