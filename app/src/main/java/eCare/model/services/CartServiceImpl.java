package eCare.model.services;

import eCare.model.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 29.06.2018.
 */
@Service("cartService")
@DependsOn("messageSource")
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private TarifService tarifService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private CustomerService userService;

    public void setOptionsInCart(Cart cart, Feature featureToAdd) {
        List<Feature> cartOptions = cart.getOptionsInCart();
        if (!cartOptions.contains(featureToAdd)) {
            cartOptions.add(featureToAdd);
            cart.setOptionsInCart(cartOptions);
        }
    }

    public Tarif addTarifToCart(Integer tarifId, Cart cart) {
        Tarif tarifToAdd = tarifService.findById(tarifId);
        cart.setTarifInCart(tarifToAdd);
        return tarifToAdd;
    }

    public Cart deleteOptionFromCart(Cart cart, Integer featureId) {
        Feature cartFeature = featureService.findById(featureId);
        List<Feature> cartFeatures = cart.getOptionsInCart();
        int index = cartFeatures.indexOf(cartFeature);
        cartFeatures.remove(index);
        cart.setOptionsInCart(cartFeatures);
        return cart;
    }

    public void submitTarif(Cart cart, Contract contract) {
        List<Feature> contractOptions = featureService.findFeatureByContract(contract.getContractId());
        if (!contractOptions.isEmpty()) {
            for (Feature feature : contractOptions) {
                List<Contract> featureContracts = feature.getFeatureContracts();
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
    }

    public List<Feature> submitOptions(List<Feature> submitOptions, Contract contract, List<Contract> contracts) {
        List<Tarif> tarifs = new ArrayList<Tarif>();
        List<Feature> contractOptions = featureService.findFeatureByContract(contract.getContractId());
        for (Feature feature : submitOptions) {
            contractOptions.add(feature);
            feature.setFeatureContracts(contracts);
            feature.setFeatureTarifs(tarifs);
            featureService.update(feature);
            contractService.update(contract);
        }
        return contractOptions;
    }

    public String searchResults(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            model.addAttribute("message", "Your cart is empty");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        }
        if (cart.getTarifInCart() == null && cart.getOptionsInCart().isEmpty()) {
            model.addAttribute("message", "Your cart is empty");
            model.addAttribute("loggedinuser", userService.getPrincipal());
            return "errorPage";
        } else {
            model.addAttribute("featuresInCart", cart.getOptionsInCart());
            model.addAttribute("tarifInCart", cart.getTarifInCart());
        }
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    public String addFeatureToCart(Integer featureId, Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        Customer user = userService.findBySSO(userService.getPrincipal());
        if (user.isBlockedByUser() || user.isBlockedByAdmin()) {
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Option cannot be chosen :(");
            return "errorPage";
        }
        Contract contract = user.getContract();
        List<Feature> contractFeatures = featureService.findFeatureByContract(contract.getContractId());
        Feature featureToAdd = featureService.findById(featureId);
        List<Feature> requiredFeatures = featureService.findAllRequiredFeatures();
        List<Feature> allBlockingFeatures = featureService.findAllBlockingFeatures();
        for (Feature feature : allBlockingFeatures) {
            if (feature.getBlockingFeatures().contains(featureToAdd) && contractFeatures.contains(feature)) {
                model.addAttribute("message", "You can't add option \"" + featureToAdd.getFeatureName() + "\" together with option \"" + feature.getFeatureName() + "\"");
                return "errorPage";
            }

        }
        for (Feature feature : requiredFeatures) {
            List<Feature> secondFeatureContainer = feature.getRequiredFeatures();
            if (secondFeatureContainer.contains(featureToAdd) && !contractFeatures.contains(feature)) {
                model.addAttribute("message", "You can't add option \"" + featureToAdd.getFeatureName() + "\" without adding option \"" + feature.getFeatureName() + "\" first");
                return "errorPage";
            }

        }
        setOptionsInCart(cart, featureToAdd);
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";

    }

    public String addTarifToCart(Integer tarifId, Model model, HttpSession session) {
        Cart cart;
        if (session.getAttribute("cart") == null) {
            cart = new Cart();
        } else {
            cart = (Cart) session.getAttribute("cart");
            session.setAttribute("cart", cart);
            model.addAttribute("loggedinuser", userService.getPrincipal());
        }

        Customer user = userService.findBySSO(userService.getPrincipal());
        if (user.isBlockedByUser() || user.isBlockedByAdmin()) {
            model.addAttribute("message", "User " + user.getSsoId() + " is blocked. Tarif cannot be chosen :(");
            return "errorPage";
        }
        Tarif tarifToAdd = addTarifToCart(tarifId, cart);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("tarifInCart", tarifToAdd);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    public String deleteTarifFromCart(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        cart.setTarifInCart(null);
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    public String deleteOptionFromCart(Integer featureId, Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        cart = deleteOptionFromCart(cart, featureId);
        if (cart.getOptionsInCart() == null) {
            cart.setOptionsInCart(null);
            session.setAttribute("cart", cart);
        }
        session.setAttribute("cart", cart);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    public String refreshCart(Model model, HttpSession session, Cart cart) {
        cart = (Cart) session.getAttribute("cart");
        List<Feature> cartFeatures = cart.getOptionsInCart();
        Tarif tarif = cart.getTarifInCart();
        model.addAttribute("tarifInCart", tarif);
        model.addAttribute("optionsInCart", cartFeatures);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        return "cart";
    }

    public String submitCartGet(Model model, HttpSession session, Cart cart, SessionStatus status) {
        Customer user = userService.findBySSO(userService.getPrincipal());
        List<Contract> contracts = contractService.findByCustomerId(user);
        Contract contract = user.getContract();
        cart = (Cart) session.getAttribute("cart");
        List<Feature> submitOptions = cart.getOptionsInCart();
        if (cart.getTarifInCart() != null) {
            submitTarif(cart, contract);
            model.addAttribute("features", null);
            model.addAttribute("contracts", contracts);
            model.addAttribute("optionsInCart", null);
        }
        if (cart.getOptionsInCart() != null && !cart.getOptionsInCart().isEmpty()) {
            List<Feature> contractOptions = submitOptions(submitOptions, contract, contracts);
            model.addAttribute("features", null);
            model.addAttribute("contracts", contracts);
            model.addAttribute("optionsInCart", null);
            model.addAttribute("userFeatures", contractOptions);
        }
        status.setComplete();
        if (submitOptions != null) {
            submitOptions.clear();
        }
        model.addAttribute("contracts", contracts);
        model.addAttribute("loggedinuser", userService.getPrincipal());
        if (user.getUserProfiles().contains(userProfileService.findByType("USER"))) {
            return "redirect:/contracts/myContract";
        } else return "redirect: /list";
    }
}
