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
        if(session.getAttribute("cart")==null){
            Cart cart = new Cart();
            model.addAttribute("cart", cart);
        }
        else{
            Cart cart = (Cart) session.getAttribute("cart");
            model.addAttribute("featuresInCart",cart.getOptionsInCart());
            model.addAttribute("tarifInCart",cart.getTarifInCart());
        }
        model.addAttribute("loggedinuser", getPrincipal());
        return "cart";
    }

    @RequestMapping(value = "/cart/{featureId}/addToCart", method = RequestMethod.POST)
    public String addFeatureToCart(@PathVariable Integer featureId, Model model, HttpSession session){
            Feature featureToAdd = featureService.findById(featureId);
            List<Feature> cartOptions = new ArrayList<Feature>();
            cartOptions.add(featureToAdd);
            session.setAttribute("cart", cartOptions);
            model.addAttribute("loggedinuser", getPrincipal());
            return "cart";
    }

    @RequestMapping(value = "/cart/{tarifId}/addToCart", method = RequestMethod.POST)
    public String addTarifToCart(@PathVariable Integer tarifId, Model model, HttpSession session){
        Tarif tarifToAdd = tarifService.findById(tarifId);
        session.setAttribute("cart", tarifToAdd);
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
        Tarif submitTarif = cart.getTarifInCart();
        contract.setTarif(submitTarif);
        List<Feature> contractOptions = featureService.findFeatureByContract(contract.getContractId());
        for (Feature feature:submitOptions) {
            contractOptions.add(feature);
            feature.setFeatureContracts(contracts);
            feature.setFeatureTarifs(tarifs);
            featureService.update(feature);
        }
        contractService.update(contract);
        tarifService.update(submitTarif);
        status.setComplete();
        cart = null;
        model.addAttribute("contracts", contracts);
        model.addAttribute("userFeatures", contractOptions);
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
