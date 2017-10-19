package eCare.model;

import eCare.model.DAO.Customer;
import eCare.model.services.CustomerService;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class App {

    public static void main(String[] args) {
        CustomerService customerService = new CustomerService();
        Customer customer1 = new Customer("Katty", "Blood", "55676787", "19-01-1990");
        Customer customer2 = new Customer("234545", "War@com.ru", "pass", "Ann");
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
        System.out.println("*** Delete - start ***");
        Integer id2 = customer2.getId();
        customerService.delete(id2);
        System.out.println("Deleted customer with id " + id2 + ".");
        System.out.println("Now all customers are " + customerService.findAll().size() + ".");
        System.out.println("*** Delete - end ***");
        System.out.println("*** FindAll - start ***");
        List<Customer> customerList2 = customerService.findAll();
        System.out.println("Customers found are :");
        for (Customer b : customerList2) {
            System.out.println("-" + b.toString());
        }
        System.out.println("*** FindAll - end ***");
        System.out.println("*** DeleteAll - start ***");
        customerService.deleteAll();
        System.out.println("Customers found are now " + customerService.findAll().size());
        System.out.println("*** DeleteAll - end ***");
        System.exit(0);
    }
}

