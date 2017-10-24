package eCare.model;

import eCare.model.PO.Contract;
import eCare.model.PO.Customer;
import eCare.model.PO.Option;
import eCare.model.PO.Tarif;
import eCare.model.services.ContractService;
import eCare.model.services.CustomerService;
import eCare.model.services.OptionService;
import eCare.model.services.TarifService;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class App {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        Customer customer1 = new Customer("Katty", "Blood", "1990-01-21", "56754656765767", "qerty", "lonely@com.ru");
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
        Tarif tarif1 = new Tarif("bezlimit");
        Tarif tarif2 = new Tarif("more sms");
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
        Contract contract1 = new Contract("8-911057gh64675");
        contract1.setCustomer(customer1);
        contract1.setTarif(tarif1);
        Contract contract2 = new Contract("8-92671-36635");
        contract2.setCustomer(customer2);
        contract2.setTarif(tarif2);
        System.out.println("contract2 has customer (name) = " + contract2 + customer2.getName());
        System.out.println("contract2 has customer (surname) = " + contract2 + customer2.getSurname());
        System.out.println("contract2 has customer (birthdate)= " + contract2 + customer2.getBirthDate());
        customer1.setName("SELINA");
        customer1.setSurname("Kyle");
        customer1.setBirthDate("1990-01-30");
        customer1.setPassportData("catwoman");
        customerService.update(customer1);
        System.out.println("customer1 has passport of  = " + contract1 + customer1.getPassportData());
        System.out.println("customer1 lives in = " + customer1.getName() + customer1.getAddress());
        System.out.println("contract1 telNumber= " + contract1 + customer1.getTelNumber());
        customer1.setTelNumber("66666");
        System.out.println("contract1 and new number = " + customer1.getTelNumber());
        System.out.println("*** Persist - start ***");
        contractService.persist(contract1);
        contractService.persist(contract2);
        List<Contract> contractList = contractService.findAll();
        System.out.println("Contracts Persisted are :");
        for(Contract c : contractList){
            System.out.println("-" + c.toString());
        }
        OptionService optionService = new OptionService();
        Option option1 = new Option("firstOption", 15.00, 6.3);
        Option option2 = new Option("second Option", 13.13, 2);
        System.out.println("*** Persist OPTION - start ***");
        optionService.persist(option1);
        optionService.persist(option2);
        option1.setOptionTarifs(tarifList);
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

