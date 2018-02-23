package eCare.model.services;

import eCare.model.PO.Contract;
import eCare.model.DAO.ContractDAO;
import eCare.model.PO.Customer;
import eCare.model.PO.Feature;
import eCare.model.PO.Tarif;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by echerkas on 20.10.2017.
 */
public interface ContractService {

    List<Contract> findByPhone(String telNumber);

    List<Contract> findByCustomerId(Customer customerId);

    List<Contract> findContractByTarif(Tarif tarifId);

    void persist(Contract entity);

    void update(Contract entity);

    Contract findById(Integer id);

//    void deleteById(Integer id);

    void delete(Integer id);

    List<Contract> findAll();

    void deleteAll();

}
