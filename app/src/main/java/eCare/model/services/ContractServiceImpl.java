package eCare.model.services;

import eCare.model.dao.ContractDao;
import eCare.model.dao.CustomerDao;
import eCare.model.dao.TarifDao;
import eCare.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 17.01.2018.
 */

@Service("contractService")
@DependsOn("messageSource")
@Transactional
public class ContractServiceImpl implements ContractService {

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

    @Autowired
    private MessageSource messageSource;

    public Contract findById(Integer id) {
        return contractDao.findById(id);
    }

    public void persist(Contract contract) {
        contractDao.persist(contract);
    }

    public void delete(Integer id) {
        Contract contract = contractDao.findById(id);
        contractDao.delete(contract);
    }

    public void update(Contract contract) {
        contractDao.update(contract);
    }

    public List<Contract> findAll() {
        return contractDao.findAll();
    }

    public void deleteAll() {
        contractDao.deleteAll();
    }

    public List<Contract> findByPhone(String telNumber) {
        List<Contract> contract = contractDao.findByPhone(telNumber);
        return contract;
    }

    public List<Contract> findByCustomerId(Customer customerId) {
        Customer customer = customerDao.findById(customerId.getId());
        List<Contract> contracts = contractDao.findByCustomerId(customerId);
        return contracts;
    }

    public List<Contract> findContractByTarif(Tarif tarifId) {
        Tarif tarif = tarifDao.findById(tarifId.getTarifId());
        List<Contract> contracts = contractDao.findContractByTarif(tarif);
        return contracts;
    }

    public Contract findUserContract(Customer user) {
        List<Contract> contracts = findByCustomerId(user);
        Contract contract = contracts.get(0);
        return contract;
    }

    public Contract createNewContract(String phone, String sso, String tarif) {
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

    public List<Feature> deleteFeatureFromContract(Feature featureToDelete, Contract contract) {
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        List<Contract> featureContracts = featureToDelete.getFeatureContracts();
        int index = features.indexOf(featureToDelete);
        features.remove(index);
        featureContracts.remove(contract);
        featureService.update(featureToDelete);
        update(contract);
        return features;
    }

    public void editContractTarif(Integer contractId, Integer tarifId) {
        Contract contract = findById(contractId);
        Tarif newTarif = tarifService.findById(tarifId);
        contract.setTarif(newTarif);
        update(contract);
    }

    public List<Feature> updateContractOptions(Integer contractId, SelectedFeatures selectedFeaturesIds, Cart cart, HttpSession session) {
        Contract contract = findById(contractId);
        Tarif tarif = contract.getTarif();
        List<Feature> userFeatures = featureService.findFeatureByContract(contractId);
        List<Feature> finalFeatures = selectedFeaturesIds.getSelectedFeatures();
        if (finalFeatures.isEmpty()) {
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
        } else {
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

    public String generateNumber(Integer contractId) {
        RandomPhoneNumber numberGenerator = new RandomPhoneNumber();
        Contract contract = findById(contractId);
        String number = numberGenerator.generateNumber();
        List<Contract> allContracts = findAll();
        for (Contract someContract : allContracts) {
            String someNumber = contract.gettNumber();
            if (someNumber.equals(number))
                number = numberGenerator.generateNumber();
        }
        contract.settNumber(number);
        update(contract);
        return number;
    }

    public String listContracts(Integer page, ModelMap model) {
        List<Contract> contracts = findAll();
        PagedListHolder<Contract> pagedListHolder = new PagedListHolder<Contract>(contracts);
        pagedListHolder.setPageSize(10);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(0);
            model.addAttribute("contracts", pagedListHolder.getPageList());
        } else if (page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page - 1);
            model.addAttribute("contracts", pagedListHolder.getPageList());
        }
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractslist";
    }

    public String getMyContract(ModelMap model, HttpSession session) {
        Customer user = userService.findBySSO(userService.getPrincipal());
        if (user == null) {
            model.addAttribute("message", "please, login");
            return "errorPage";
        }
        Contract contract = findUserContract(user);
        List<Contract> contracts = findByCustomerId(user);
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        model.addAttribute("userFeatures", features);
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "userContract";
    }

    public String newContract(ModelMap model) {
        Contract contract = new Contract();
        model.addAttribute("contract", contract);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractRegistration";
    }

    public String saveContract(String phone, String sso, String tarif, ModelMap model) {
        Customer customer = userService.findBySSO(sso);
        List<Contract> allContracts = findAll();
        for (Contract someContract : allContracts) {
            String someNumber = someContract.gettNumber();
            if (someNumber.equals(phone)) {
                model.addAttribute("loggedinuser", userService.getPrincipal());
                model.addAttribute("message", "This phone number is already taken");
                return "errorPage";
            }
        }
        Contract newContract = createNewContract(phone, sso, tarif);
        model.addAttribute("tarif", newContract.getTarif());
        model.addAttribute("customer", customer);
        model.addAttribute("phone", customer.getTelNumber());
        model.addAttribute("contract", newContract);
        model.addAttribute("message", "Contract " + newContract.getContractId() + " " + " added successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String seeContractDetails(int id, ModelMap model) {
        Contract contract = findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractslist";
    }

    public ModelAndView seeContractOptions(int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("contractFeatures");
        Contract contract = findById(id);
        session.setAttribute("contract", contract);
        List<Feature> contractFeatures = featureService.findFeatureByContract(id);
        if (!contractFeatures.isEmpty()) {
            modelAndView.addObject("features", contractFeatures);
            modelAndView.addObject("loggedinuser", userService.getPrincipal());
            return modelAndView;
        } else {
            ModelAndView modelAndView2 = new ModelAndView("errorPage");
            String contractWithoutOptions = messageSource.getMessage("contract.not.having.any.options", new String[]{Integer.toString(id)}, Locale.getDefault());
            modelAndView2.addObject("message", contractWithoutOptions);
            modelAndView2.addObject("loggedinuser", userService.getPrincipal());
            return modelAndView2;
        }
    }

    public String changeTarif(Integer id, ModelMap model, HttpSession session) {
        Customer user = userService.findBySSO(userService.getPrincipal());
        List<Contract> contracts = findByCustomerId(user);
        Tarif tarif = tarifService.findById(id);
        model.addAttribute("tarif", tarif);
        Contract contract = findUserContract(user);
        if (!contract.getTarif().equals(tarif)) {
            contract.setTarif(tarif);
            List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
            features.clear();
            model.addAttribute("features", null);
            model.addAttribute("contracts", contracts);
            model.addAttribute("edit", true);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "userContract";
        } else {
            String tarifIsAlreadyChosen = messageSource.getMessage("tarif.is.already.chosen", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifIsAlreadyChosen);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
    }

    public String deleteFeatureFromContract(Integer featureId, ModelMap model, HttpSession session) {
        Contract contract = (Contract) session.getAttribute("contract");
        Feature featureToDelete = featureService.findById(featureId);
        List<Feature> features = deleteFeatureFromContract(featureToDelete, contract);
        model.addAttribute("features", features);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractFeatures";
    }

    public String editContract(Integer contractId, ModelMap model) {
        Contract contract = findById(contractId);
        List<Tarif> tarifs = tarifService.findAll();
        List<Contract> existingContracts = findAll();
        if (!existingContracts.contains(contract)) {
            String string = "contract with id " + contractId + " is not found";
            model.addAttribute("message", string);
            return "errorPage";
        }
        Tarif userTarif = contract.getTarif();
        model.addAttribute("tarif", userTarif);
        model.addAttribute("userTarif", userTarif);
        model.addAttribute("tarifs", tarifs);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "editContractTarif";
    }

    public String updateContract(Integer contractId, Integer tarifId, ModelMap model) {
        editContractTarif(contractId, tarifId);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/contracts/listContracts";
    }

    public String editContractOptions(Integer contractId, ModelMap model, HttpSession session) {
        Contract contract = findById(contractId);
        List<Feature> features = featureService.findAll();
        List<Feature> userFeatures = featureService.findFeatureByContract(contractId);
        Cart cart = new Cart();
        session.setAttribute("cart", cart);
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(featureService.findAll()));
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", features);
        model.addAttribute("contract", contract);
        model.addAttribute("userFeatures", userFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "editContractFeatures";
    }

    public String updateContractOptions(Integer contractId, SelectedFeatures selectedFeaturesIds, BindingResult result,
                                        ModelMap model, Cart cart, HttpSession session) {
        List<Feature> userFeatures = updateContractOptions(contractId, selectedFeaturesIds, cart, session);
        if (result.hasErrors()) {
            model.addAttribute("message", "OOOPS");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        model.addAttribute("userFeatures", userFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/contracts/listContracts";
    }

    public String setTarif(Integer id, ModelMap model, HttpSession session) {
        Contract contract = (Contract) session.getAttribute("contract");
        List<Contract> contracts = new ArrayList<Contract>();
        contracts.add(contract);
        Tarif tarif = tarifService.findById(id);
        model.addAttribute("tarif", tarif);
        if (!contract.getTarif().getName().equals(tarif.getName())) {
            contract.setTarif(tarif);
            List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
            for (Feature feature : features) {
                featureService.delete(feature.getFeatureId());
            }
            update(contract);
            model.addAttribute("features", features);
            model.addAttribute("contracts", contracts);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "userContract";
        } else {
            String tarifIsAlreadyChosen = messageSource.getMessage("tarif.is.already.chosen", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifIsAlreadyChosen);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
    }

    public String deleteContract(Integer id, ModelMap model) {
        delete(id);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/contracts/listContracts";
    }

    public String blockContract(Integer id, ModelMap model, HttpSession session) {
        Contract contract = findById(id);
        Customer user = contract.getCustomer();
        Customer notSessionUser = userService.findBySSO(userService.getPrincipal());
        if (user.isBlockedByUser() || user.isBlockedByAdmin()) {
            model.addAttribute("message", "User " + user.getSsoId() + " is already blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }

        if (!user.equals(notSessionUser)) {
            user.setBlockedByAdmin(true);
            userService.updateUser(user);
            session.setAttribute("user", user);
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
        } else {
            user.setBlockedByUser(true);
            userService.updateUser(user);
            session.setAttribute("user", user);
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
        }
        return "registrationsuccess";
    }

    public String unblockContract(Integer id, ModelMap model, HttpSession session) {
        Contract contract = findById(id);
        Customer user = contract.getCustomer();
        Customer notSessionUser = userService.findBySSO(userService.getPrincipal());

        if (!user.isBlockedByUser() && !user.isBlockedByAdmin()) {
            model.addAttribute("message", "User " + user.getSsoId() + " is not blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        if (user.isBlockedByAdmin() && user.equals(notSessionUser)) {
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked by ADMIN. You can't unblock it");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }

        if (user.equals(notSessionUser)) {
            user.setBlockedByUser(false);
            userService.updateUser(user);
            session.setAttribute("user", user);
            model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
        }

        if (!user.equals(notSessionUser)) {
            user.setBlockedByAdmin(false);
            userService.updateUser(user);
            session.setAttribute("user", user);
            model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
        }

        model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String generateNumber(Integer id, ModelMap model) {
        String number = generateNumber(id);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        model.addAttribute("message", "number " + number + " was generated and set to contract");
        return "registrationsuccess";
    }
}
