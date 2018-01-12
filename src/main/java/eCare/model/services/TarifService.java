package eCare.model.services;

import eCare.model.PO.Tarif;
import eCare.model.DAO.TarifDAO;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public interface TarifService {

//    private static TarifDAO tarifDAO;

//    public TarifService() {
//        tarifDAO = new TarifDAO();
//    }

    List<Tarif> findByName(String name);

    void persist(Tarif entity);

    void update(Tarif entity);

     Tarif findById(Integer id);

//    void deleteById(Integer id);

    void delete(Integer id);

    List<Tarif> findAll();

    void deleteAll();

    boolean isTarifUnique(String name);

//    TarifDAO tarifDAO() {
//        return tarifDAO;
//    }
}
