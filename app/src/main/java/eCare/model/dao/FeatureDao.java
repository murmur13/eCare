package eCare.model.dao;

import eCare.model.po.Feature;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
@Repository("featureDao")
public class FeatureDao implements DaoInterface<Feature, Integer> {

    static final Logger logger = LoggerFactory.getLogger(FeatureDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public void persist(Feature entity) {
        getSession().save(entity);
    }

    public void update(Feature entity) {
        Session session = getSession();
        session.clear();
        session.update(entity);
        session.flush();
    }

    public Feature findById(Integer id) {
        Feature feature = (Feature) getSession().get(Feature.class, id);
        return feature;
    }

    public List<Feature> findByName(String name) {
        logger.info("FeatureName : {}", name);
        Query query = sessionFactory.getCurrentSession().createQuery("select f from Feature f where f.featureName = :name");
        query.setParameter("name", name);
        List results = query.list();
        return  results;
    }

    public void delete(Feature entity) {
        getSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Feature> findAll() {
        List<Feature> features = (List<Feature>) getSession().createQuery("from Feature").list();
        return features;
    }

    public List<Feature> findAllBlockingFeatures() {
        List<Feature> blockingFeatures = (List<Feature>) getSession().createQuery("select f from Feature f join f.blockingFeatures").list();
        return blockingFeatures;
    }

    public List<Feature> findAllRequiredFeatures(){
        List<Feature> requiredFeatures = (List<Feature>) getSession().createQuery("select f from Feature f join f.requiredFeatures").list();
        return requiredFeatures;
    }

    public void deleteAll() {
        List<Feature> entityList = findAll();
        for (Feature entity : entityList) {
            delete(entity);
        }
    }

    public List<Feature> findFeatureByTarif(Integer tarifId) {
        logger.info("tarifId : {}", tarifId);
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("select f from Feature f join f.featureTarifs ft where ft.tarifId = :tarifId");
        query.setParameter("tarifId", tarifId);
        List results = query.list();

        return  results;
    }

    public List<Feature> findFeatureByContract(Integer contract) {
        logger.info("contractId : {}", contract);
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("select f from Feature f join f.featureContracts fc where fc.contractId= :contract");
        query.setParameter("contract", contract);
        List results = query.list();
        return  results;
    }
}
