package eCare.model.services;

import eCare.model.DAO.ContractDAO;
import eCare.model.PO.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 17.01.2018.
 */

@Service("contractService")
@Transactional
public class ContractServiceImpl implements ContractService{

    @Autowired
    ContractDAO contractDAO;

    public Contract findById(Integer id) {
        return contractDAO.findById(id);
    }

    public void persist(Contract contract) {
        contractDAO.persist(contract);
    }

    public void delete(Integer id){
        Contract contract = contractDAO.findById(id);
        contractDAO.delete(contract);
    }

    public void update(Contract contract) {
        contractDAO.update(contract);
    }

    public List<Contract> findAll() {
        return contractDAO.findAll();
    }

    public void deleteAll(){
        contractDAO.deleteAll();
    }

    public List<Contract> findByPhone(String telNumber) {
        List<Contract> contract = contractDAO.findByPhone(telNumber);
        return contract;
    }
}
