package eCare.model.dao;

import eCare.model.po.Contract;
import eCare.model.po.Customer;
import eCare.model.po.Tarif;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */

@Repository("contractDao")
public class ContractDao implements DaoInterface<Contract, Integer> {

    static final Logger logger = LoggerFactory.getLogger(ContractDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void persist(Contract entity) {
        getSession().save(entity);
    }

    public void update(Contract entity) {
        Session session = getSession();
        session.clear();
        session.update(entity);
        session.flush();
    }

    public Contract findById(Integer id) {
        Contract contract = (Contract) getSession().get(Contract.class, id);
        return contract;
    }

    public List<Contract> findByPhone(String telNumber) {
        logger.info("ContractTelNumber : {}", telNumber);
        Query query = sessionFactory.getCurrentSession().createQuery("select c from Contract c where c.tNumber = :telNumber");
        query.setParameter("telNumber", telNumber);
        List results = query.list();
        return results;
    }

    public List<Contract> findByCustomerId(Customer customerId) {
        logger.info("ContractCustomerId : {}", customerId);
        Query query = sessionFactory.getCurrentSession().createQuery("select c from Contract c where c.customer = :customerId");
        query.setParameter("customerId", customerId);
        List results = query.list();
        return results;
    }

    public List<Contract> findContractByTarif(Tarif tarifId) {
        logger.info("TarifId : {}, tarifId");
        Query query = sessionFactory.getCurrentSession().createQuery("select c from Contract c where c.tarif = :tarifId");
        query.setParameter("tarifId", tarifId);
        List result = query.list();
        return result;
    }

    public void delete(Contract entity) {
        getSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Contract> findAll() {
        List<Contract> contracts = (List<Contract>) getSession().createQuery("from Contract").list();
        return contracts;
    }

    public void deleteAll() {
        List<Contract> entityList = findAll();
        for (Contract entity : entityList) {
            delete(entity);
        }
    }
}
