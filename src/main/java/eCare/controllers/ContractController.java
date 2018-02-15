package eCare.controllers;

import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Tarif;
import eCare.model.services.ContractService;
import eCare.model.services.CustomerService;
import eCare.model.services.FeatureService;
import eCare.model.services.TarifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 17.01.2018.
 */
@Controller
@RequestMapping("/contracts")
//@SessionAttributes("contract")
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
    public String listContracts(ModelMap model) {

        List<Contract> contracts = contractService.findAll();
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractslist";
    }

    @RequestMapping(value = {"/getMyContract" }, method = RequestMethod.GET)
    public String getMyContract(ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
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
    @ModelAttribute
    @RequestMapping(value = { "/newcontract" }, method = RequestMethod.POST)
    public String saveContract(@RequestParam(value = "tNumber", required = false) String phone, @RequestParam(value = "customer", required = false) String sso,
                               @RequestParam(value = "tarif", required = false) String tarif,
                               ModelMap model) {


        Customer customer = userService.findBySSO(sso);
        Tarif tarif1 = tarifService.findById(Integer.parseInt(tarif));
        Contract contract = new Contract(phone, customer, tarif1);
        model.addAttribute("customer", customer);
        model.addAttribute("tNumber", phone);
//        if (result.hasErrors()) {
//            return "contractRegistration";
//        }

        model.addAttribute("contract", contract);

        contractService.persist(contract);
        model.addAttribute("tarif", tarif1);
        model.addAttribute("success", "Contract " + contract.getContractId() + " " + " added successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        //return "success";
        return "registrationsuccess";
    }

    @RequestMapping(value = {"/contract/{id}/details" }, method = RequestMethod.GET)
    public String seeContractDetails(@PathVariable("id") int id, ModelMap model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractslist";
    }

    /**
     * This method will provide the medium to update an existing contract.
     */
    @RequestMapping(value = { "/edit-contract-{contractId}" }, method = RequestMethod.GET)
    public String editContract(@PathVariable String contractId, ModelMap model) {
        Contract contract = contractService.findById(Integer.parseInt(contractId));
        String customer = contract.getCustomer().getSsoId();
        String tarif = contract.getTarif().getName();
        model.addAttribute("tarif", tarif);
        model.addAttribute("customer", customer);
        model.addAttribute("contract", contract);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractRegistration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating contract in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-contract-{contractId}" }, method = RequestMethod.POST)
    public String updateContract(Contract contract, BindingResult result,
                             ModelMap model, @PathVariable String contractId) {

        if (result.hasErrors()) {
            return "error";
        }

        contractService.update(contract);
        model.addAttribute("success", "Contract " + contract.getContractId() + " "+ " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "contractslist";
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
