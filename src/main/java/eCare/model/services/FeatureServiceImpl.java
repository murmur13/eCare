package eCare.model.services;

import eCare.model.DAO.ContractDAO;
import eCare.model.DAO.CustomerDAO;
import eCare.model.DAO.FeatureDAO;
import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 13.01.2018.
 */

@Service("featureService")
@Transactional
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureDAO featureDAO;

//    @Autowired
//    private CustomerDAO customerDAO;

    @Autowired
    private ContractDAO contractDAO;

    public Feature findById(Integer id) {
        return featureDAO.findById(id);
    }

    public void persist(Feature feature) {
        featureDAO.persist(feature);
    }

    public void delete(Integer id){
        Feature feature = featureDAO.findById(id);
        featureDAO.delete(feature);
    }

    public void update(Feature feature) {
        featureDAO.update(feature);
    }

    public List<Feature> findAll() {
        return featureDAO.findAll();
    }

    public List<Feature> findAllBlockingFeatures(){return featureDAO.findAllBlockingFeatures();}

//    public List<Feature> findByTarif(Integer tarifId){
//        List<Feature> feature = featureDAO.findByTarif(tarifId);
//        return feature;
//    }

    public List<Feature> findFeatureByTarif(Integer tarifId){
        List<Feature> features = featureDAO.findFeatureByTarif(tarifId);
        return features;
    }

    public List<Feature> findFeatureByContract(Integer contract){
        Contract userContract = contractDAO.findById(contract);
        List<Feature> featureList = featureDAO.findFeatureByContract(userContract.getContractId());
        return featureList;
    }

    public void deleteAll(){
        featureDAO.deleteAll();
    }

    public boolean isFeatureUnique(String name){
        boolean isUnique = false;
        List<Feature> feature = findByName(name);
        if(feature == null || feature.isEmpty()){
            isUnique=true;
        }
        return isUnique;
    }

    public List<Feature> findByName(String name) {
        List<Feature> feature = featureDAO.findByName(name);
        return feature;
    }
}
