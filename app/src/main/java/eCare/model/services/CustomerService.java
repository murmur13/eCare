package eCare.model.services;

import eCare.model.po.Customer;
import eCare.model.po.UserProfile;

import java.util.HashSet;
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

    boolean isCurrentAuthenticationAnonymous();

    String getPrincipal();

    HashSet<UserProfile> addUserProfile(UserProfile role);

}
