package eCare.model.services;

import eCare.model.PO.Contract;
import eCare.model.PO.Tarif;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public interface TarifService {

    List<Tarif> findByName(String name);

    void persist(Tarif entity);

    void update(Tarif entity);

     Tarif findById(Integer id);

//    void deleteById(Integer id);

    void delete(Integer id);

    List<Tarif> findAll();

    void deleteAll();

    boolean isTarifUnique(String name);

}
