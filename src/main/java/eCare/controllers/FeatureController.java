package eCare.controllers;

import eCare.model.PO.*;
import eCare.model.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.cglib.core.Block;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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
                model.addAttribute("message", "This option already exists");
                return "errorPage";
            }

            featureService.persist(feature);
            model.addAttribute("message", "Feature " + feature.getFeatureName() + " " + " added successfully");
            model.addAttribute("loggedinuser", getPrincipal());
            //return "success";
            return "registrationsuccess";
        }

    @RequestMapping(value = {"/chooseFeature-{id}"}, method = RequestMethod.GET)
    public String chooseFeature(@PathVariable Integer id, ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = contracts.get(0);
        Tarif tarif = contract.getTarif();
        List<Feature> tarifOptions = featureService.findFeatureByTarif(tarif.getTarifId());
        List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
        Feature chosenFeature = featureService.findById(id);
        for (Feature feature : features) {
            if (feature.getFeatureId()== chosenFeature.getFeatureId()) {
                String featureIsAlreadyChosen = messageSource.getMessage("feature.is.already.chosen", new String[]{Integer.toString(chosenFeature.getFeatureId())}, Locale.getDefault());
                model.addAttribute("message", featureIsAlreadyChosen);
                model.addAttribute("loggedinuser", getPrincipal());
                return "errorPage";
            }
        }
        features.add(chosenFeature);
        chosenFeature.setFeatureContracts(contracts);
        tarifOptions.add(chosenFeature);
        List <Tarif> featureTarifs = chosenFeature.getFeatureTarifs();
        featureTarifs.add(tarif);
        chosenFeature.setFeatureTarifs(featureTarifs);
        Cart cart = (Cart) session.getAttribute("cart");
//        contractService.update(contract);
//        featureService.update(chosenFeature);
//        tarifService.update(tarif);
//        if(session.getAttribute("cart")== null){
//
//        }
//        Cart cart = (Cart)
        cart.setOptionsInCart(features);
        session.setAttribute("optionsInCart", features);
        session.setAttribute("cart", cart);
        model.addAttribute("userFeatures", features);
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect: /cart";
//        return "userContract";
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
        model.addAttribute("message", "Feature " + feature.getFeatureName() + " " + " updated successfully");
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

    @RequestMapping(value = { "/blockingFeatures/seeAll" }, method = RequestMethod.GET)
    public String seeBlockingFeatures(ModelMap model) {

        List<Feature> blockingFeatures = featureService.findAllBlockingFeatures();
        List<MessagesList> messagesList = new ArrayList<MessagesList>();
        if (blockingFeatures.isEmpty()) {
            model.addAttribute("message", "There is no blocked features yet");
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
        Set<Feature> blockingFeaturesSet = new HashSet<Feature>(blockingFeatures);

        for (Feature blockingfeature: blockingFeaturesSet) {
            List<Feature> featuresToDisplay = blockingfeature.getBlockingFeatures();
            MessagesList message = new MessagesList();
            message.setMessageFeature(blockingfeature);
            List<String> names = new ArrayList<String>();
            for (Feature feature : featuresToDisplay) {
                String name = feature.getFeatureName();
                names.add(name);
            }
            message.setMessageList(names);
            messagesList.add(message);
        }
        model.addAttribute("messages", messagesList);
        model.addAttribute("features", blockingFeaturesSet);
        model.addAttribute("loggedinuser", getPrincipal());
        return "blockingFeaturesList";
    }

    @RequestMapping(value = { "/blockingFeatures" }, method = RequestMethod.GET)
    public String blockingFeatures(ModelMap model) {
        List<Feature > features = featureService.findAll();
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(featureService.findAll()));
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", features);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "blockingFeatures";
    }

    @RequestMapping(value = { "/blockingFeatures" }, method = RequestMethod.POST)
    public String blockingFeatures (@ModelAttribute(value = "selectedFeatures") SelectedFeatures selectedFeaturesIds, BindingResult result,
                                        ModelMap model){
        List<Feature> blockingFeatures = selectedFeaturesIds.getSelectedFeatures();
        if (blockingFeatures.size() > 2) {
            model.addAttribute("message", "You can choose only two options");
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
        Feature fisrtFeature = blockingFeatures.get(0);
        Feature secondFeature = blockingFeatures.get(1);
        List<Feature> blockedFeatures = fisrtFeature.getBlockingFeatures();
        blockedFeatures.add(secondFeature);
        fisrtFeature.setBlockingFeatures(blockedFeatures);
        featureService.update(fisrtFeature);

        if (result.hasErrors()) {
            model.addAttribute("message", "OOOPS");
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
        model.addAttribute("features", blockingFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect:/features/blockingFeatures/seeAll";

    }

    @RequestMapping(value = { "/unblockFeatures" }, method = RequestMethod.GET)
    public String unblockFeatures(ModelMap model) {
        List<Feature > features = featureService.findAllBlockingFeatures();
        HashSet<Feature> set = new HashSet<Feature>(features);
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
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", set);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "unblockFeatures";
    }

    @RequestMapping(value = { "/unblockFeatures/{id}_{secondId}" }, method = RequestMethod.GET)
    public String unblockFeatures (@PathVariable Integer id, @PathVariable Integer secondId,
                                    ModelMap model){

        List<Feature> blockingFeatures = featureService.findAllBlockingFeatures();
        Integer index = blockingFeatures.indexOf(featureService.findById(secondId));
        Feature featureToDelete = blockingFeatures.get(index);
        List<Feature> features =  featureToDelete.getBlockingFeatures();
        features.remove(featureService.findById(id));
        featureToDelete.setBlockingFeatures(features);
        featureService.update(featureToDelete);
        blockingFeatures.remove(featureToDelete);

//        if (blockingFeatures.size() > 2) {
//            model.addAttribute("message", "Something went wrong. There must be two features to unblock..");
//            model.addAttribute("loggedinuser", getPrincipal());
//            return "errorPage";
//        }

        model.addAttribute("features", blockingFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect:/features/unblockFeatures";

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
