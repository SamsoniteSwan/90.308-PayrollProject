package com.bluelight.services;

import com.bluelight.utils.DatabaseUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.usertype.ParameterizedType;


import java.util.ArrayList;
import java.util.List;

/**
 * GenericDatabaseService
 *
 * @author Jeremy Swanson (jeremy at jlswanson.com)
 * @version 1, 3/8/2016
 */
public class GenericDatabaseService<G> implements GenericService {

    private Session session;
    private Class<G> persistantClass;

    public GenericDatabaseService() {
        this.persistantClass = (Class<G>) (getClass().
                getGenericSuperclass());
        //this.session = DatabaseUtils.getSessionFactory().getCurrentSession();
    }

    public Class<G> getPersistantClass() {
        return persistantClass;
    }

    public void setSession(Session s) {
        this.session = s;
    }

    protected Session getSession() {
        if (session == null)
            session = DatabaseUtils.getSessionFactory().openSession();
        return session;
    }

    @Override
    public List getAll() {
        Session sess = DatabaseUtils.getSessionFactory().openSession();
        List result;
        Criteria criteria = sess.createCriteria(getPersistantClass());

        result = criteria.list();
        sess.close();
        return result;
    }
}
