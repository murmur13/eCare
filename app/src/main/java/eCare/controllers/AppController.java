package eCare.controllers;

import eCare.model.PO.Customer;
import eCare.model.PO.UserProfile;
import eCare.model.PO.UserProfileType;
import eCare.model.services.CustomerService;
import eCare.model.services.UserProfileService;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by echerkas on 15.11.2017.
 */

@Controller
@RequestMapping("/")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@SessionAttributes(value = {"roles", "user"})
public class AppController {

        @Autowired
        CustomerService userService;

        @Autowired
        UserProfileService userProfileService;

        @Autowired
        MessageSource messageSource;

        @Autowired
        PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

        @Autowired
        AuthenticationTrustResolver authenticationTrustResolver;


    /**
     * This method will land us onto the main page.
     */
    @RequestMapping(value = { "/", "/mainPage" }, method = RequestMethod.GET)
    public String mainPage(ModelMap model, HttpSession session, HttpServletRequest request){
//    Customer sessionAttribute  = (Customer) session.getAttribute("user");
        String name = getPrincipal();
        Customer user = userService.findBySSO(name);
        request.getSession().setAttribute("user", user);
//        session.setAttribute();
        model.addAttribute("loggedinuser", getPrincipal());
        return "main";
    }

    /**
     * This method will list all existing users.
     */

    @RequestMapping(value = { "/list" }, method = RequestMethod.GET)
    public String listUsers(@RequestParam(required = false) Integer page, ModelMap model, HttpSession session) {
        Customer user = (Customer) session.getAttribute("user");
        List<Customer> users = userService.findAllUsers();

        PagedListHolder<Customer> pagedListHolder = new PagedListHolder<Customer>(users);
        pagedListHolder.setPageSize(15);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());
        model.addAttribute("page", page);
        if(page == null || page < 1 || page > pagedListHolder.getPageCount()){
            pagedListHolder.setPage(0);
            model.addAttribute("users", pagedListHolder.getPageList());
        }
        else if(page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page-1);
            model.addAttribute("users", pagedListHolder.getPageList());
        }

        model.addAttribute("loggedinuser", getPrincipal());
        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))){
            return "main";
        } else return "userslist";

    }


    /**
     * This method will find the user by name.
     */

    @RequestMapping(value = {"/search" }, method = RequestMethod.GET)
    public String search(ModelMap model) {
        Customer user = new Customer();
        model.addAttribute("user", user);
        model.addAttribute("loggedinuser", getPrincipal());
        return "search";
    }

    @RequestMapping(value = { "/search" }, method = RequestMethod.POST)
    public String findByNameOrTel(@RequestParam ("nameOrPhone") String nameOrPhone, ModelMap model) {
            List<Customer> users = userService.findByName(nameOrPhone);
        if(users.isEmpty()){
            users = userService.findByTelNumber(nameOrPhone);
            model.addAttribute("users", users);
            model.addAttribute("name", nameOrPhone);
            if(users.isEmpty()){
                model.addAttribute("message", "User " + nameOrPhone + " is not found");
                return "errorPage";
            }
        }
        else {
            model.addAttribute("users", users);
            model.addAttribute("name", nameOrPhone);
        }
        model.addAttribute("edit", false);

//        if (result.hasErrors()) {
//            return "error";
//        }

        model.addAttribute("loggedinuser", getPrincipal());
        return "userslist";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String newRegisteredUser(HttpSession session, ModelMap model) {
        Customer user = new Customer();
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        model.addAttribute("edit", false);
        model.addAttribute("loggedinuser");
        return "registration";
    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerNewUser(HttpSession session, Customer user, BindingResult result, ModelMap model) {
        UserProfile role = userProfileService.findByType("USER");
        HashSet <UserProfile> userProfiles = new HashSet<UserProfile>();
        userProfiles.add(role);
        user.setUserProfiles(userProfiles);
            if (result.hasErrors()) {
                return "registration";
            }
            if(!userService.isUserSSOUnique(user.getId(), user.getSsoId()) || (user.getId()!=null)){
                FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
                result.addError(ssoError);
                return "registration";
            }
        userService.saveUser(user);
//        model.addAttribute("user", user);
        session.setAttribute("user", user);
        model.addAttribute("message", "Please, log in with your new account!");
        model.addAttribute("loggedinuser", user.getSsoId());
        return "errorPage";
    }

        /**
         * This method will provide the medium to add a new user.
         */
        @RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
        public String newUser(ModelMap model) {
            Customer user = new Customer();
            model.addAttribute("user", user);
            model.addAttribute("edit", false);
            model.addAttribute("loggedinuser", getPrincipal());
            return "registration";
        }

        /**
         * This method will be called on form submission, handling POST request for
         * saving user in database. It also validates the user input
         */
        @RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
        public String saveUser(@Valid Customer user, BindingResult result,
                               ModelMap model) {

            if (result.hasErrors()) {
                return "registration";
            }

            if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
                FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
                result.addError(ssoError);
                return "registration";
            }

            userService.saveUser(user);
            model.addAttribute("message", "User " + user.getName() + " "+ user.getSurname() + " registered successfully");
            model.addAttribute("loggedinuser", getPrincipal());
            return "registrationsuccess";
//            return "redirect: /mainPage";
        }


        /**
         * This method will provide the medium to update an existing user.
         */
        @RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
        public String editUser(@PathVariable String ssoId, ModelMap model) {
            Customer editUser = userService.findBySSO(ssoId);
            model.addAttribute("editUser", editUser);
            model.addAttribute("edit", true);
            model.addAttribute("loggedinuser", getPrincipal());
            return "editUser";
        }

        /**
         * This method will be called on form submission, handling POST request for
         * updating user in database. It also validates the user input
         */
        @RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
        public String updateUser(@Valid Customer user, BindingResult result,
                                 ModelMap model, @PathVariable String ssoId) {

            if (result.hasErrors()) {
                return "editUser";
            }

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


            userService.updateUser(user);
//            model.addAttribute("message", "User " + user.getName() + " "+ user.getSurname() + " updated successfully");
            model.addAttribute("loggedinuser", getPrincipal());
            return "redirect: /list";
        }


        /**
         * This method will delete an user by it's SSOID value.
         */
        @RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
        public String deleteUser(@PathVariable String ssoId) {
            userService.deleteUserBySSO(ssoId);
            return "redirect:/list";
        }


        /**
         * This method will provide UserProfile list to views
         */
        @ModelAttribute("roles")
        public List<UserProfile> initializeProfiles() {
            return userProfileService.findAll();
        }

        /**
         * This method handles Access-Denied redirect.
         */
        @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
        public String accessDeniedPage(ModelMap model) {
            model.addAttribute("loggedinuser", getPrincipal());
            return "accessDenied";
        }

        /**
         * This method handles login GET requests.
         * If users is already logged-in and tries to goto login page again, will be redirected to main page.
         */
        @RequestMapping(value = "/login", method = RequestMethod.GET)
        public String loginPage() {
            if (isCurrentAuthenticationAnonymous()) {
                return "login";
            } else {
                return "redirect:/mainPage";
            }
        }

        /**
         * This method handles logout requests.
         * Toggle the handlers if you are RememberMe functionality is useless in your app.
         */
        @RequestMapping(value="/logout", method = RequestMethod.GET)
        public String logoutPage (HttpServletRequest request, HttpServletResponse response, WebRequest webR, SessionStatus status){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
                status.setComplete();
                webR.removeAttribute("user", webR.SCOPE_SESSION);
                //new SecurityContextLogoutHandler().logout(request, response, auth);
                persistentTokenBasedRememberMeServices.logout(request, response, auth);
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            return "redirect:/login?logout";
        }

        /**
         * This method returns the principal[user-name] of logged-in user.
         */
        private String getPrincipal(){
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            } else {
                userName = principal.toString();
            }
            return userName;
        }

        /**
         * This method returns true if users is already authenticated [logged-in], else false.
         */
        private boolean isCurrentAuthenticationAnonymous() {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authenticationTrustResolver.isAnonymous(authentication);
        }

    }
