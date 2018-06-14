package eCare.model.DAO;

import eCare.model.PO.Contract;
import eCare.model.PO.Tarif;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
@Repository("tarifDAO")
public class TarifDAO  implements DAOInterface <Tarif, Integer>{

    static final Logger logger = LoggerFactory.getLogger(TarifDAO.class);

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void test() {
        System.out.print(applicationContext);
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public void persist(Tarif entity) {
        getSession().save(entity);
    }

    public void update(Tarif entity) {
        getSession().update(entity);
    }

    public Tarif findById(Integer id) {
        Tarif tarif = (Tarif) getSession().get(Tarif.class, id);
        return tarif;
    }

    public List<Tarif> findByName(String name) {
        logger.info("TarifName : {}", name);
        Query query = sessionFactory.getCurrentSession().createQuery("select t from Tarif t where t.name = :name");
        query.setParameter("name", name);
        List results = query.list();
        return  results;
    }

//    public void deleteById(Integer id){
//        Tarif tarif = getSession().find(Tarif.class, id);
//        delete(tarif);
//    }

    public void delete(Tarif entity) {
        getSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Tarif> findAll() {
        List<Tarif> tarifs = (List<Tarif>) getSession().createQuery("from Tarif").list();
        return tarifs;
    }

    public void deleteAll() {
        List<Tarif> entityList = findAll();
        for (Tarif entity : entityList) {
            delete(entity);
        }
    }
}
