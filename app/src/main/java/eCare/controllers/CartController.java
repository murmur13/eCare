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
    private CartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String searchResults(Model model, HttpSession session) {
        String view = cartService.searchResults(model, session);
        return view;
    }

    @RequestMapping(value = "/cart/{featureId}/addToCart", method = RequestMethod.GET)
    public String addFeatureToCart(@PathVariable Integer featureId, Model model, HttpSession session) {
        String view = cartService.addFeatureToCart(featureId, model, session);
        return view;
    }

    @RequestMapping(value = "/cart/{tarifId}/addTarifToCart", method = RequestMethod.GET)
    public String addTarifToCart(@PathVariable Integer tarifId, Model model, HttpSession session) {
        String view = cartService.addTarifToCart(tarifId, model, session);
        return view;
    }

    @RequestMapping(value = "/cart/deleteTarif", method = RequestMethod.GET)
    public String deleteTarifFromCart(Model model, HttpSession session) {
        String view = cartService.deleteTarifFromCart(model, session);
        return view;
    }

    @RequestMapping(value = "/cart/{featureId}/deleteOption", method = RequestMethod.GET)
    public String deleteOptionFromCart(@PathVariable Integer featureId, Model model, HttpSession session) {
        String view = cartService.deleteOptionFromCart(featureId, model, session);
        return view;
    }

    @RequestMapping(value = "/cart/refresh", method = RequestMethod.GET)
    public String refreshCart(Model model, HttpSession session, Cart cart) {
        String view = cartService.refreshCart(model, session, cart);
        return view;
    }

    @RequestMapping(value = "/cart/submit", method = RequestMethod.GET)
    public String submitCartGet(Model model, HttpSession session, Cart cart, SessionStatus status) {
        String view = cartService.submitCartGet(model, session, cart, status);
        return view;
    }
}
