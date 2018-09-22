package eCare.model.services;

import eCare.model.po.Cart;
import eCare.model.po.Contract;
import eCare.model.po.Feature;
import eCare.model.po.Tarif;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by echerkas on 29.06.2018.
 */
public interface CartService {

    void setOptionsInCart(Cart cart, Feature featureToAdd);

    Tarif addTarifToCart(Integer tarifId, Cart cart);

    Cart deleteOptionFromCart(Cart cart, Integer featureId);

    void submitTarif(Cart cart, Contract contract);

    List<Feature> submitOptions(List<Feature> submitOptions, Contract contract, List<Contract> contracts);

    String searchResults(Model model, HttpSession session);

    String addFeatureToCart(Integer featureId, Model model, HttpSession session);

    String addTarifToCart(Integer tarifId, Model model, HttpSession session);

    String deleteTarifFromCart(Model model, HttpSession session);

    String deleteOptionFromCart(Integer featureId, Model model, HttpSession session);

    String refreshCart(Model model, HttpSession session, Cart cart);

    String submitCartGet(Model model, HttpSession session, Cart cart, SessionStatus status);
}
