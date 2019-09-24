package server;

import entities.EntityModel;
import entities.IEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

public class Service  {
    private IEntity entity;

    public Service(IEntity e){
        entity=e;
    }

    public void save() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(entity);
        tx1.commit();
        session.close();
    }

    public void update() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entity);
        tx1.commit();
        session.close();
    }

    public void delete() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(entity);
        tx1.commit();
        session.close();
    }

    public void reread() {

    }
    public static <T> List<T> executeStatementSelect (String sql){
        SessionFactory sf = HibernateSessionFactory.getSessionFactory();
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery(sql);
        List<T> result = (List<T>) query.getResultList();
        session.close();
        return  result;
    }

    /*public static <T  extends EntityModel> T findById(Class clazz, int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        T e = (T)session.get(clazz,id);
        session.close();
        return  e;
    }*/

    public static <T  extends EntityModel> T findById(Class clazz, Serializable id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        T e = (T)session.get(clazz, id);
        session.close();
        return  e;
    }

    /*public static <T  extends EntityModel> T findByStr(Class clazz, String id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        //Object key = session.getIdentifier()
        T e = (T)session.get(clazz, id);
        session.close();
        return  e;
    }*/
}