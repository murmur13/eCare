package eCare.controllers;

import eCare.model.PO.Feature;
import eCare.model.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

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

    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/listFeatures" }, method = RequestMethod.GET)
    public String listFeatures(ModelMap model) {

        List<Feature> features = featureService.findAll();
        model.addAttribute("features", features);
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
