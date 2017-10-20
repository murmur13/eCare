package eCare.model.services;

import eCare.model.DAO.Contract;
import eCare.model.DAO.ContractDAO;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public class ContractService {

    private static ContractDAO contractDAO;

    public ContractService() {
        contractDAO = new ContractDAO();
    }

    public void persist(Contract entity) {
        contractDAO.openCurrentSessionwithTransaction();
        contractDAO.persist(entity);
        contractDAO.closeCurrentSessionwithTransaction();
    }

    public void update(Contract entity) {
        contractDAO.openCurrentSessionwithTransaction();
        contractDAO.update(entity);
        contractDAO.closeCurrentSessionwithTransaction();
    }

    public Contract findById(Integer id) {
        contractDAO.openCurrentSession();
        Contract contract = contractDAO.findById(id);
        contractDAO.closeCurrentSession();
        return contract;
    }

    public void delete(Integer id) {
        contractDAO.openCurrentSessionwithTransaction();
        Contract contract = contractDAO.findById(id);
        contractDAO.delete(contract);
        contractDAO.closeCurrentSessionwithTransaction();
    }

    public List<Contract> findAll() {
        contractDAO.openCurrentSession();
        List<Contract> contractList = contractDAO.findAll();
        contractDAO.closeCurrentSession();
        return contractList;
    }

    public void deleteAll() {
        contractDAO.openCurrentSessionwithTransaction();
        contractDAO.deleteAll();
        contractDAO.closeCurrentSessionwithTransaction();
    }

    public ContractDAO contractDAO() {
        return contractDAO;
    }
}
