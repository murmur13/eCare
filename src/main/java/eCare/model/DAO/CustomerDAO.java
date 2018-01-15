package eCare.model.DAO;

import eCare.model.PO.Customer;

import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */
public interface CustomerDAO {

    Customer findById(int id);

    Customer findBySSO(String sso);

    void save(Customer user);

    void deleteBySSO(String sso);

    List<Customer> findAllUsers();

    List<Customer> findByName(String name);

    List<Customer> findByTelNumber(String telNumber);
}
