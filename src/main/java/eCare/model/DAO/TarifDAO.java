package eCare.model.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public class TarifDAO  implements DAOInterface <Tarif, Integer>{

    private Session currentSession;

    private Transaction currentTransaction;

    public TarifDAO() {

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

    public void persist(Tarif entity) {
        getCurrentSession().save(entity);
    }

    public void update(Tarif entity) {
        getCurrentSession().update(entity);
    }

    public Tarif findById(Integer id) {
        Tarif tarif = (Tarif) getCurrentSession().get(Tarif.class, id);
        return tarif;
    }

    public void delete(Tarif entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Tarif> findAll() {
        List<Tarif> tarifs = (List<Tarif>) getCurrentSession().createQuery("from Tarif").list();
        return tarifs;
    }

    public void deleteAll() {
        List<Tarif> entityList = findAll();
        for (Tarif entity : entityList) {
            delete(entity);
        }
    }
}
