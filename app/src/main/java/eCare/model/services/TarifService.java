package eCare.model.services;

import eCare.model.po.Tarif;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public interface TarifService {

    List<Tarif> findByName(String name);

    void persist(Tarif entity);

    void update(Tarif entity);

     Tarif findById(Integer id);

    void delete(Integer id);

    List<Tarif> findAll();

    void deleteAll();

    boolean isTarifUnique(String name);

    Tarif editTarifAndSendToQueue(Tarif tarif);

    String listTarifs(Integer page, ModelMap model);

    String newTarif(ModelMap model);

    String saveTarif(Tarif tarif, BindingResult result, ModelMap model);

    String editTarif(Integer id, ModelMap model);

    String updateTarif(Tarif tarif, BindingResult result, ModelMap model, Integer id);

    String deleteTarif(Integer id, ModelMap model);

}
