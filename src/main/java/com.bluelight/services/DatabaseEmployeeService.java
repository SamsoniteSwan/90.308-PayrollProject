package com.bluelight.services;

import com.bluelight.model.Employee;
import com.bluelight.model.PayPeriod;

import com.bluelight.model.Person;
import com.bluelight.utils.DatabaseUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;


/**
 * DatabasePersonService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 10/11/2015
 */
public class DatabaseEmployeeService implements EmployeeService {

    @Override
    public List<Employee> getEmployee() throws ServiceException {
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
    public void addOrUpdateEmployee(Employee person) throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(person);
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
        List<PayPeriod> periods = new ArrayList<>();
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(PayPeriod.class);
            criteria.add(Restrictions.eq("employee", employee));
            /**
             * NOTE criteria.list(); generates unchecked warning so SuppressWarnings
             * is used - HOWEVER, this about the only @SuppressWarnings I think it is OK
             * to suppress them - in almost all other cases they should be fixed not suppressed
             */
            /* Favorites diabled until proper substitute determined
            List<Favorite> list = criteria.list();
            for (Favorite favorite : list) {
                periods.add(favorite.getCompany());
            }
            */
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
        return periods;
    }

    @Override
    public void addPayPeriod(PayPeriod payPeriod, Employee employee) throws ServiceException {
        Session session =  DatabaseUtils.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(payPeriod);
            transaction.commit();
            transaction = session.beginTransaction();
            /*
            Favorite favorite = new Favorite();
            favorite.setCompany(company);
            favorite.setPerson(person);
            session.saveOrUpdate(favorite);
            transaction.commit();
            */

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
