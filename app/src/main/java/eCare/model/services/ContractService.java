package eCare.model.services;

import eCare.model.po.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public interface ContractService {

    List<Contract> findByPhone(String telNumber);

    List<Contract> findByCustomerId(Customer customerId);

    List<Contract> findContractByTarif(Tarif tarifId);

    void persist(Contract entity);

    void update(Contract entity);

    Contract findById(Integer id);

    void delete(Integer id);

    List<Contract> findAll();

    void deleteAll();

    Contract findUserContract(Customer user);

    Contract createNewContract(String phone, String sso, String tarif);

    List<Feature> deleteFeatureFromContract(Feature featureToDelete, Contract contract);

    void editContractTarif(Integer contractId, Integer tarifId);

    List<Feature> updateContractOptions(Integer contractId, SelectedFeatures selectedFeaturesIds, Cart cart, HttpSession session);

    String generateNumber(Integer contractId);

}
