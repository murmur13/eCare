package eCare.model.services;

import eCare.configuration.MessageSender;
import eCare.model.dao.TarifDao;
import eCare.model.po.Tarif;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by echerkas on 06.12.2017.
 */
@Service("tarifService")
@DependsOn("messageSource")
@Transactional
public class TarifServiceImpl implements TarifService {

    @Autowired
    private TarifDao tarifDao;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private CustomerService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ContractService contractService;

    public Tarif findById(Integer id) {
        return tarifDao.findById(id);
    }

    public void persist(Tarif tarif) {
        tarifDao.persist(tarif);
    }

    public void delete(Integer id) {
        Tarif tarif = tarifDao.findById(id);
        tarifDao.delete(tarif);
    }

    public void update(Tarif tarif) {
        tarifDao.update(tarif);
    }

    public List<Tarif> findAll() {
        return tarifDao.findAll();
    }

    public void deleteAll() {
        tarifDao.deleteAll();
    }

    public boolean isTarifUnique(String name) {
        boolean isUnique = false;
        List<Tarif> tarif = findByName(name);
        if (tarif == null || tarif.isEmpty()) {
            isUnique = true;
        }
        return isUnique;
    }

    public List<Tarif> findByName(String name) {
        List<Tarif> tarif = tarifDao.findByName(name);
        return tarif;
    }

    public void setTarifDAO(TarifDao tarifDAO) {
        this.tarifDao = tarifDAO;
    }

    public Tarif editTarifAndSendToQueue(Tarif tarif){
        update(tarif);
        Exchange ex = new DefaultExchange(camelContext);
        final UUID correlationID = createUuid();
        ex.getIn().setHeader("JMSCorrelationID", correlationID);
        ex.getIn().setBody(tarif.getTarifId());
        messageSender.sendMessage(ex);
        return tarif;
    }

    public UUID createUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public String listTarifs(Integer page, ModelMap model){
        List<Tarif> tarifs = findAll();

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
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "tarifslist";
    }

    public String newTarif(ModelMap model){
        Tarif tarif = new Tarif();
        tarif.setName("sdgsd");
        tarif.setPrice(13.0);
        tarif.setTarifId(0);
        model.addAttribute("tarif", tarif);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "tarifRegistration";
    }

    public String saveTarif(Tarif tarif, BindingResult result, ModelMap model){

        if (result.hasErrors()) {
            return "tarifRegistration";
        }

        if (!isTarifUnique(tarif.getName())) {
            FieldError nameError = new FieldError("tarif", "name", messageSource.getMessage("non.unique.name", new String[]{tarif.getName()}, Locale.getDefault()));
            result.addError(nameError);
            return "tarifRegistration";
        }

        persist(tarif);
        model.addAttribute("message", "Tarif " + tarif.getName() + " " + " added successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String editTarif(Integer id, ModelMap model){
        Tarif tarif = findById(id);
        model.addAttribute("tarif", tarif);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "tarifRegistration";
    }

    public String updateTarif(Tarif tarif, BindingResult result, ModelMap model, Integer id){
        if (result.hasErrors()) {
            return "tarifRegistration";
        }
        editTarifAndSendToQueue(tarif);
        model.addAttribute("message", "Tarif " + tarif.getName() + " " + " updated successfully");
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "registrationsuccess";
    }

    public String deleteTarif(Integer id, ModelMap model){
        Tarif tarif = findById(id);
        if(!contractService.findContractByTarif(tarif).isEmpty()){
            String tarifNotDeleted = messageSource.getMessage("tarif.not.deletable", new String[]{Integer.toString(tarif.getTarifId())}, Locale.getDefault());
            model.addAttribute("message", tarifNotDeleted);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        delete(id);
        return "redirect:/tarifs/listTarifs";
    }
}
