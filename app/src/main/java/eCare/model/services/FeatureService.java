package eCare.model.services;

import eCare.model.po.Contract;
import eCare.model.po.Customer;
import eCare.model.po.Feature;
import eCare.model.po.SelectedFeatures;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public interface FeatureService {


    List<Feature> findByName(String name);

    void persist(Feature entity);

    void update(Feature entity);

    Feature findById(Integer id);

    void delete(Integer id);

    List<Feature> findAll();

    void deleteAll();

    boolean isFeatureUnique(String name);

    List<Feature> findFeatureByContract(Integer contract);

    List<Feature> findFeatureByTarif(Integer tarifId);

    List<Feature> findAllBlockingFeatures();

    List <Feature> findAllRequiredFeatures();

    Contract deletedFeatureFromContract(Integer id, Customer user);

    void createBlockingFeatures(List<Feature> blockingFeatures);

    void createRequiredFeatures(List<Feature> requiredFeatures);

    SelectedFeatures unblockFeatures(List<Feature> features);

    List<Feature> returnUnblockedFeatures(Integer id, Integer secondId);

    List<Feature> dismissRequiredFeatures(Integer id, Integer secondId);

}
