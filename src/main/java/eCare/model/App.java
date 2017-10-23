package eCare.model;

import eCare.model.DAO.Contract;
import eCare.model.DAO.Customer;
import eCare.model.DAO.Tarif;
import eCare.model.services.ContractService;
import eCare.model.services.CustomerService;
import eCare.model.services.TarifService;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class App {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        Customer customer1 = new Customer("Katty", "Blood", "1990-01-21", "6855687865673456", "qerty", "lonely@com.ru");
        Customer customer2 = new Customer("Ann", "Wayne", "2000-06-06", "@com", "pass");
        System.out.println("*** Persist - start ***");
        customerService.persist(customer1);
        customerService.persist(customer2);
        List<Customer> customerList = customerService.findAll();
        System.out.println("Customers Persisted are :");
        for (Customer b : customerList) {
            System.out.println("-" + b.toString());
        }
        System.out.println("*** Persist - end ***");
        System.out.println("*** Update - start ***");
        customer1.setAddress("Hell");
        customerService.update(customer1);
        System.out.println("Customer Updated is =>" +customerService.findById(customer1.getId()).toString());
        System.out.println("*** Update - end ***");
        System.out.println("*** Find - start ***");
        Integer id1 = customer1.getId();
        Customer another = customerService.findById(id1);
        System.out.println("Customer found with id " + id1 + " is =>" + another.toString());
        System.out.println("*** Find - end ***");

        TarifService tarifService = new TarifService();
        Tarif tarif1 = new Tarif("bezlimit10");
        Tarif tarif2 = new Tarif("more sms10");
        System.out.println("*** Persist - start ***");
        tarifService.persist(tarif1);
        tarifService.persist(tarif2);
        List<Tarif> tarifList = tarifService.findAll();
        System.out.println("Tarifs Persisted are :");
        for(Tarif t : tarifList){
            System.out.println("-" + t.toString());
        }
        System.out.println("*** Persist - end ***");

        ContractService contractService = new ContractService();
        Contract contract1 = new Contract("8-911056475", 1);
        Contract contract2 = new Contract("8-921-35435", 2);
        System.out.println("*** Persist - start ***");
        contractService.persist(contract1);
        contractService.persist(contract2);
        List<Contract> contractList = contractService.findAll();
        System.out.println("Contracts Persisted are :");
        for(Contract c : contractList){
            System.out.println("-" + c.toString());
        }
        System.out.println("*** Persist - end ***");




//        System.out.println("*** Delete - start ***");
//        Integer id2 = customer2.getId();
//        customerService.delete(id2);
//        System.out.println("Deleted customer with id " + id2 + ".");
//        System.out.println("Now all customers are " + customerService.findAll().size() + ".");
//        System.out.println("*** Delete - end ***");
//        System.out.println("*** FindAll - start ***");
//        List<Customer> customerList2 = customerService.findAll();
//        System.out.println("Customers found are :");
//        for (Customer b : customerList2) {
//            System.out.println("-" + b.toString());
//        }
//        System.out.println("*** FindAll - end ***");
//        System.out.println("*** DeleteAll - start ***");
//        customerService.deleteAll();
//        System.out.println("Customers found are now " + customerService.findAll().size());
//        System.out.println("*** DeleteAll - end ***");
//        System.exit(0);
    }
}

