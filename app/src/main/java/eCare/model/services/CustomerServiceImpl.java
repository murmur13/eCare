package eCare.model.services;

import eCare.model.DAO.CustomerDAO;
import eCare.model.PO.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer findById(Integer id) {
        return customerDAO.findById(id);
    }

    public Customer findBySSO(String sso) {
        return customerDAO.findBySSO(sso);
    }

    public void saveUser(Customer user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        customerDAO.save(user);
    }

    /*
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     */
    public void updateUser(Customer user) {
        Customer entity = customerDAO.findById(user.getId());
        if(entity!=null){
            entity.setSsoId(user.getSsoId());
            if(!user.getPassword().equals(entity.getPassword())){
                entity.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            entity.setName(user.getName());
            entity.setSurname(user.getSurname());
            entity.setTelNumber(user.getTelNumber());
            entity.setBirthDate(user.getBirthDate());
            entity.setPassportData(user.getPassportData());
            entity.setAddress(user.getAddress());
            entity.setMail(user.getMail());
            entity.setUserProfiles(user.getUserProfiles());
            entity.setBlockedByUser(user.isBlockedByUser());
            entity.setBlockedByAdmin(user.isBlockedByAdmin());
        }
    }


    public void deleteUserBySSO(String sso) {
        customerDAO.deleteBySSO(sso);
    }

    public List<Customer> findAllUsers() {
        return customerDAO.findAllUsers();
    }

    public boolean isUserSSOUnique(Integer id, String sso) {
        Customer user = findBySSO(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }

    public List<Customer> findByName(String name) {
        List<Customer> customers = customerDAO.findByName(name);
        return customers;
    }

    public List<Customer> findByTelNumber(String telNumber){
        List<Customer> customers = customerDAO.findByTelNumber(telNumber);
        return customers;
    }
}
