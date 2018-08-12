package eCare.model.services;

import eCare.model.dao.ContractDao;
import eCare.model.dao.FeatureDao;
import eCare.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 13.01.2018.
 */

@Service("featureService")
@Transactional
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractService contractService;

    public Feature findById(Integer id) {
        return featureDao.findById(id);
    }

    public void persist(Feature feature) {
        featureDao.persist(feature);
    }

    public void delete(Integer id){
        Feature feature = featureDao.findById(id);
        featureDao.delete(feature);
    }

    public void update(Feature feature) {
        featureDao.update(feature);
    }

    public List<Feature> findAll() {
        return featureDao.findAll();
    }

    public List<Feature> findAllBlockingFeatures(){return featureDao.findAllBlockingFeatures();}

    public List<Feature> findAllRequiredFeatures(){return featureDao.findAllRequiredFeatures();}

    public List<Feature> findFeatureByTarif(Integer tarifId){
        List<Feature> features = featureDao.findFeatureByTarif(tarifId);
        return features;
    }

    public List<Feature> findFeatureByContract(Integer contract){
        Contract userContract = contractDao.findById(contract);
        List<Feature> featureList = featureDao.findFeatureByContract(userContract.getContractId());
        return featureList;
    }

    public void deleteAll(){
        featureDao.deleteAll();
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
        List<Feature> feature = featureDao.findByName(name);
        return feature;
    }

    public Contract deletedFeatureFromContract(Integer id, Customer user){
        Contract contract = contractService.findUserContract(user);
        List<Feature> userFeatures = findFeatureByContract(contract.getContractId());
        Feature featureToDelete = findById(id);
        int index = userFeatures.indexOf(featureToDelete);
        userFeatures.remove(index);
        List<Contract> featureContracts = featureToDelete.getFeatureContracts();
        int contractIndex = featureContracts.indexOf(contract);
        featureContracts.remove(contractIndex);
        contractService.update(contract);
        update(featureToDelete);
        return contract;
    }

    public void createBlockingFeatures(List<Feature> blockingFeatures){
        Feature fisrtFeature = blockingFeatures.get(0);
        Feature secondFeature = blockingFeatures.get(1);
        List<Feature> blockedFeatures = fisrtFeature.getBlockingFeatures();
        blockedFeatures.add(secondFeature);
        fisrtFeature.setBlockingFeatures(blockedFeatures);
        update(fisrtFeature);
    }

    public void createRequiredFeatures(List<Feature> requiredFeatures){
        Feature fisrtFeature = requiredFeatures.get(0);
        Feature secondFeature = requiredFeatures.get(1);
        List<Feature> features = fisrtFeature.getRequiredFeatures();
        features.add(secondFeature);
        fisrtFeature.setRequiredFeatures(features);
        update(fisrtFeature);
    }

    public SelectedFeatures unblockFeatures(List<Feature> features){
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(features));
        for (Feature blockingfeature: features) {
            List<Feature> featuresToDisplay = blockingfeature.getBlockingFeatures();
            MessagesList message = new MessagesList();
            message.setMessageFeature(blockingfeature);
            List<String> names = new ArrayList<String>();
            for (Feature feature : featuresToDisplay) {
                String name = feature.getFeatureName();
                names.add(name);
            }
            message.setMessageList(names);
        }
        return selectedFeatures;
    }

    public List<Feature> returnUnblockedFeatures(Integer id, Integer secondId){
        List<Feature> blockingFeatures = findAllBlockingFeatures();
        Integer index = blockingFeatures.indexOf(findById(secondId));
        Feature featureToDelete = blockingFeatures.get(index);
        List<Feature> features =  featureToDelete.getBlockingFeatures();
        features.remove(findById(id));
        featureToDelete.setBlockingFeatures(features);
        update(featureToDelete);
        blockingFeatures.remove(featureToDelete);
        return blockingFeatures;
    }

    public List<Feature> dismissRequiredFeatures(Integer id, Integer secondId){
        List<Feature> requiredFeatures = findAllRequiredFeatures();
        Integer index = requiredFeatures.indexOf(findById(secondId));
        Feature featureToDelete = requiredFeatures.get(index);
        List<Feature> features =  featureToDelete.getRequiredFeatures();
        features.remove(findById(id));
        featureToDelete.setRequiredFeatures(features);
        update(featureToDelete);
        requiredFeatures.remove(featureToDelete);
        return requiredFeatures;
    }
}
