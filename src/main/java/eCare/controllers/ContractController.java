package eCare.controllers;

import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Feature;
import eCare.model.PO.Tarif;
import eCare.model.services.ContractService;
import eCare.model.services.CustomerService;
import eCare.model.services.FeatureService;
import eCare.model.services.TarifService;
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
    CustomerService userService;

    @Autowired
    FeatureService featureService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    TarifService tarifService;

    @Autowired
    ContractService contractService;

    @Autowired
    AppController appController;

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
//        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractslist";
    }

    @RequestMapping(value = {"/getMyContract" }, method = RequestMethod.GET)
    public String getMyContract(ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = contracts.get(0);
        List <Feature> features = featureService.findFeatureByContract(contract.getContractId());
            model.addAttribute("userFeatures", features);
            model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "userContract";
    }

    @RequestMapping(value = { "/newcontract" }, method = RequestMethod.GET)
    public String newContract(ModelMap model) {
        Contract contract = new Contract();
        model.addAttribute("contract", contract);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
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
        Tarif tarif1 = tarifService.findById(Integer.parseInt(tarif));
        Contract contract = new Contract(phone, customer, tarif1);
//        if (result.hasErrors()) {
//            return "contractRegistration";
//        }
        customer.setTelNumber(phone);
        userService.updateUser(customer);
        contractService.persist(contract);
        List<Contract> tarifContracts = contractService.findContractByTarif(tarif1);
        tarifContracts.add(contract);
        model.addAttribute("tarif", tarif1);
        model.addAttribute("customer", customer);
        model.addAttribute("phone", customer.getTelNumber());
        model.addAttribute("contract", contract);
        model.addAttribute("success", "Contract " + contract.getContractId() + " " + " added successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/contract/{id}/details" }, method = RequestMethod.GET)
    public String seeContractDetails(@PathVariable("id") int id, ModelMap model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractslist";
    }

    @RequestMapping(value = {"/showOptions-{id}" }, method = RequestMethod.GET)
    public ModelAndView seeContractOptions(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView("featuresList");
        Contract contract = contractService.findById(id);
        List<Feature> contractFeatures = featureService.findFeatureByContract(id);
        if(!contractFeatures.isEmpty()){
            modelAndView.addObject("features", contractFeatures);
            modelAndView.addObject("loggedinuser", getPrincipal());
            return modelAndView;
    }
        else{
            ModelAndView modelAndView2 = new ModelAndView("errorPage");
            String contractWithoutOptions = messageSource.getMessage("contract.not.having.any.options", new String[]{Integer.toString(id)}, Locale.getDefault());
            modelAndView2.addObject("message", contractWithoutOptions);
            modelAndView2.addObject("loggedinuser", getPrincipal());
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
        if (!contract.getTarif().getName().equals(tarif.getName())) {
            contract.setTarif(tarif);
            List<Feature> features = featureService.findFeatureByContract(contract.getContractId());
            for (Feature feature : features) {
                featureService.delete(feature.getFeatureId());
            }
            contractService.update(contracts.get(0));
            model.addAttribute("features", features);
            model.addAttribute("contracts", contracts);
            model.addAttribute("loggedinuser", getPrincipal());
            return "userContract";
        } else {
            String tarifIsAlreadyChosen = messageSource.getMessage("tarif.is.already.chosen", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifIsAlreadyChosen);
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
    }

    /**
     * This method will provide the medium to update an existing contract.
     */

    @RequestMapping(value = { "/edit-contract-{contractId}" }, method = RequestMethod.GET)
    public String editContract(@PathVariable Integer contractId, ModelMap model, HttpServletRequest request) {
        Contract contract = contractService.findById(contractId);
        request.getSession().setAttribute("contract", contract);
        List<Tarif> tarifs = tarifService.findAll();
        List<Feature> features = featureService.findAll();
        Tarif userTarif = contract.getTarif();
        List <Feature> contractFeatures = featureService.findFeatureByContract(contractId);
//        Customer customer = contract.getCustomer();
        model.addAttribute("contract", contract);
        model.addAttribute("userTarif", userTarif);
        model.addAttribute("tarifs", tarifs);
        model.addAttribute("features", features);
        model.addAttribute("contractFeatures", contractFeatures);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "editContract";
    }

    @RequestMapping(value = { "/edit-contract-{contractId}" }, method = RequestMethod.POST)
    public String updateContract(@PathVariable Integer contractId, Contract contract,  ModelMap model, HttpServletRequest request, WebRequest webR){
//        Contract contract = contractService.findById(contractId);
        contractService.update(contract);
        model.get("contractFeatures");
        model.get("features");
        webR.removeAttribute("contract", webR.SCOPE_SESSION);
        model.addAttribute("loggedinuser", getPrincipal());
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
            model.addAttribute("loggedinuser", getPrincipal());
            return "userContract";
        } else {
            String tarifIsAlreadyChosen = messageSource.getMessage("tarif.is.already.chosen", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifIsAlreadyChosen);
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
    }

//    /**
//     * This method will be called on form submission, handling POST request for
//     * updating contract in database. It also validates the user input
//     */
//    @RequestMapping(value = { "/edit-contract-{contractId}" }, method = RequestMethod.POST)
//    public String updateContract(Contract contract, BindingResult result,
//                             ModelMap model, @PathVariable String contractId) {
//
//        if (result.hasErrors()) {
//            return "error";
//        }
//
//        contractService.update(contract);
//        model.addAttribute("success", "Contract " + contract.getContractId() + " "+ " updated successfully");
//        model.addAttribute("loggedinuser", getPrincipal());
//        return "contractslist";
//    }

    @RequestMapping(value = { "/delete-contract-{id}" }, method = RequestMethod.GET)
    public String deleteContract(@PathVariable Integer id, ModelMap model) {
        contractService.delete(id);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect:/contracts/listContracts";
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
