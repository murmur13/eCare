package eCare.model.services;

import eCare.model.dao.ContractDao;
import eCare.model.dao.FeatureDao;
import eCare.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by echerkas on 13.01.2018.
 */

@Service("featureService")
@DependsOn("messageSource")
@Transactional
public class FeatureServiceImpl implements FeatureService {

    private final static String NO_USERSERVICE_DEFINED = "Lost connection with user service";

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CustomerService userService;

    @Autowired
    private MessageSource messageSource;

    public Feature findById(Integer id) {
        return featureDao.findById(id);
    }

    public void persist(Feature feature) {
        featureDao.persist(feature);
    }

    public void delete(Integer id) {
        Feature feature = featureDao.findById(id);
        featureDao.delete(feature);
    }

    public void update(Feature feature) {
        featureDao.update(feature);
    }

    public List<Feature> findAll() {
        return featureDao.findAll();
    }

    public List<Feature> findAllBlockingFeatures() {
        return featureDao.findAllBlockingFeatures();
    }

    public List<Feature> findAllRequiredFeatures() {
        return featureDao.findAllRequiredFeatures();
    }

    public List<Feature> findFeatureByTarif(Integer tarifId) {
        List<Feature> features = featureDao.findFeatureByTarif(tarifId);
        return features;
    }

    public List<Feature> findFeatureByContract(Integer contract) {
        Contract userContract = contractDao.findById(contract);
        List<Feature> featureList = featureDao.findFeatureByContract(userContract.getContractId());
        return featureList;
    }

    public void deleteAll() {
        featureDao.deleteAll();
    }

    public boolean isFeatureUnique(String name) {
        boolean isUnique = false;
        List<Feature> feature = findByName(name);
        if (feature == null || feature.isEmpty()) {
            isUnique = true;
        }
        return isUnique;
    }

    public List<Feature> findByName(String name) {
        List<Feature> feature = featureDao.findByName(name);
        return feature;
    }

    public Contract deletedFeatureFromContract(Integer id, Customer user) {
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

    public void createBlockingFeatures(List<Feature> blockingFeatures) {
        List<Feature> blockedFeatures = new ArrayList<Feature>();
        Feature fisrtFeature = blockingFeatures.get(0);
        Feature secondFeature = blockingFeatures.get(1);
        if (!fisrtFeature.getBlockingFeatures().isEmpty()) {
            blockedFeatures.addAll(fisrtFeature.getBlockingFeatures());
        }
        blockedFeatures.add(secondFeature);
        fisrtFeature.setBlockingFeatures(blockedFeatures);
        if (featureDao != null) {
            update(fisrtFeature);
        }
    }

    public void createRequiredFeatures(List<Feature> requiredFeatures) {
        Feature fisrtFeature = requiredFeatures.get(0);
        Feature secondFeature = requiredFeatures.get(1);
        List<Feature> features = fisrtFeature.getRequiredFeatures();
        features.add(secondFeature);
        fisrtFeature.setRequiredFeatures(features);
        update(fisrtFeature);
    }

    //TODO: remove code with Messages logic
    public SelectedFeatures unblockFeatures(List<Feature> features) {
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(features));
        for (Feature blockingfeature : features) {
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

    public List<Feature> returnUnblockedFeatures(Integer id, Integer secondId) {
        List<Feature> blockingFeatures = findAllBlockingFeatures();
        Integer index = blockingFeatures.indexOf(findById(secondId));
        Feature featureToDelete = blockingFeatures.get(index);
        List<Feature> features = featureToDelete.getBlockingFeatures();
        features.remove(findById(id));
        featureToDelete.setBlockingFeatures(features);
        update(featureToDelete);
        blockingFeatures.remove(featureToDelete);
        return blockingFeatures;
    }

    public List<Feature> dismissRequiredFeatures(Integer id, Integer secondId) {
        List<Feature> requiredFeatures = findAllRequiredFeatures();
        Integer index = requiredFeatures.indexOf(findById(secondId));
        Feature featureToDelete = requiredFeatures.get(index);
        List<Feature> features = featureToDelete.getRequiredFeatures();
        features.remove(findById(id));
        featureToDelete.setRequiredFeatures(features);
        update(featureToDelete);
        requiredFeatures.remove(featureToDelete);
        return requiredFeatures;
    }

    public String listFeatures(Integer page, ModelMap model) {
        List<Feature> features = findAll();
        PagedListHolder<Feature> pagedListHolder = new PagedListHolder<Feature>(features);
        pagedListHolder.setPageSize(15);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(0);
            model.addAttribute("features", pagedListHolder.getPageList());
        } else if (page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page - 1);
            model.addAttribute("features", pagedListHolder.getPageList());
        }
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "featuresList";
    }

    public String newFeature(ModelMap model) {
        Feature feature = new Feature();
        model.addAttribute("feature", feature);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "featureRegistration";
    }

    public String saveFeature(Feature feature, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "featureRegistration";
        }

        if (!isFeatureUnique(feature.getFeatureName())) {
            FieldError nameError = new FieldError("feature", "name", messageSource.getMessage("non.unique.name", new String[]{feature.getFeatureName()}, Locale.getDefault()));
            result.addError(nameError);
            model.addAttribute("message", "This option already exists");
            return "errorPage";
        }

        persist(feature);
        model.addAttribute("message", "Feature " + feature.getFeatureName() + " " + " added successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String chooseFeature(Integer id, ModelMap model, HttpSession session) {
        Customer user = userService.findBySSO(userService.getPrincipal());
        Contract contract = contractService.findUserContract(user);
        List<Feature> features = findFeatureByContract(contract.getContractId());
        Feature chosenFeature = findById(id);
        for (Feature feature : features) {
            if (feature.getFeatureId() == chosenFeature.getFeatureId()) {
                String featureIsAlreadyChosen = messageSource.getMessage("feature.is.already.chosen", new String[]{Integer.toString(chosenFeature.getFeatureId())}, Locale.getDefault());
                model.addAttribute("message", featureIsAlreadyChosen);
                model.addAttribute("loggedinuser", userService.getPrincipal());
                return "errorPage";
            }
        }

        Cart cart = (Cart) session.getAttribute("cart");
        cart.setOptionsInCart(features);
        session.setAttribute("optionsInCart", features);
        session.setAttribute("cart", cart);
        model.addAttribute("userFeatures", features);
        model.addAttribute("contracts", contractService.findByCustomerId(user));
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect: /cart";
    }

    public String editTarif(Integer id, ModelMap model) {
        Feature feature = findById(id);
        model.addAttribute("feature", feature);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "featureRegistration";
    }

    public String updateFeature(Feature feature, BindingResult result, ModelMap model, Integer id) {
        if (result.hasErrors()) {
            return "featureRegistration";
        }
        update(feature);
        model.addAttribute("message", "Feature " + feature.getFeatureName() + " " + " updated successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String deleteFeatureFromContract(Integer id, ModelMap model) {
        Customer user = userService.findBySSO(userService.getPrincipal());
        Contract updatedContract = deletedFeatureFromContract(id, user);
        List<Feature> userFeatures = findFeatureByContract(updatedContract.getContractId());
        model.addAttribute("userFeatures", userFeatures);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/contracts/myContract";
    }

    public String seeBlockingFeatures(ModelMap model) {
        List<Feature> blockingFeatures = findAllBlockingFeatures();
        List<MessagesList> messagesList = new ArrayList<MessagesList>();
        if (blockingFeatures.isEmpty()) {
            model.addAttribute("message", "There is no blocked features yet");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        Set<Feature> blockingFeaturesSet = new HashSet<Feature>(blockingFeatures);

        for (Feature blockingfeature : blockingFeaturesSet) {
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
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "blockingFeaturesList";
    }

    public String blockingFeatures(ModelMap model) {
        List<Feature> features = findAll();
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(findAll()));
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", features);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "blockingFeatures";
    }

    public String blockingFeatures(SelectedFeatures selectedFeaturesIds, BindingResult result, ModelMap model) {
        List<Feature> blockingFeatures = new ArrayList<Feature>();
        blockingFeatures.addAll(selectedFeaturesIds.getSelectedFeatures());
        if (blockingFeatures.size() == 2) {
            createBlockingFeatures(blockingFeatures);
        } else {
            model.addAttribute("message", "You can choose only two options");
            model.addAttribute("loggedinuser", getPrincipalMessageForUserService(userService));
            return "errorPage";
        }

        if (result.hasErrors()) {
            model.addAttribute("message", "OOOPS");
            model.addAttribute("loggedinuser", getPrincipalMessageForUserService(userService));
            return "errorPage";
        }
        model.addAttribute("features", blockingFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipalMessageForUserService(userService));
        return "redirect:/features/blockingFeatures/seeAll";
    }

    public String requiredFeatures(ModelMap model) {
        List<Feature> features = findAll();
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(findAll()));
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", features);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "requiredFeatures";
    }

    public String requiredFeatures(SelectedFeatures selectedFeaturesIds, BindingResult result, ModelMap model) {
        List<Feature> requiredFeatures = selectedFeaturesIds.getSelectedFeatures();
        if (requiredFeatures.size() > 2) {
            model.addAttribute("message", "You can choose only two options");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        createRequiredFeatures(requiredFeatures);

        if (result.hasErrors()) {
            model.addAttribute("message", "OOOPS");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        model.addAttribute("features", requiredFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/features/requiredFeatures/seeAll";
    }

    public String seeRequiredFeatures(ModelMap model) {

        List<Feature> requiredFeatures = findAllRequiredFeatures();
        List<MessagesList> messagesList = new ArrayList<MessagesList>();
        if (requiredFeatures.isEmpty()) {
            model.addAttribute("message", "There is no required features yet");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        Set<Feature> requiredFeaturesSet = new HashSet<Feature>(requiredFeatures);
        for (Feature requiredfeature : requiredFeaturesSet) {
            List<Feature> featuresToDisplay = requiredfeature.getRequiredFeatures();
            MessagesList message = new MessagesList();
            message.setMessageFeature(requiredfeature);
            List<String> names = new ArrayList<String>();
            for (Feature feature : featuresToDisplay) {
                String name = feature.getFeatureName();
                names.add(name);
            }
            message.setMessageList(names);
            messagesList.add(message);
        }
        model.addAttribute("messages", messagesList);
        model.addAttribute("features", requiredFeaturesSet);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "requiredFeaturesList";
    }

    public String unblockFeatures(ModelMap model) {
        List<Feature> features = findAllBlockingFeatures();
        SelectedFeatures selectedFeatures = unblockFeatures(features);
        HashSet<Feature> set = new HashSet<Feature>(features);
        model.addAttribute("selectedFeatures", selectedFeatures);
        model.addAttribute("features", set);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "unblockFeatures";
    }

    public String unblockFeatures(Integer id, Integer secondId, ModelMap model) {
        List blockingFeatures = returnUnblockedFeatures(id, secondId);
        model.addAttribute("features", blockingFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/features/unblockFeatures";
    }

    public String dismissRequiredFeatures(ModelMap model) {
        List<Feature> features = findAllRequiredFeatures();
        HashSet<Feature> set = new HashSet<Feature>(features);
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        selectedFeatures.setSelectedFeatures(new ArrayList<Feature>(features));
        for (Feature requiredFeature : features) {
            List<Feature> featuresToDisplay = requiredFeature.getRequiredFeatures();
            MessagesList message = new MessagesList();
            message.setMessageFeature(requiredFeature);
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
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "dismissRequiredFeatures";
    }

    public String dismissRequiredFeatures(Integer id, Integer secondId, ModelMap model) {
        List<Feature> requiredFeatures = dismissRequiredFeatures(id, secondId);
        model.addAttribute("features", requiredFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "redirect:/features/dismissRequiredFeatures";
    }

    private String getPrincipalMessageForUserService(CustomerService userService) {
        return userService != null ? userService.getPrincipal() : NO_USERSERVICE_DEFINED;
    }

}
