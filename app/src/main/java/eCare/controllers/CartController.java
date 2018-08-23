package eCare.controllers;

import eCare.model.po.*;
import eCare.model.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by echerkas on 03.04.2018.
 */
@Controller
@Scope("prototype")
public class CartController {

    @Autowired
    private Cart cart;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CustomerService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private CartService cartService;

    @RequestMapping(value="/cart", method= RequestMethod.GET)
    public String searchResults(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart.getTarifInCart()==null && cart.getOptionsInCart().isEmpty()){
            model.addAttribute("message", "Your cart is empty");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        else{
            model.addAttribute("featuresInCart",cart.getOptionsInCart());
            model.addAttribute("tarifInCart",cart.getTarifInCart());
        }
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/{featureId}/addToCart", method = RequestMethod.GET)
    public String addFeatureToCart(@PathVariable Integer featureId, Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        Customer user = userService.findBySSO(userService.getPrincipal());
        if(user.isBlockedByUser() || user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Option cannot be chosen :(");
            return "errorPage";
        }
        Contract contract = user.getContract();
        List<Feature> contractFeatures = featureService.findFeatureByContract(contract.getContractId());
        Feature featureToAdd = featureService.findById(featureId);
        List<Feature> requiredFeatures = featureService.findAllRequiredFeatures();
        List<Feature> allBlockingFeatures = featureService.findAllBlockingFeatures();
        for (Feature feature: allBlockingFeatures) {
            if(feature.getBlockingFeatures().contains(featureToAdd) && contractFeatures.contains(feature)){
                model.addAttribute("message", "You can't add option \"" + featureToAdd.getFeatureName() + "\" together with option \"" + feature.getFeatureName() + "\"");
                return "errorPage";
            }

        }
        for (Feature feature: requiredFeatures) {
            List<Feature> secondFeatureContainer = feature.getRequiredFeatures();
            if(secondFeatureContainer.contains(featureToAdd) && !contractFeatures.contains(feature)){
                model.addAttribute("message", "You can't add option \"" + featureToAdd.getFeatureName() + "\" without adding option \"" + feature.getFeatureName() + "\" first");
                return "errorPage";
            }

        }
        cartService.setOptionsInCart(cart, featureToAdd);
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/{tarifId}/addTarifToCart", method = RequestMethod.GET)
    public String addTarifToCart(@PathVariable Integer tarifId, Model model, HttpSession session) {
        Cart cart;
        if (session.getAttribute("cart") == null) {
            cart = new Cart();
        }
        else{
            cart = (Cart) session.getAttribute("cart");
            session.setAttribute("cart", cart);
            model.addAttribute("loggedinuser", userService.getPrincipal());
            }

        Customer user = userService.findBySSO(userService.getPrincipal());
        if(user.isBlockedByUser() || user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Tarif cannot be chosen :(");
            return "errorPage";
        }
        Tarif tarifToAdd = cartService.addTarifToCart(tarifId, cart);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
        }

    @RequestMapping(value = "/cart/deleteTarif", method = RequestMethod.GET)
    public String deleteTarifFromCart(Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setTarifInCart(null);
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/deleteOption-{featureId}", method = RequestMethod.GET)
    public String deleteOptionFromCart(@PathVariable Integer featureId, Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        cart = cartService.deleteOptionFromCart(cart, featureId);
        if(cart.getOptionsInCart() == null){
            cart.setOptionsInCart(null);
            session.setAttribute("cart", cart);
        }
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/refresh", method = RequestMethod.GET)
    public String refreshCart(Model model, HttpSession session, Cart cart){
        cart = (Cart) session.getAttribute("cart");
        List<Feature> cartFeatures = cart.getOptionsInCart();
        Tarif tarif = cart.getTarifInCart();
        model.addAttribute("tarifInCart", tarif);
        model.addAttribute("optionsInCart", cartFeatures);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/submit", method = RequestMethod.GET)
    public String submitCartGet(Model model, HttpSession session, Cart cart, SessionStatus status){
        Customer user = userService.findBySSO(userService.getPrincipal());
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = user.getContract();
        cart = (Cart) session.getAttribute("cart");
        List<Feature> submitOptions = cart.getOptionsInCart();
        if(cart.getTarifInCart()!=null) {
            cartService.submitTarif(cart, contract);
            model.addAttribute("features", null);
            model.addAttribute("contracts", contracts);
            model.addAttribute("optionsInCart", null);
        }
        if(cart.getOptionsInCart() !=null && !cart.getOptionsInCart().isEmpty()) {
            List<Feature> contractOptions = cartService.submitOptions(submitOptions, contract, contracts);
                model.addAttribute("features", null);
                model.addAttribute("contracts", contracts);
                model.addAttribute("optionsInCart", null);
                model.addAttribute("userFeatures", contractOptions);
            }
        status.setComplete();
        if(submitOptions != null) {
            submitOptions.clear();
        }
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))){
            return "userContract";
        } else return "userslist";
    }
}
