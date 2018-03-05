package eCare.controllers;

import eCare.model.PO.Customer;
import eCare.model.PO.Tarif;
import eCare.model.services.ContractService;
import eCare.model.services.TarifService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 06.12.2017.
 */
@Controller
@RequestMapping("/tarifs")
public class TarifController {

    @Autowired
    TarifService tarifService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ContractService contractService;

    /**
     * This method will list all existing tarifs.
     */
    @RequestMapping(value = {"/listTarifs" }, method = RequestMethod.GET)
    public String listTarifs(@RequestParam(required = false) Integer page, ModelMap model) {
        List<Tarif> tarifs = tarifService.findAll();

        PagedListHolder<Tarif> pagedListHolder = new PagedListHolder<Tarif>(tarifs);
        pagedListHolder.setPageSize(15);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            model.addAttribute("tarifs", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            model.addAttribute("tarifs", pagedListHolder.getPageList());
        }
//        model.addAttribute("tarifs", tarifs);
        model.addAttribute("loggedinuser", getPrincipal());
        return "tarifslist";
    }

    @RequestMapping(value = { "/newtarif" }, method = RequestMethod.GET)
    public String newTarif(ModelMap model) {
        Tarif tarif = new Tarif();
        model.addAttribute("tarif", tarif);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
        return "tarifRegistration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = { "/newtarif" }, method = RequestMethod.POST)
    public String saveTarif(@Valid Tarif tarif, BindingResult result,
                           ModelMap model) {

        if (result.hasErrors()) {
            return "tarifRegistration";
        }

        if (!tarifService.isTarifUnique(tarif.getName())) {
            FieldError nameError = new FieldError("tarif", "name", messageSource.getMessage("non.unique.name", new String[]{tarif.getName()}, Locale.getDefault()));
            result.addError(nameError);
            return "tarifRegistration";
        }

            tarifService.persist(tarif);
            model.addAttribute("success", "Tarif " + tarif.getName() + " " + " added successfully");
            model.addAttribute("loggedinuser", getPrincipal());
            //return "success";
            return "registrationsuccess";
        }


    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = { "/edit-tarif-{id}" }, method = RequestMethod.GET)
    public String editTarif(@PathVariable Integer id, ModelMap model) {
        Tarif tarif = tarifService.findById(id);
        model.addAttribute("tarif", tarif);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "tarifRegistration";
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-tarif-{id}" }, method = RequestMethod.POST)
    public String updateTarif(@Valid Tarif tarif, BindingResult result,
                             ModelMap model, @PathVariable Integer id) {

        if (result.hasErrors()) {
            return "tarifRegistration";
        }
        tarifService.update(tarif);
        model.addAttribute("success", "Tarif " + tarif.getName() + " " + " updated successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    @RequestMapping(value = { "/delete-tarif-{id}" }, method = RequestMethod.GET)
    public String deleteTarif(@PathVariable Integer id, ModelMap model) {
        Tarif tarif = tarifService.findById(id);
        if(!contractService.findContractByTarif(tarif).isEmpty()){
            String tarifNotDeleted = messageSource.getMessage("tarif.not.deletable", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifNotDeleted);
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
        tarifService.delete(id);
        return "redirect:/tarifs/listTarifs";
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
