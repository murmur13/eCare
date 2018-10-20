package eCare.controllers;

import eCare.model.po.Feature;
import eCare.model.po.SelectedFeatures;
import eCare.model.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by echerkas on 13.01.2018.
 */
@Controller
@Scope("singleton")
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    /**
     * This method will list all existing users.
     */
    @RequestMapping(value = {"/listFeatures"}, method = RequestMethod.GET)
    public String listFeatures(@RequestParam(required = false) Integer page, ModelMap model) {
        String view = featureService.listFeatures(page, model);
        return view;
    }

    @RequestMapping(value = {"/newfeature"}, method = RequestMethod.GET)
    public String newFeature(ModelMap model) {
        String view = featureService.newFeature(model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = {"/newfeature"}, method = RequestMethod.POST)
    public String saveFeature(@Valid Feature feature, BindingResult result,
                              ModelMap model) {

        String view = featureService.saveFeature(feature, result, model);
        return view;
    }

    @RequestMapping(value = {"/{id}/chooseFeature"}, method = RequestMethod.GET)
    public String chooseFeature(@PathVariable Integer id, ModelMap model, HttpSession session) {
        String view = featureService.chooseFeature(id, model, session);
        return view;
    }


    /**
     * This method will provide the medium to update an existing feature.
     */
    @RequestMapping(value = {"/{id}/editFeature"}, method = RequestMethod.GET)
    public String editTarif(@PathVariable Integer id, ModelMap model) {
        String view = featureService.editTarif(id, model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = {"/{id}/editFeature"}, method = RequestMethod.POST)
    public String updateFeature(@Valid Feature feature, BindingResult result,
                                ModelMap model, @PathVariable Integer id) {
        String view = featureService.updateFeature(feature, result, model, id);
        return view;
    }

    @RequestMapping(value = {"/{id}/deleteFeature"}, method = RequestMethod.GET)
    public String deleteFeature(@PathVariable Integer id) {
        featureService.delete(id);
        return "redirect:/features/listFeatures";
    }

    @RequestMapping(value = {"/{id}/deleteFeatureFromContract"}, method = RequestMethod.GET)
    public String deleteFeatureFromContract(@PathVariable Integer id, ModelMap model) {
        String view = featureService.deleteFeatureFromContract(id, model);
        return view;
    }

    @RequestMapping(value = {"/blockingFeatures/seeAll"}, method = RequestMethod.GET)
    public String seeBlockingFeatures(ModelMap model) {
        String view = featureService.seeBlockingFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/blockingFeatures"}, method = RequestMethod.GET)
    public String blockingFeatures(ModelMap model) {
        String view = featureService.blockingFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/blockingFeatures"}, method = RequestMethod.POST)
    public String blockingFeatures(@ModelAttribute(value = "selectedFeatures") SelectedFeatures selectedFeaturesIds, BindingResult result,
                                   ModelMap model) {
        String view = featureService.blockingFeatures(selectedFeaturesIds, result, model);
        return view;

    }

    @RequestMapping(value = {"/requiredFeatures"}, method = RequestMethod.GET)
    public String requiredFeatures(ModelMap model) {
        String view = featureService.requiredFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/requiredFeatures"}, method = RequestMethod.POST)
    public String requiredFeatures(@ModelAttribute(value = "selectedFeatures") SelectedFeatures selectedFeaturesIds, BindingResult result,
                                   ModelMap model) {
        String view = featureService.requiredFeatures(selectedFeaturesIds, result, model);
        return view;
    }

    @RequestMapping(value = {"/requiredFeatures/seeAll"}, method = RequestMethod.GET)
    public String seeRequiredFeatures(ModelMap model) {
        String view = featureService.seeRequiredFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/unblockFeatures"}, method = RequestMethod.GET)
    public String unblockFeatures(ModelMap model) {
        String view = featureService.unblockFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/unblockFeatures/{id}/{secondId}"}, method = RequestMethod.GET)
    public String unblockFeatures(@PathVariable Integer id, @PathVariable Integer secondId,
                                  ModelMap model) {
        String view = featureService.unblockFeatures(id, secondId, model);
        return view;
    }

    @RequestMapping(value = {"/dismissRequiredFeatures"}, method = RequestMethod.GET)
    public String dismissRequiredFeatures(ModelMap model) {
        String view = featureService.dismissRequiredFeatures(model);
        return view;
    }

    @RequestMapping(value = {"/dismissRequiredFeatures/{id}/{secondId}"}, method = RequestMethod.GET)
    public String dismissRequiredFeatures(@PathVariable Integer id, @PathVariable Integer secondId,
                                          ModelMap model) {
        String view = featureService.dismissRequiredFeatures(id, secondId, model);
        return view;
    }
}
