package eCare.model.services;

import eCare.model.DAO.ContractDAO;
import eCare.model.DAO.CustomerDAO;
import eCare.model.DAO.TarifDAO;
import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Feature;
import eCare.model.PO.Tarif;
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

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    TarifDAO tarifDAO;

    @Autowired
    CustomerServiceImpl userService;

    @Autowired
    TarifServiceImpl tarifService;

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

    public List<Contract> findByCustomerId(Customer customerId){
        Customer customer = customerDAO.findById(customerId.getId());
        List<Contract> contracts = contractDAO.findByCustomerId(customerId);
        return contracts;
    }

    public List<Contract> findContractByTarif(Tarif tarifId){
        Tarif tarif = tarifDAO.findById(tarifId.getTarifId());
        List<Contract>contracts = contractDAO.findContractByTarif(tarif);
        return contracts;
    }

    public Contract findUserContract(Customer user){
        List<Contract> contracts = findByCustomerId(user);
        Contract contract = contracts.get(0);
        return contract;
    }

    public Contract createNewContract(String phone, String sso, String tarif){
        Customer customer = userService.findBySSO(sso);
        Tarif tarif1 = tarifService.findById(Integer.parseInt(tarif));
        Contract contract = new Contract(phone, customer, tarif1);
        customer.setTelNumber(phone);
        userService.updateUser(customer);
        persist(contract);
        List<Contract> tarifContracts = findContractByTarif(tarif1);
        tarifContracts.add(contract);
        return contract;
    }

}
