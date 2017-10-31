package eCare.model.DAO;

import eCare.model.PO.BlockingFeatures;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 29.10.2017.
 */
public class BlockingFeaturesDAO implements DAOInterface <BlockingFeatures, Integer> {


    private Session currentSession;

    private Transaction currentTransaction;

    public BlockingFeaturesDAO() {
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

    public void persist(BlockingFeatures entity) {
        getCurrentSession().save(entity);
    }

    public void update(BlockingFeatures entity) {
        getCurrentSession().update(entity);
    }

    public BlockingFeatures findById(Integer id) {
        BlockingFeatures blockingFeatures = (BlockingFeatures) getCurrentSession().get(BlockingFeatures.class, id);
        return blockingFeatures;
    }

    public void delete(BlockingFeatures entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<BlockingFeatures> findAll() {
        List<BlockingFeatures> blockingFeatures = (List<BlockingFeatures>) getCurrentSession().createQuery("from BlockingFeatures").list();
        return blockingFeatures;
    }

    public void deleteAll() {
        List<BlockingFeatures> entityList = findAll();
        for (BlockingFeatures entity : entityList) {
            delete(entity);
        }
    }
}
