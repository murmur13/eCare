package eCare.model;

import eCare.model.PO.*;
import eCare.model.services.*;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class App {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        Customer customer1 = new Customer("Katty", "Blood", "1990-01-21", "567546765er65767", "qerty", "lonely@com.ru");
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
        Tarif tarif1 = new Tarif("bezlimit25");
        Tarif tarif2 = new Tarif("more sms25");
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
        Contract contract1 = new Contract("8-91107675");
        contract1.setCustomer(customer1);
        contract1.setTarif(tarif1);
        Contract contract2 = new Contract("8-92771-335");
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
        FeatureService featureService = new FeatureService();
        Feature feature1 = new Feature("first feature", 15.00, 6.3);
        Feature feature2 = new Feature("second feature1", 13.13, 2);
        System.out.println("*** Persist FEATURE - start ***");
        featureService.persist(feature1);
        featureService.persist(feature2);
        feature1.setFeatureTarifs(tarifList);
        featureService.update(feature1);
        System.out.println("*** Persist - end ***");

        System.out.println("*** BLOCKING FEATURES ***");
        BlockingFeaturesService blockingFeaturesService = new BlockingFeaturesService();
        BlockingFeatures block1 = new BlockingFeatures("blocking feature 1");
        BlockingFeatures block2 = new BlockingFeatures("blocking feature 2");
        System.out.println("*** Persist blocking features start***");
        blockingFeaturesService.persist(block1);
        blockingFeaturesService.persist(block2);
        List<Feature> featuresList = featureService.findAll();
        block1.setBlockingFeatures(featuresList);
        blockingFeaturesService.update(block1);
        System.out.println("*** BLOCKING FEATURES***");

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

