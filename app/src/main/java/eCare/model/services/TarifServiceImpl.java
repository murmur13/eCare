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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 06.12.2017.
 */
@Service("tarifService")
@Transactional
public class TarifServiceImpl implements TarifService {

    @Autowired
    private TarifDao tarifDao;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private CamelContext camelContext;

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
        ex.getIn().setBody(tarif);
        messageSender.sendMessage(ex);
//        messageSender.sendMessage(String.valueOf(tarif.getTarifId()));
        return tarif;
    }
}
