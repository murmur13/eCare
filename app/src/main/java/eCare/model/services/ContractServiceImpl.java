package eCare.model.services;

import eCare.model.dao.ContractDao;
import eCare.model.dao.CustomerDao;
import eCare.model.dao.TarifDao;
import eCare.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by echerkas on 17.01.2018.
 */

@Service("contractService")
@Transactional
public class ContractServiceImpl implements ContractService{

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private TarifDao tarifDao;

    @Autowired
    private CustomerService userService;

    @Autowired
    private TarifService tarifService;

    public Contract findById(Integer id) {
        return contractDao.findById(id);
    }

    public void persist(Contract contract) {
        contractDao.persist(contract);
    }

    public void delete(Integer id){
        Contract contract = contractDao.findById(id);
        contractDao.delete(contract);
    }

    public void update(Contract contract) {
        contractDao.update(contract);
    }

    public List<Contract> findAll() {
        return contractDao.findAll();
    }

    public void deleteAll(){
        contractDao.deleteAll();
    }

    public List<Contract> findByPhone(String telNumber) {
        List<Contract> contract = contractDao.findByPhone(telNumber);
        return contract;
    }

    public List<Contract> findByCustomerId(Customer customerId){
        Customer customer = customerDao.findById(customerId.getId());
        List<Contract> contracts = contractDao.findByCustomerId(customerId);
        return contracts;
    }

    public List<Contract> findContractByTarif(Tarif tarifId){
        Tarif tarif = tarifDao.findById(tarifId.getTarifId());
        List<Contract>contracts = contractDao.findContractByTarif(tarif);
        return contracts;
    }

    public Contract findUserContract(Customer user){
        List<Contract> contracts = findByCustomerId(user);
        Contract contract = contracts.get(0);
        return contract;
    }

    public Contract createNewContract(String phone, String sso, String tarif){
        Customer customer = userService.findBySSO(sso);
        Tarif tarif1 = tarifService.findById(Integer.parseInt(tarif));
        Contract contract = new Contract(phone, customer, tarif1);
        customer.setTelNumber(phone);
        userService.updateUser(customer);
        persist(contract);
        List<Contract> tarifContracts = findContractByTarif(tarif1);
        tarifContracts.add(contract);
        return contract;
    }

    public List<Feature> deleteFeatureFromContract(Feature featureToDelete, Contract contract){
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        List<Contract> featureContracts = featureToDelete.getFeatureContracts();
        int index = features.indexOf(featureToDelete);
        features.remove(index);
        featureContracts.remove(contract);
        featureService.update(featureToDelete);
        update(contract);
        return features;
    }

    public void editContractTarif(Integer contractId, Integer tarifId){
        Contract contract = findById(contractId);
        Tarif newTarif = tarifService.findById(tarifId);
        contract.setTarif(newTarif);
        update(contract);
    }

    public List<Feature> updateContractOptions(Integer contractId, SelectedFeatures selectedFeaturesIds,Cart cart, HttpSession session){
        Contract contract = findById(contractId);
        Tarif tarif = contract.getTarif();
        List<Feature> userFeatures = featureService.findFeatureByContract(contractId);
        List<Feature> finalFeatures = selectedFeaturesIds.getSelectedFeatures();
        if(finalFeatures.isEmpty()) {
            for (Feature feature : userFeatures) {
                List<Tarif> featureTarifs = feature.getFeatureTarifs();
                List<Contract> featureContracts = feature.getFeatureContracts();
                featureContracts.remove(contract);
                feature.setFeatureContracts(featureContracts);
                featureTarifs.remove(feature);
                feature.setFeatureTarifs(featureTarifs);
                featureService.update(feature);
                update(contract);
                tarifService.update(tarif);
            }
            userFeatures.clear();
        }
        else {
            for (Feature feature : finalFeatures) {
                if (!userFeatures.contains(feature)) {
                    userFeatures.add(feature);
                    List<Tarif> featureTarifs = feature.getFeatureTarifs();
                    List<Contract> featureContracts = feature.getFeatureContracts();
                    if (!featureContracts.contains(contract)) {
                        featureContracts.add(contract);
                        feature.setFeatureContracts(featureContracts);
                    }

                    if (!featureTarifs.contains(tarif)) {
                        featureTarifs.add(tarif);
                        feature.setFeatureTarifs(featureTarifs);
                    }
                    cart.setOptionsInCart(finalFeatures);
                    session.setAttribute("optionsInCart", finalFeatures);
                    featureService.update(feature);
                    update(contract);
                    tarifService.update(tarif);
                }
            }
        }
        return userFeatures;
    }

    public String generateNumber(Integer contractId){
        RandomPhoneNumber numberGenerator = new RandomPhoneNumber();
        Contract contract = findById(contractId);
        String number = numberGenerator.generateNumber();
        List<Contract> allContracts = findAll();
        for (Contract someContract :allContracts) {
            String someNumber = contract.gettNumber();
            if(someNumber.equals(number))
                number = numberGenerator.generateNumber();
        }
        contract.settNumber(number);
        update(contract);
        return number;
    }
}
