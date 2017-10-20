package eCare.model.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public class ContractDAO implements DAOInterface <Contract, Integer> {

    private Session currentSession;

    private Transaction currentTransaction;

    public ContractDAO() {
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

    public void persist(Contract entity) {
        getCurrentSession().save(entity);
    }

    public void update(Contract entity) {
        getCurrentSession().update(entity);
    }

    public Contract findById(Integer id) {
        Contract contract = (Contract) getCurrentSession().get(Contract.class, id);
        return contract;
    }

    public void delete(Contract entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Contract> findAll() {
        List<Contract> contracts = (List<Contract>) getCurrentSession().createQuery("from Contract").list();
        return contracts;
    }

    public void deleteAll() {
        List<Contract> entityList = findAll();
        for (Contract entity : entityList) {
            delete(entity);
        }
    }
}
