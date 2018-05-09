package eCare.model.services;

import eCare.model.DAO.FeatureDAO;
import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Feature;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public interface FeatureService {


    List<Feature> findByName(String name);

    void persist(Feature entity);

    void update(Feature entity);

    Feature findById(Integer id);

//    void deleteById(Integer id);

    void delete(Integer id);

    List<Feature> findAll();

    void deleteAll();

    boolean isFeatureUnique(String name);

//    List<Feature> findFeatureByCustomer(Customer customer);

    List<Feature> findFeatureByContract(Integer contract);

    List<Feature> findFeatureByTarif(Integer tarifId);

    List<Feature> findAllBlockingFeatures();

}
