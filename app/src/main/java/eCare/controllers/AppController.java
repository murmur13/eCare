package eCare.controllers;

import eCare.model.po.Customer;
import eCare.model.po.UserProfile;
import eCare.model.services.CustomerService;
import eCare.model.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private CustomerService userService;

    @Autowired
    private UserProfileService userProfileService;

    Logger logger = LoggerFactory.getLogger(AppController.class);

    /**
     * This method will land us onto the main page.
     */
    @RequestMapping(value = {"/", "/mainPage"}, method = RequestMethod.GET)
    public String mainPage(ModelMap model) {
        String result = userService.mainPageMethod(model);
        return result;
    }

    /**
     * This method will list all existing users.
     */

    @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
    public String listUsers(@RequestParam(required = false) Integer page, ModelMap model) {
        String result = userService.listUsersMethod(page, model);
        return result;
    }

    /**
     * This method will find the user by name.
     */

    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public String search(ModelMap model) {
        String result = userService.searchUser(model);
        return result;
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public String findByNameOrTel(@RequestParam("nameOrPhone") String nameOrPhone, ModelMap model) {
        logger.info(nameOrPhone);
        String result = userService.findByNameOrTel(nameOrPhone, model);
        return result;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String newRegisteredUser(ModelMap model) {
        String result = userService.newRegisteredUser(model);
        return result;
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String registerNewUser(Customer user, BindingResult result, ModelMap model) {
        logger.info(user.toString());
        String view = userService.registerNewUser(user, result, model);
        return view;
    }

    /**
     * This method will provide the medium to add a new user.
     */
    @RequestMapping(value = {"/newuser"}, method = RequestMethod.GET)
    public String newUser(ModelMap model) {
        String view = userService.newUser(model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = {"/newuser"}, method = RequestMethod.POST)
    public String saveUser(@Valid Customer user, BindingResult result,
                           ModelMap model) {
        logger.info(user.toString());
        String view = userService.saveUser(user, result, model);
        return view;
    }


    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = {"/user/{ssoId}/edit"}, method = RequestMethod.GET)
    public String editUser(@PathVariable String ssoId, ModelMap model) {
        String view = userService.editUser(ssoId, model);
        return view;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = {"/user/{ssoId}/edit"}, method = RequestMethod.POST)
    public String updateUser(@Valid Customer user, BindingResult result,
                             ModelMap model, @PathVariable String ssoId) {
        logger.info(user.toString());
        String view = userService.updateUser(user, result, model, ssoId);
        return view;
    }


    /**
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = {"/user/{ssoId}/delete"}, method = RequestMethod.GET)
    public String deleteUser(@PathVariable String ssoId) {
        String view = userService.deleteUser(ssoId);
        return view;
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
        String view = userService.login();
        return view;
    }

    /**
     * This method handles logout requests.
     * Toggle the handlers if you are RememberMe functionality is useless in your app.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response, WebRequest webR, SessionStatus status) {
        String view = userService.logout(request, response, webR, status);
        return view;
    }

}
