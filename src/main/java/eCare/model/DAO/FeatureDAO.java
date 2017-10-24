package eCare.model.DAO;

import eCare.model.PO.Feature;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public class FeatureDAO implements DAOInterface <Feature, Integer> {

    private Session currentSession;

    private Transaction currentTransaction;

    public FeatureDAO() {
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        SessionFactory session = new Configuration().configure().buildSessionFactory();
        return session;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void persist(Feature entity) {
        getCurrentSession().save(entity);
    }

    public void update(Feature entity) {
        getCurrentSession().update(entity);
    }

    public Feature findById(Integer id) {
        Feature feature = getCurrentSession().get(Feature.class, id);
        return feature;
    }

    public void delete(Feature entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Feature> findAll() {
        List<Feature> featureList = (List<Feature>) getCurrentSession().createQuery("from Feature").list();
        return featureList;
    }

    public void deleteAll() {
        List<Feature> entityList = findAll();
        for (Feature entity : entityList) {
            delete(entity);
        }
    }
}
