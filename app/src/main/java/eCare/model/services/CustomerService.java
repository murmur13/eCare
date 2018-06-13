package eCare.model.services;

import eCare.model.PO.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */

public interface CustomerService {

    Customer findById(Integer id);

    Customer findBySSO(String sso);

    void saveUser(Customer customer);

    void updateUser(Customer customer);

    void deleteUserBySSO(String sso);

    List<Customer> findAllUsers();

    boolean isUserSSOUnique(Integer id, String sso);

    List<Customer> findByName(String name);

    List<Customer> findByTelNumber(String telNumber);

}
