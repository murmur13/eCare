package eCare.model.services;

import eCare.model.dao.CustomerDao;
import eCare.model.po.Customer;
import eCare.model.po.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * Created by echerkas on 15.11.2017.
 */

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationTrustResolver authenticationTrustResolver;

    public Customer findById(Integer id) {
        return customerDao.findById(id);
    }

    public Customer findBySSO(String sso) {
        return customerDao.findBySSO(sso);
    }

    public void saveUser(Customer user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        customerDao.save(user);
    }

    public void updateUser(Customer user) {
        Customer entity = customerDao.findById(user.getId());
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
        customerDao.deleteBySSO(sso);
    }

    public List<Customer> findAllUsers() {
        return customerDao.findAllUsers();
    }

    public boolean isUserSSOUnique(Integer id, String sso) {
        Customer user = findBySSO(sso);
        return ( user == null || ((id != null) && (user.getId() == id)));
    }

    public List<Customer> findByName(String name) {
        List<Customer> customers = customerDao.findByName(name);
        return customers;
    }

    public List<Customer> findByTelNumber(String telNumber){
        List<Customer> customers = customerDao.findByTelNumber(telNumber);
        return customers;
    }

    /**
     * This method returns true if users is already authenticated [logged-in], else false.
     */
    public boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    /**
     * This method returns the principal[user-name] of logged-in user.
     */
    public String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public HashSet<UserProfile> addUserProfile(UserProfile role){
        HashSet <UserProfile> userProfiles = new HashSet<UserProfile>();
        userProfiles.add(role);
        return userProfiles;
    }
}
