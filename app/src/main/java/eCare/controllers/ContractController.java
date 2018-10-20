package eCare.controllers;

import eCare.model.po.Cart;
import eCare.model.po.SelectedFeatures;
import eCare.model.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by echerkas on 17.01.2018.
 */
@Controller
@RequestMapping("/contracts")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@SessionAttributes("contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    /**
     * This method will list all existing contracts.
     */
    @RequestMapping(value = {"/listContracts"}, method = RequestMethod.GET)
    public String listContracts(@RequestParam(required = false) Integer page, ModelMap model) {
        String view = contractService.listContracts(page, model);
        return view;
    }

    @RequestMapping(value = {"/myContract"}, method = RequestMethod.GET)
    public String getMyContract(ModelMap model, HttpSession session) {
        String view = contractService.getMyContract(model, session);
        return view;
    }

    @RequestMapping(value = {"/newcontract"}, method = RequestMethod.GET)
    public String newContract(ModelMap model) {
        String view = contractService.newContract(model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving contract in database. It also validates the user input
     */
    @RequestMapping(value = {"/newcontract"}, method = RequestMethod.POST)
    public String saveContract(@RequestParam(value = "tNumber", required = false) String phone, @RequestParam(value = "customer", required = false) String sso,
                               @RequestParam(value = "tarif", required = false) String tarif,
                               ModelMap model) {

        String view = contractService.saveContract(phone, sso, tarif, model);
        return view;
    }

    @RequestMapping(value = {"/contract/{id}/details"}, method = RequestMethod.GET)
    public String seeContractDetails(@PathVariable("id") int id, ModelMap model) {
        String view = contractService.seeContractDetails(id, model);
        return view;
    }

    @RequestMapping(value = {"/{id}/showOptions"}, method = RequestMethod.GET)
    public ModelAndView seeContractOptions(@PathVariable("id") int id, HttpSession session) {
        ModelAndView view = contractService.seeContractOptions(id, session);
        return view;
    }

    @RequestMapping(value = {"/{id}/changeTarif"}, method = RequestMethod.POST)
    public String changeTarif(@PathVariable Integer id, ModelMap model, HttpSession session) {
        String view = contractService.changeTarif(id, model, session);
        return view;
    }

    @RequestMapping(value = {"/{featureId}/deleteFromContract"}, method = RequestMethod.POST)
    public String deleteFeatureFromContract(@PathVariable Integer featureId,
                                            ModelMap model, HttpSession session) {
        String view = contractService.deleteFeatureFromContract(featureId, model, session);
        return view;
    }

    /**
     * This method will provide the medium to update an existing contract.
     */

    @RequestMapping(value = {"/{contractId}/editContractTarif"}, method = RequestMethod.GET)
    public String editContract(@PathVariable Integer contractId, ModelMap model) {
        String view = contractService.editContract(contractId, model);
        return view;
    }

    @RequestMapping(value = {"/{contractId}/editContractTarif"}, method = RequestMethod.POST)
    public String updateContract(@PathVariable Integer contractId, Integer tarifId, ModelMap model) {
        String view = contractService.updateContract(contractId, tarifId, model);
        return view;
    }

    @RequestMapping(value = {"/{contractId}/editContractOptions"}, method = RequestMethod.GET)
    public String editContractOptions(@PathVariable Integer contractId, ModelMap model, HttpSession session) {
        String view = contractService.editContractOptions(contractId, model, session);
        return view;
    }

    @RequestMapping(value = {"/{contractId}/editContractOptions"}, method = RequestMethod.POST)
    public String updateContractOptions(@PathVariable Integer contractId, @ModelAttribute(value = "selectedFeatures") SelectedFeatures selectedFeaturesIds, BindingResult result,
                                        ModelMap model, Cart cart, HttpSession session) {
        String view = contractService.updateContractOptions(contractId, selectedFeaturesIds, result, model, cart, session);
        return view;
    }


    @RequestMapping(value = {"/{id}/setTarif"}, method = RequestMethod.POST)
    public String setTarif(@PathVariable Integer id, ModelMap model, HttpSession session) {
        String view = contractService.setTarif(id, model, session);
        return view;
    }

    @RequestMapping(value = {"/{id}/deleteContract"}, method = RequestMethod.POST)
    public String deleteContract(@PathVariable Integer id, ModelMap model) {
        String view = contractService.deleteContract(id, model);
        return view;
    }

    @RequestMapping(value = {"/{id}/block"}, method = RequestMethod.GET)
    public String blockContract(@PathVariable Integer id, ModelMap model, HttpSession session) {
        String view = contractService.blockContract(id, model, session);
        return view;
    }

    @RequestMapping(value = {"/{id}/unblock"}, method = RequestMethod.GET)
    public String unblockContract(@PathVariable Integer id, ModelMap model, HttpSession session) {
        String view = contractService.unblockContract(id, model, session);
        return view;
    }

    @RequestMapping(value = {"/contract/{id}/phoneNumber"}, method = RequestMethod.GET)
    public String generateNumber(@PathVariable Integer id, ModelMap model) {
        String view = contractService.generateNumber(id, model);
        return view;
    }
}
