package eCare.model.services;

import eCare.model.DAO.SecondCustomerDAO;
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
public class SecondCustomerServiceImpl implements SecondCustomerService{

    @Autowired
    private SecondCustomerDAO secondCustomerDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer findById(Integer id) {
        return secondCustomerDAO.findById(id);
    }

    public Customer findBySSO(String sso) {
        Customer user = secondCustomerDAO.findBySSO(sso);
        return user;
    }

    public void saveUser(Customer user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        secondCustomerDAO.save(user);
    }

    /*
     * Since the method is running with Transaction, No need to call hibernate update explicitly.
     * Just fetch the entity from db and update it with proper values within transaction.
     * It will be updated in db once transaction ends.
     */
    public void updateUser(Customer user) {
        Customer entity = secondCustomerDAO.findById(user.getId());
        if(entity!=null){
            entity.setSsoId(user.getSsoId());
            if(!user.getPassword().equals(entity.getPassword())){
                entity.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            entity.setName(user.getName());
            entity.setSurname(user.getSurname());
            entity.setMail(user.getMail());
            entity.setUserProfiles(user.getUserProfiles());
        }
    }


    public void deleteUserBySSO(String sso) {
        secondCustomerDAO.deleteBySSO(sso);
    }

    public List<Customer> findAllUsers() {
        return secondCustomerDAO.findAllUsers();
    }

    public boolean isUserSSOUnique(Integer id, String sso) {
        Customer user = findBySSO(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }

    public List<Customer> findByName(String name) {
        List<Customer> customers = secondCustomerDAO.findByName(name);
        return customers;
    }
}
