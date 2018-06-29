package eCare.model.services;

import eCare.model.PO.Cart;
import eCare.model.PO.Contract;
import eCare.model.PO.Feature;
import eCare.model.PO.Tarif;

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
}
