package eCare.controllers;

import eCare.model.PO.*;
import eCare.model.services.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 17.01.2018.
 */
@Controller
@RequestMapping("/contracts")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@SessionAttributes("contract")
public class ContractController {

    @Autowired
    CustomerServiceImpl userService;

    @Autowired
    FeatureService featureService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    TarifService tarifService;

    @Autowired
    ContractServiceImpl contractService;

    @Autowired
    AppController appController;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    RandomPhoneNumber randomPhoneNumber;

    /**
     * This method will list all existing contracts.
     */
    @RequestMapping(value = {"/listContracts" }, method = RequestMethod.GET)
    public String listContracts(@RequestParam(required = false) Integer page, ModelMap model) {
        List<Contract> contracts = contractService.findAll();
        PagedListHolder<Contract> pagedListHolder = new PagedListHolder<Contract>(contracts);
        pagedListHolder.setPageSize(10);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            model.addAttribute("contracts", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            model.addAttribute("contracts", pagedListHolder.getPageList());
        }
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractslist";
    }

    @RequestMapping(value = {"/getMyContract" }, method = RequestMethod.GET)
    public String getMyContract(ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        Contract contract = contractService.findUserContract(user);
        List<Contract> contracts = contractService.findByCustomerId(user);
        List <Feature> features = featureService.findFeatureByContract(contract.getContractId());
            model.addAttribute("userFeatures", features);
            model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "userContract";
    }

    @RequestMapping(value = { "/newcontract" }, method = RequestMethod.GET)
    public String newContract(ModelMap model) {
        Contract contract = new Contract();
        model.addAttribute("contract", contract);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractRegistration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving contract in database. It also validates the user input
     */
    @RequestMapping(value = { "/newcontract" }, method = RequestMethod.POST)
    public String saveContract(@RequestParam(value = "tNumber", required = false) String phone, @RequestParam(value = "customer", required = false) String sso,
                               @RequestParam(value = "tarif", required = false) String tarif,
                               ModelMap model) {

        Customer customer = userService.findBySSO(sso);
        Contract newContract = contractService.createNewContract(phone, sso, tarif);
        model.addAttribute("tarif", newContract.getTarif());
        model.addAttribute("customer", customer);
        model.addAttribute("phone", customer.getTelNumber());
        model.addAttribute("contract", newContract);
        model.addAttribute("success", "Contract " + newContract.getContractId() + " " + " added successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/contract/{id}/details" }, method = RequestMethod.GET)
    public String seeContractDetails(@PathVariable("id") int id, ModelMap model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractslist";
    }

    @RequestMapping(value = {"/showOptions-{id}" }, method = RequestMethod.GET)
    public ModelAndView seeContractOptions(@PathVariable("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("contractFeatures");
        Contract contract = contractService.findById(id);
        session.setAttribute("contract", contract);
        List<Feature> contractFeatures = featureService.findFeatureByContract(id);
        if(!contractFeatures.isEmpty()){
            modelAndView.addObject("features", contractFeatures);
            modelAndView.addObject("loggedinuser", userService.getPrincipal());
            return modelAndView;
    }
        else{
            ModelAndView modelAndView2 = new ModelAndView("errorPage");
            String contractWithoutOptions = messageSource.getMessage("contract.not.having.any.options", new String[]{Integer.toString(id)}, Locale.getDefault());
            modelAndView2.addObject("message", contractWithoutOptions);
            modelAndView2.addObject("loggedinuser", userService.getPrincipal());
            return modelAndView2;
        }
    }

    @RequestMapping(value = {"/changeTarif-{id}"}, method = RequestMethod.GET)
    public String changeTarif(@PathVariable Integer id,  ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
        Tarif tarif = tarifService.findById(id);
        model.addAttribute("tarif", tarif);
        Contract contract = contracts.get(0);
        if (!contract.getTarif().equals(tarif)) {
            contract.setTarif(tarif);
            List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
            features.clear();
//            contractService.update(contract);
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

    @RequestMapping(value = {"/deleteFeature/fromContract-{featureId}"}, method = RequestMethod.GET)
    public String deleteFeatureFromContract(@PathVariable Integer featureId,
                                            ModelMap model, HttpSession session) {
//        Contract contract = contractService.findById(contractId);
        Contract contract = (Contract) session.getAttribute("contract");
        Feature featureToDelete = featureService.findById(featureId);
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        List<Contract> featureContracts = featureToDelete.getFeatureContracts();
        int index = features.indexOf(featureToDelete);
        features.remove(index);
        featureContracts.remove(contract);
        featureService.update(featureToDelete);
        contractService.update(contract);
        model.addAttribute("features", features);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "contractFeatures";
    }

    /**
     * This method will provide the medium to update an existing contract.
     */

    @RequestMapping(value = { "/edit-contractTarif-{contractId}" }, method = RequestMethod.GET)
    public String editContract(@PathVariable Integer contractId, ModelMap model) {
        Contract contract = contractService.findById(contractId);
        List<Tarif> tarifs = tarifService.findAll();
        Tarif userTarif = contract.getTarif();
        model.addAttribute("tarif", userTarif);
        model.addAttribute("userTarif", userTarif);
        model.addAttribute("tarifs", tarifs);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "editContractTarif";
    }

    @RequestMapping(value = { "/edit-contractTarif-{contractId}" }, method = RequestMethod.POST)
    public String updateContract(@PathVariable Integer contractId, Integer tarifId, ModelMap model, HttpSession session){
        Contract contract = contractService.findById(contractId);
        Tarif newTarif = tarifService.findById(tarifId);
        contract.setTarif(newTarif);
//        session.setAttribute("tarifInCart", newTarif);
        contractService.update(contract);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
//        return "redirect: /cart";
        return "redirect:/contracts/listContracts";
    }

    @RequestMapping(value = { "/edit-contractOptions-{contractId}" }, method = RequestMethod.GET)
    public String editContractOptions(@PathVariable Integer contractId, ModelMap model, HttpSession session) {
        Contract contract = contractService.findById(contractId);
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

    @RequestMapping(value = { "/edit-contractOptions-{contractId}" }, method = RequestMethod.POST)
    public String updateContractOptions(@PathVariable Integer contractId, @ModelAttribute(value = "selectedFeatures") SelectedFeatures selectedFeaturesIds, BindingResult result,
                                        ModelMap model, Cart cart, HttpSession session){
        Contract contract = contractService.findById(contractId);
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
                contractService.update(contract);
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
                    contractService.update(contract);
                    tarifService.update(tarif);
                }
            }
        }

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


    @RequestMapping(value = {"/setTarifToContract-{id}"}, method = RequestMethod.GET)
    public String setTarif(@PathVariable Integer id,  ModelMap model, HttpSession session) {
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
            contractService.update(contract);
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

    @RequestMapping(value = { "/delete-contract-{id}" }, method = RequestMethod.GET)
    public String deleteContract(@PathVariable Integer id, ModelMap model) {
        contractService.delete(id);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/contracts/listContracts";
    }

    @RequestMapping(value = { "/block-contract-{id}" }, method = RequestMethod.GET)
    public String blockContract(@PathVariable Integer id, ModelMap model, HttpSession session) {

        UserProfile roleUser = userProfileService.findByType("USER");
        UserProfile roleAdmin = userProfileService.findByType("ADMIN");

        Contract contract = contractService.findById(id);
        Customer user = contract.getCustomer();
        Customer sessionUser = (Customer) session.getAttribute("user");
        if(user.isBlockedByUser() || user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is already blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }

//        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))){
        if (!user.equals(sessionUser)){
            user.setBlockedByAdmin(true);
            userService.updateUser(user);
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
        }
        else{
            user.setBlockedByUser(true);
            userService.updateUser(user);
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
        }
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/unblock-contract-{id}" }, method = RequestMethod.GET)
    public String unblockContract(@PathVariable Integer id, ModelMap model, HttpSession session) {
        Contract contract = contractService.findById(id);
        Customer user = contract.getCustomer();
        UserProfile roleUser = userProfileService.findByType("USER");
        UserProfile roleAdmin = userProfileService.findByType("ADMIN");
        Customer sessionUser = (Customer) session.getAttribute("user");

        if(!user.isBlockedByUser() && !user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is not blocked");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        if(user.isBlockedByAdmin() && user.equals(sessionUser)){
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked by ADMIN. You can't unblock it");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }

        if(user.equals(sessionUser)){
            user.setBlockedByUser(false);
            userService.updateUser(user);
            model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
//            model.addAttribute("loggedinuser", getPrincipal());
//            return "registrationsuccess";
        }

        if(!user.equals(sessionUser)){
            user.setBlockedByAdmin(false);
            userService.updateUser(user);
            model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
//            model.addAttribute("loggedinuser", getPrincipal());
//            return "registrationsuccess";
        }


        model.addAttribute("message", "User " + user.getSsoId() + " is unblocked");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/generatePhoneNumber-contract-{id}" }, method = RequestMethod.GET)
    public String generateNumber(@PathVariable Integer id, ModelMap model, HttpSession session) {
        RandomPhoneNumber numberGenerator = new RandomPhoneNumber();
        Contract contract = contractService.findById(id);
        String number = numberGenerator.generateNumber();
        List<Contract> allContracts = contractService.findAll();
        for (Contract someContract :allContracts) {
            String someNumber = contract.gettNumber();
            if(someNumber.equals(number))
                number = numberGenerator.generateNumber();
        }
        contract.settNumber(number);
        contractService.update(contract);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        model.addAttribute("message", "number " + number + " was generated and set to contract");
        return "registrationsuccess";
    }
}
