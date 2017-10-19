package eCare.model.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class CustomerDAO implements DAOInterface <Customer, Integer> {

    private Session currentSession;

    private Transaction currentTransaction;

    public CustomerDAO() {

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

    public void persist(Customer entity) {
        getCurrentSession().save(entity);
    }

    public void update(Customer entity) {
        getCurrentSession().update(entity);
    }

    public Customer findById(Integer id) {
        Customer customer = (Customer) getCurrentSession().get(Customer.class, id);
        return customer;
    }

    public void delete(Customer entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Customer> findAll() {
        List<Customer> customers = (List<Customer>) getCurrentSession().createQuery("from Customer").list();
        return customers;
    }

    public void deleteAll() {
        List<Customer> entityList = findAll();
        for (Customer entity : entityList) {
            delete(entity);
        }
    }
}
