package com.bluelight.services;

import com.bluelight.model.PayPeriod;
import com.bluelight.utils.DatabaseUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * DatabasePayPeriodService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/4/2016
 */
public class DatabasePayPeriodService implements PayPeriodService {
    @Override
    public List<PayPeriod> getAllPayPeriods() throws ServiceException {
        Session session = DatabaseUtils.getSessionFactory().openSession();
        List<PayPeriod> result = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(PayPeriod.class);

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
}
