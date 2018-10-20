package eCare.controllers;

import eCare.model.po.Tarif;
import eCare.model.services.TarifService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by echerkas on 06.12.2017.
 */
@Controller
@Scope("singleton")
@RequestMapping("/tarifs")
public class TarifController {

    Logger logger = LoggerFactory.getLogger(TarifController.class);

    @Autowired
    private TarifService tarifService;

    /**
     * This method will list all existing tarifs.
     */
    @RequestMapping(value = {"/listTarifs"}, method = RequestMethod.GET)
    public String listTarifs(@RequestParam(required = false) Integer page, ModelMap model) {
        String view = tarifService.listTarifs(page, model);
        return view;
    }

    @RequestMapping(value = {"/newtarif"}, method = RequestMethod.GET)
    public String newTarif(ModelMap model) {
        String view = tarifService.newTarif(model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = {"/newtarif"}, method = RequestMethod.POST)
    public String saveTarif(@Valid Tarif tarif, BindingResult result,
                            ModelMap model) {
        String view = tarifService.saveTarif(tarif, result, model);
        return view;
    }


    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = {"/{id}/editTarif"}, method = RequestMethod.GET)
    public String editTarif(@PathVariable Integer id, ModelMap model) {
        String view = tarifService.editTarif(id, model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = {"/{id}/editTarif"}, method = RequestMethod.POST)
    public String updateTarif(@Valid Tarif tarif, BindingResult result,
                              ModelMap model, @PathVariable Integer id) {
        String view = tarifService.updateTarif(tarif, result, model, id);
        return view;
    }

    @RequestMapping(value = {"/{id}/deleteTarif"}, method = RequestMethod.GET)
    public String deleteTarif(@PathVariable Integer id, ModelMap model) {
        String view = tarifService.deleteTarif(id, model);
        return view;
    }

}
