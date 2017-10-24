package eCare.model.DAO;

import eCare.model.PO.Option;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public class OptionDAO implements DAOInterface <Option, Integer> {

    private Session currentSession;

    private Transaction currentTransaction;

    public OptionDAO() {
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

    public void persist(Option entity) {
        getCurrentSession().save(entity);
    }

    public void update(Option entity) {
        getCurrentSession().update(entity);
    }

    public Option findById(Integer id) {
        Option option = getCurrentSession().get(Option.class, id);
        return option;
    }

    public void delete(Option entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Option> findAll() {
        List<Option> options = (List<Option>) getCurrentSession().createQuery("from Option").list();
        return options;
    }

    public void deleteAll() {
        List<Option> entityList = findAll();
        for (Option entity : entityList) {
            delete(entity);
        }
    }
}
