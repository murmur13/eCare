package eCare.model.services;



import eCare.model.DAO.OptionDAO;
import eCare.model.PO.Option;

import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */
public class OptionService {

    private static OptionDAO optionDAO;

    public OptionService() {
        optionDAO = new OptionDAO();
    }

    public void persist(Option entity) {
        optionDAO.openCurrentSessionwithTransaction();
        optionDAO.persist(entity);
        optionDAO.closeCurrentSessionwithTransaction();
    }

    public void update(Option entity) {
        optionDAO.openCurrentSessionwithTransaction();
        optionDAO.update(entity);
        optionDAO.closeCurrentSessionwithTransaction();
    }

    public Option findById(Integer id) {
        optionDAO.openCurrentSession();
        Option option = optionDAO.findById(id);
        optionDAO.closeCurrentSession();
        return option;
    }

    public void delete(Integer id) {
        optionDAO.openCurrentSessionwithTransaction();
        Option option = optionDAO.findById(id);
        optionDAO.delete(option);
        optionDAO.closeCurrentSessionwithTransaction();
    }

    public List<Option> findAll() {
        optionDAO.openCurrentSession();
        List<Option> optionList = optionDAO.findAll();
        optionDAO.closeCurrentSession();
        return optionList;
    }

    public void deleteAll() {
        optionDAO.openCurrentSessionwithTransaction();
        optionDAO.deleteAll();
        optionDAO.closeCurrentSessionwithTransaction();
    }

    public OptionDAO optionDAO() {
        return optionDAO;
    }
}
