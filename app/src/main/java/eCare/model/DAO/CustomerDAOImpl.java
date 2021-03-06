package eCare.model.DAO;

import eCare.model.PO.Customer;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */

@Repository("secondCustomerDAO")
public class CustomerDAOImpl extends AbstractDao <Integer, Customer> implements CustomerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

    public Customer findById(int id) {
        Customer user = getByKey(id);
        if (user != null) {
            Hibernate.initialize(user.getUserProfiles());
        }
        return user;
    }

    public Customer findBySSO(String sso) {
        logger.info("SSO : {}", sso);
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("ssoId", sso));
        Customer user = (Customer) crit.uniqueResult();
        if (user != null) {
            Hibernate.initialize(user.getUserProfiles());
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    public List<Customer> findAllUsers() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("name"));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
        List<Customer> users = (List<Customer>) criteria.list();

        // No need to fetch userProfiles since we are not showing them on list page. Let them lazy load.
        // Uncomment below lines for eagerly fetching of userProfiles if you want.
		/*
		for(User user : users){
			Hibernate.initialize(user.getUserProfiles());
		}*/
        return users;
    }

    public void save(Customer user) {
        persist(user);
    }

    public void deleteBySSO(String sso) {
        Criteria crit = createEntityCriteria();
        crit.add(Restrictions.eq("ssoId", sso));
        Customer user = (Customer) crit.uniqueResult();
        delete(user);
    }

    public List<Customer> findByName(String name) {
        logger.info("CustomerName : {}", name);
        Query query = sessionFactory.getCurrentSession().createQuery("select c from Customer c where c.name = :name");
        query.setParameter("name", name);
        List results = query.list();
        return results;
    }

    public List<Customer> findByTelNumber(String telNumber){
        logger.info("CustomerTelNumber : {}", telNumber);
        Query query = sessionFactory.getCurrentSession().createQuery("select t from Customer t where t.telNumber = :telNumber");
        query.setParameter("telNumber", telNumber);
        List result = query.list();
        return result;
    }
}
