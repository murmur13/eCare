package eCare.model.services;

import eCare.model.dao.CustomerDao;
import eCare.model.po.Customer;
import eCare.model.po.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by echerkas on 15.11.2017.
 */

@Service("customerService")
@DependsOn("messageSource")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

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
        if (entity != null) {
            entity.setSsoId(user.getSsoId());
            if (!user.getPassword().equals(entity.getPassword())) {
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
        return (user == null || ((id != null) && (user.getId() == id)));
    }

    public List<Customer> findByName(String name) {
        List<Customer> customers = customerDao.findByName(name);
        return customers;
    }

    public List<Customer> findByTelNumber(String telNumber) {
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
    public String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public HashSet<UserProfile> addUserProfile(UserProfile role) {
        HashSet<UserProfile> userProfiles = new HashSet<UserProfile>();
        userProfiles.add(role);
        return userProfiles;
    }

    public String mainPageMethod(ModelMap model) {
        String name = getPrincipal();
        Customer user = findBySSO(name);
        model.addAttribute("loggedinuser", getPrincipal());
        return "main";
    }

    public String listUsersMethod(Integer page, ModelMap model) {
        Customer user = findBySSO(getPrincipal());
        List<Customer> users = findAllUsers();
        PagedListHolder<Customer> pagedListHolder = new PagedListHolder<Customer>(users);
        pagedListHolder.setPageSize(15);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(0);
            model.addAttribute("users", pagedListHolder.getPageList());
        } else if (page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page - 1);
            model.addAttribute("users", pagedListHolder.getPageList());
        }

        model.addAttribute("loggedinuser", getPrincipal());
        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))) {
            return "main";
        } else return "userslist";
    }

    public String searchUser(ModelMap model) {
        Customer user = new Customer();
        model.addAttribute("user", user);
        model.addAttribute("loggedinuser", getPrincipal());
        return "search";
    }

    public String findByNameOrTel(String nameOrPhone, ModelMap model) {
        List<Customer> users = findByName(nameOrPhone);
        if (users.isEmpty()) {
            users = findByTelNumber(nameOrPhone);
            model.addAttribute("users", users);
            model.addAttribute("name", nameOrPhone);
            if (users.isEmpty()) {
                model.addAttribute("message", "User " + nameOrPhone + " is not found");
                return "errorPage";
            }
        } else {
            model.addAttribute("users", users);
            model.addAttribute("name", nameOrPhone);
        }
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
        return "userslist";
    }

    public String newRegisteredUser(ModelMap model) {
        Customer user = new Customer();
        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser");
        return "registration";
    }

    public String registerNewUser(Customer user, BindingResult result, ModelMap model) {
        UserProfile role = userProfileService.findByType("USER");
        HashSet<UserProfile> profiles = addUserProfile(role);
        user.setUserProfiles(profiles);
        if (result.hasErrors()) {
            return "registration";
        }
        if (!isUserSSOUnique(user.getId(), user.getSsoId()) || (user.getId() != null)) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }
        saveUser(user);
        model.addAttribute("message", "Please, log in with your new account!");
        model.addAttribute("loggedinuser", user.getSsoId());
        return "errorPage";
    }

    public String newUser(ModelMap model) {
        Customer user = new Customer();
        model.addAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser", getPrincipal());
        return "registration";
    }

    public String saveUser(Customer user, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "registration";
        }

        if (!isUserSSOUnique(user.getId(), user.getSsoId())) {
            FieldError ssoError = new FieldError("user", "ssoId", messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }

        saveUser(user);
        model.addAttribute("message", "User " + user.getName() + " " + user.getSurname() + " registered successfully");
        model.addAttribute("loggedinuser", getPrincipal());
        return "registrationsuccess";
    }

    public String editUser(String ssoId, ModelMap model) {
        Customer editUser = findBySSO(ssoId);
        model.addAttribute("editUser", editUser);
        model.addAttribute("edit", true);
        model.addAttribute("loggedinuser", getPrincipal());
        return "editUser";
    }

    public String updateUser(Customer user, BindingResult result, ModelMap model, String ssoId) {
        if (result.hasErrors()) {
            return "editUser";
        }
        updateUser(user);
        model.addAttribute("loggedinuser", getPrincipal());
        return "redirect: /list";
    }

    public String deleteUser(String ssoId) {
        deleteUserBySSO(ssoId);
        return "redirect:/list";
    }

    public String login() {
        if (isCurrentAuthenticationAnonymous()) {
            return "login";
        } else {
            return "redirect:/mainPage";
        }
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, WebRequest webR, SessionStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            status.setComplete();
            webR.removeAttribute("user", webR.SCOPE_SESSION);
            persistentTokenBasedRememberMeServices.logout(request, response, auth);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login?logout";
    }
}
