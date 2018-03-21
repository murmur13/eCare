package eCare.controllers;

import eCare.model.PO.*;
import eCare.model.services.ContractService;
import eCare.model.services.FeatureService;
import eCare.model.services.TarifService;
import eCare.model.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by echerkas on 13.01.2018.
 */
@Controller
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    FeatureService featureService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ContractService contractService;

    @Autowired
    TarifService tarifService;

    @Autowired
    UserProfileService userProfileService;

    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/listFeatures" }, method = RequestMethod.GET)
    public String listFeatures(@RequestParam(required = false) Integer page, ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        UserProfile userRole = userProfileService.findByType("USER");
        UserProfile adminRole = userProfileService.findByType("ADMIN");

        List<Feature> features = new ArrayList<Feature>();
//        if(user.getUserProfiles().contains(userRole)){
//            Contract contract = user.getContracts().get(0);
//            Tarif tarif = contract.getTarif();
//            features = featureService.findFeatureByTarif(tarif.getTarifId());
////            model.addAttribute("features", features);
//
//        }
//        if(user.getContracts() == null || user.getUserProfiles().contains(adminRole)){
           features = featureService.findAll();
//            model.addAttribute("features", features);
//        }

        PagedListHolder<Feature> pagedListHolder = new PagedListHolder<Feature>(features);
        pagedListHolder.setPageSize(15);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            model.addAttribute("features", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            model.addAttribute("features", pagedListHolder.getPageList());
        }
        model.addAttribute("loggedinuser", getPrincipal());
        return "featuresList";
    }

        @RequestMapping(value = {"/newfeature"}, method = RequestMethod.GET)
        public String newFeature (ModelMap model){
            Feature feature = new Feature();
            model.addAttribute("feature", feature);
            model.addAttribute("edit", false);
            model.addAttribute("loggedinuser", getPrincipal());
            return "featureRegistration";
        }

        /**
         * This method will be called on form submission, handling POST request for
         * saving user in database. It also validates the user input
         */
        @RequestMapping(value = { "/newfeature" }, method = RequestMethod.POST)
        public String saveFeature(@Valid Feature feature, BindingResult result,
                ModelMap model) {

            if (result.hasErrors()) {
                return "featureRegistration";
            }

            if (!featureService.isFeatureUnique(feature.getFeatureName())) {
                FieldError nameError = new FieldError("feature", "name", messageSource.getMessage("non.unique.name", new String[]{feature.getFeatureName()}, Locale.getDefault()));
                result.addError(nameError);
                return "featureRegistration";
            }

            featureService.persist(feature);
            model.addAttribute("success", "Feature " + feature.getFeatureName() + " " + " added successfully");
            model.addAttribute("loggedinuser", getPrincipal());
            //return "success";
            return "registrationsuccess";
        }

    @RequestMapping(value = {"/chooseFeature-{id}"}, method = RequestMethod.GET)
    public String chooseFeature(@PathVariable Integer id, ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = contracts.get(0);
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        Feature chosenFeature = featureService.findById(id);
        for (Feature feature : features) {
            if (feature.getFeatureName().equals(chosenFeature.getFeatureName())) {
                String featureIsAlreadyChosen = messageSource.getMessage("feature.is.already.chosen", new String[]{Integer.toString(chosenFeature.getFeatureId())}, Locale.getDefault());
                model.addAttribute("message", featureIsAlreadyChosen);
                model.addAttribute("loggedinuser", getPrincipal());
                return "errorPage";
            }
        }
        features.add(chosenFeature);
        chosenFeature.setFeatureContracts(contracts);
        featureService.persist(chosenFeature);
        model.addAttribute("userFeatures", features);
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "userContract";
    }


    /**
     * This method will provide the medium to update an existing feature.
     */
    @RequestMapping(value = { "/edit-feature-{id}" }, method = RequestMethod.GET)
    public String editTarif(@PathVariable Integer id, ModelMap model) {
        Feature feature = featureService.findById(id);
        model.addAttribute("feature", feature);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "featureRegistration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-feature-{id}" }, method = RequestMethod.POST)
    public String updateFeature(@Valid Feature feature, BindingResult result,
                              ModelMap model, @PathVariable Integer id) {

        if (result.hasErrors()) {
            return "featureRegistration";
        }
//        if(featureService.isFeatureUnique())
        featureService.update(feature);
        model.addAttribute("success", "Feature " + feature.getFeatureName() + " " + " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/delete-feature-{id}" }, method = RequestMethod.GET)
    public String deleteFeature(@PathVariable Integer id) {
        featureService.delete(id);
        return "redirect:/features/listFeatures";
    }

    @RequestMapping(value = { "/delete-feature-{id}/fromContract" }, method = RequestMethod.GET)
    public String deleteFeatureFromContract(@PathVariable Integer id, HttpSession session, ModelMap model) {
        Customer user = (Customer) session.getAttribute("user");
        Contract contract = user.getContracts().get(0);
        List<Feature> userFeatures = featureService.findFeatureByContract(contract.getContractId());
        Feature featureToDelete = featureService.findById(id);
        int index = userFeatures.indexOf(featureToDelete);
        userFeatures.remove(index);
        List<Contract> featureContracts = featureToDelete.getFeatureContracts();
        int contractIndex = featureContracts.indexOf(contract);
        featureContracts.remove(contractIndex);
        contractService.update(contract);
        featureService.update(featureToDelete);
        model.addAttribute("userFeatures", userFeatures);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect:/contracts/getMyContract";
    }


    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    private String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
