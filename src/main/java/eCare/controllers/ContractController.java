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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 17.01.2018.
 */
@Controller
@RequestMapping("/contracts")
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

    /**
     * This method will list all existing contracts.
     */
    @RequestMapping(value = {"/listContracts" }, method = RequestMethod.GET)
    public String listContracts(ModelMap model) {

        List<Contract> contracts = contractService.findAll();
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", getPrincipal());
        return "contactslist";
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
    public String saveContract(@RequestParam("customerSSO") String customerSSO, @RequestParam("tarifId") String tarif, @Valid Contract contract, BindingResult result,
                               ModelMap model) {

        Customer customer = userService.findBySSO(customerSSO);
        Tarif tarif1 = tarifService.findById(Integer.parseInt(tarif));
        contract.setCustomer(customer);
        contract.setTarif(tarif1);
        if (result.hasErrors()) {
            return "contractRegistration";
        }

        contractService.persist(contract);
        model.addAttribute("name", customerSSO);
        model.addAttribute("customer", customer);
        model.addAttribute("tarifId", tarif1);
        model.addAttribute("success", "Contract " + contract.getContractId() + " " + " added successfully");
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
