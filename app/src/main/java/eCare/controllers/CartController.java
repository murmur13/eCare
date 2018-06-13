package eCare.controllers;

import eCare.model.PO.*;
import eCare.model.services.ContractService;
import eCare.model.services.FeatureService;
import eCare.model.services.TarifService;
import eCare.model.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 03.04.2018.
 */
@Controller
@Scope("session")
public class CartController {

    @Autowired
    private Cart cart;

    @Autowired
    FeatureService featureService;

    @Autowired
    TarifService tarifService;

    @Autowired
    ContractService contractService;

    @Autowired
    UserProfileService userProfileService;

    @RequestMapping(value="/cart", method= RequestMethod.GET)
    public String searchResults(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart.getTarifInCart()==null && cart.getOptionsInCart().isEmpty()){
//            model.addAttribute("cart", cart);
            model.addAttribute("message", "Your cart is empty");
            model.addAttribute("loggedinuser", getPrincipal());
            return "errorPage";
        }
        else{
            model.addAttribute("featuresInCart",cart.getOptionsInCart());
            model.addAttribute("tarifInCart",cart.getTarifInCart());
        }
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/{featureId}/addToCart", method = RequestMethod.GET)
    public String addFeatureToCart(@PathVariable Integer featureId, Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        Customer user = (Customer) session.getAttribute("user");
        if(user.isBlockedByUser() || user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Option cannot be chosen :(");
            return "errorPage";
        }
        Contract contract = user.getContracts().get(0);
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
        List<Feature> cartOptions = cart.getOptionsInCart();
            if (!cartOptions.contains(featureToAdd)){
                cartOptions.add(featureToAdd);
                cart.setOptionsInCart(cartOptions);
            }
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", getPrincipal());
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
            model.addAttribute("loggedinuser", getPrincipal());
            }

        Customer user = (Customer) session.getAttribute("user");
        if(user.isBlockedByUser() || user.isBlockedByAdmin()){
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Tarif cannot be chosen :(");
            return "errorPage";
        }
        Tarif tarifToAdd = tarifService.findById(tarifId);
        cart.setTarifInCart(tarifToAdd);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
        }

    @RequestMapping(value = "/cart/deleteTarif", method = RequestMethod.GET)
    public String deleteTarifFromCart(Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setTarifInCart(null);
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/deleteOption-{featureId}", method = RequestMethod.GET)
    public String deleteOptionFromCart(@PathVariable Integer featureId, Model model, HttpSession session){
        Cart cart = (Cart) session.getAttribute("cart");
        Feature cartFeature = featureService.findById(featureId);
        List<Feature> cartFeatures = cart.getOptionsInCart();
        int index = cartFeatures.indexOf(cartFeature);
        cartFeatures.remove(index);
        cart.setOptionsInCart(cartFeatures);
        if(cart.getOptionsInCart() == null){
            cart.setOptionsInCart(null);
            session.setAttribute("cart", cart);
        }
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/refresh", method = RequestMethod.GET)
    public String refreshCart(Model model, HttpSession session, Cart cart){
        cart = (Cart) session.getAttribute("cart");
        List<Feature> cartFeatures = cart.getOptionsInCart();
        Tarif tarif = cart.getTarifInCart();
        model.addAttribute("tarifInCart", tarif);
        model.addAttribute("optionsInCart", cartFeatures);
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/submit", method = RequestMethod.GET)
    public String submitCartGet(Model model, HttpSession session, Cart cart, SessionStatus status){
        Customer user = (Customer) session.getAttribute("user");
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = contracts.get(0);
        cart = (Cart) session.getAttribute("cart");
        List<Tarif> tarifs = new ArrayList<Tarif>();
        List<Feature> submitOptions = cart.getOptionsInCart();
        if(cart.getTarifInCart()!=null) {
            List<Feature> contractOptions = featureService.findFeatureByContract(contract.getContractId());
            if(!contractOptions.isEmpty()){
                for (Feature feature:contractOptions) {
                    List <Contract> featureContracts = feature.getFeatureContracts();
                    int index = featureContracts.indexOf(contract);
                    Contract featureContract = featureContracts.get(index);
                    featureContracts.remove(index);
                    featureService.update(feature);
                    contractService.update(featureContract);
                }
            }
            Tarif submitTarif = cart.getTarifInCart();
            contract.setTarif(submitTarif);
            contractOptions.clear();
            contractService.update(contract);
            tarifService.update(submitTarif);
            cart.setTarifInCart(null);
            model.addAttribute("features", null);
            model.addAttribute("contracts", contracts);
            model.addAttribute("optionsInCart", null);
        }
        if(cart.getOptionsInCart() !=null && !cart.getOptionsInCart().isEmpty()) {
            for (Feature feature : submitOptions) {
                List<Feature> contractOptions = featureService.findFeatureByContract(contract.getContractId());
                contractOptions.add(feature);
                feature.setFeatureContracts(contracts);
                feature.setFeatureTarifs(tarifs);
                featureService.update(feature);
                model.addAttribute("features", null);
                model.addAttribute("contracts", contracts);
                model.addAttribute("optionsInCart", null);
                model.addAttribute("userFeatures", contractOptions);
                contractService.update(contract);
            }
        }
        status.setComplete();
        if(submitOptions != null) {
            submitOptions.clear();
        }
        model.addAttribute("contracts", contracts);
//        model.addAttribute("tarif",submitTarif );
        model.addAttribute("loggedinuser", getPrincipal());
        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))){
            return "userContract";
        } else return "userslist";
    }

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


}
