package eCare.model.services;

import eCare.model.DAO.FeatureDAO;
import eCare.model.PO.Feature;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public class FeatureService {

    private static FeatureDAO featureDAO;

    public FeatureService() {
        featureDAO = new FeatureDAO();
    }

    public void persist(Feature entity) {
        featureDAO.openCurrentSessionwithTransaction();
        featureDAO.persist(entity);
        featureDAO.closeCurrentSessionwithTransaction();
    }

    public void update(Feature entity) {
        featureDAO.openCurrentSessionwithTransaction();
        featureDAO.update(entity);
        featureDAO.closeCurrentSessionwithTransaction();
    }

    public Feature findById(Integer id) {
        featureDAO.openCurrentSession();
        Feature feature = featureDAO.findById(id);
        featureDAO.closeCurrentSession();
        return feature;
    }

    public void delete(Integer id) {
        featureDAO.openCurrentSessionwithTransaction();
        Feature feature = featureDAO.findById(id);
        featureDAO.delete(feature);
        featureDAO.closeCurrentSessionwithTransaction();
    }

    public List<Feature> findAll() {
        featureDAO.openCurrentSession();
        List<Feature> featureList = featureDAO.findAll();
        featureDAO.closeCurrentSession();
        return featureList;
    }

    public void deleteAll() {
        featureDAO.openCurrentSessionwithTransaction();
        featureDAO.deleteAll();
        featureDAO.closeCurrentSessionwithTransaction();
    }

    public FeatureDAO featureDAO() {
        return featureDAO;
    }
}
