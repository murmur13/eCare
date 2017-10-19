package eCare.model.services;

import eCare.model.DAO.Customer;
import eCare.model.DAO.CustomerDAO;

import java.util.List;

/**
 * Created by echerkas on 18.10.2017.
 */
public class CustomerService {

        private static CustomerDAO customerDAO;

        public CustomerService() {
            customerDAO = new CustomerDAO();
        }

        public void persist(Customer entity) {
            customerDAO.openCurrentSessionwithTransaction();
            customerDAO.persist(entity);
            customerDAO.closeCurrentSessionwithTransaction();
        }

        public void update(Customer entity) {
            customerDAO.openCurrentSessionwithTransaction();
            customerDAO.update(entity);
            customerDAO.closeCurrentSessionwithTransaction();
        }

        public Customer findById(Integer id) {
            customerDAO.openCurrentSession();
            Customer customer = customerDAO.findById(id);
            customerDAO.closeCurrentSession();
            return customer;
        }

        public void delete(Integer id) {
            customerDAO.openCurrentSessionwithTransaction();
            Customer customer = customerDAO.findById(id);
            customerDAO.delete(customer);
            customerDAO.closeCurrentSessionwithTransaction();
        }

        public List<Customer> findAll() {
            customerDAO.openCurrentSession();
            List<Customer> customerList = customerDAO.findAll();
            customerDAO.closeCurrentSession();
            return customerList;
        }

        public void deleteAll() {
            customerDAO.openCurrentSessionwithTransaction();
            customerDAO.deleteAll();
            customerDAO.closeCurrentSessionwithTransaction();
        }

        public CustomerDAO customerDAO() {
            return customerDAO;
        }
    }
