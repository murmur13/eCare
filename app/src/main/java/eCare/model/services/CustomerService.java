package eCare.model.services;

import eCare.model.po.Customer;
import eCare.model.po.UserProfile;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    String mainPageMethod(ModelMap model);

    String listUsersMethod(Integer page, ModelMap model);

    String searchUser(ModelMap model);

    String findByNameOrTel(String nameOrPhone, ModelMap model);

    String newRegisteredUser(ModelMap model);

    String registerNewUser(Customer user, BindingResult result, ModelMap model);

    String newUser(ModelMap model);

    String saveUser(Customer user, BindingResult result, ModelMap model);

    String editUser(String ssoId, ModelMap model);

    String updateUser(Customer user, BindingResult result, ModelMap model, String ssoId);

    String deleteUser(String ssoId);

    String login();

    String logout (HttpServletRequest request, HttpServletResponse response, WebRequest webR, SessionStatus status);

}
