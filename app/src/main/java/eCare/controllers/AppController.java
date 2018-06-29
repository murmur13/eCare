package eCare.controllers;

import eCare.model.PO.Customer;
import eCare.model.PO.UserProfile;
import eCare.model.services.CustomerService;
import eCare.model.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

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
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
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


    /**
     * This method will land us onto the main page.
     */
    @RequestMapping(value = { "/", "/mainPage" }, method = RequestMethod.GET)
    public String mainPage(ModelMap model, HttpServletRequest request){
        String name = userService.getPrincipal();
        Customer user = userService.findBySSO(name);
        request.getSession().setAttribute("user", user);
        model.addAttribute("loggedinuser", userService.getPrincipal());
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

        model.addAttribute("loggedinuser", userService.getPrincipal());
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
        model.addAttribute("loggedinuser", userService.getPrincipal());
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
        model.addAttribute("loggedinuser", userService.getPrincipal());
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
        HashSet<UserProfile> profiles = userService.addUserProfile(role);
        user.setUserProfiles(profiles);
            if (result.hasErrors()) {
                return "registration";
            }
            if(!userService.isUserSSOUnique(user.getId(), user.getSsoId()) || (user.getId()!=null)){
                FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
                result.addError(ssoError);
                return "registration";
            }
        userService.saveUser(user);
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
            model.addAttribute("loggedinuser", userService.getPrincipal());
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
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "registrationsuccess";
        }


        /**
         * This method will provide the medium to update an existing user.
         */
        @RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
        public String editUser(@PathVariable String ssoId, ModelMap model) {
            Customer editUser = userService.findBySSO(ssoId);
            model.addAttribute("editUser", editUser);
            model.addAttribute("edit", true);
            model.addAttribute("loggedinuser", userService.getPrincipal());
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
            userService.updateUser(user);
            model.addAttribute("loggedinuser", userService.getPrincipal());
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
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "accessDenied";
        }

        /**
         * This method handles login GET requests.
         * If users is already logged-in and tries to goto login page again, will be redirected to main page.
         */
        @RequestMapping(value = "/login", method = RequestMethod.GET)
        public String loginPage() {
            if (userService.isCurrentAuthenticationAnonymous()) {
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

    }
