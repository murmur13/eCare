package eCare.model.services;

import eCare.model.PO.Tarif;
import eCare.model.DAO.TarifDAO;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public class TarifService {

    private static TarifDAO tarifDAO;

    public TarifService() {
        tarifDAO = new TarifDAO();
    }

    public void persist(Tarif entity) {
        tarifDAO.openCurrentSessionwithTransaction();
        tarifDAO.persist(entity);
        tarifDAO.closeCurrentSessionwithTransaction();
    }

    public void update(Tarif entity) {
        tarifDAO.openCurrentSessionwithTransaction();
        tarifDAO.update(entity);
        tarifDAO.closeCurrentSessionwithTransaction();
    }

    public Tarif findById(Integer id) {
        tarifDAO.openCurrentSession();
        Tarif tarif = tarifDAO.findById(id);
        tarifDAO.closeCurrentSession();
        return tarif;
    }

    public void delete(Integer id) {
        tarifDAO.openCurrentSessionwithTransaction();
        Tarif tarif = tarifDAO.findById(id);
        tarifDAO.delete(tarif);
        tarifDAO.closeCurrentSessionwithTransaction();
    }

    public List<Tarif> findAll() {
        tarifDAO.openCurrentSession();
        List<Tarif> tarifList = tarifDAO.findAll();
        tarifDAO.closeCurrentSession();
        return tarifList;
    }

    public void deleteAll() {
        tarifDAO.openCurrentSessionwithTransaction();
        tarifDAO.deleteAll();
        tarifDAO.closeCurrentSessionwithTransaction();
    }

    public TarifDAO tarifDAO() {
        return tarifDAO;
    }
}
