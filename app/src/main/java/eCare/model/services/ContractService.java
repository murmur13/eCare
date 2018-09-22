package eCare.model.services;

import eCare.model.po.*;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

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

    String listContracts(Integer page, ModelMap model);

    String getMyContract(ModelMap model, HttpSession session);

    String newContract(ModelMap model);

    String saveContract(String phone, String sso, String tarif, ModelMap model);

    String seeContractDetails(int id, ModelMap model);

    ModelAndView seeContractOptions(int id, HttpSession session);

    String changeTarif(Integer id,  ModelMap model, HttpSession session);

    String deleteFeatureFromContract(Integer featureId, ModelMap model, HttpSession session);

    String editContract(Integer contractId, ModelMap model);

    String updateContract(Integer contractId, Integer tarifId, ModelMap model);

    String editContractOptions(Integer contractId, ModelMap model, HttpSession session);

    String updateContractOptions(Integer contractId, SelectedFeatures selectedFeaturesIds, BindingResult result,
                                 ModelMap model, Cart cart, HttpSession session);

    String setTarif(Integer id,  ModelMap model, HttpSession session);

    String deleteContract(Integer id, ModelMap model);

    String blockContract(Integer id, ModelMap model, HttpSession session);

    String unblockContract(Integer id, ModelMap model, HttpSession session);

    String generateNumber(Integer id, ModelMap model);

}
