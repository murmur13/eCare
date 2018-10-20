package eCare.model.po;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 03.04.2018.
 */
@Component
@Scope("session")
public class Cart {

    private int id;
    private String name;
    private Double price;

    private List<Feature> optionsInCart = new ArrayList<Feature>();
    private Tarif tarifInCart;

    public Cart() {
    }

    public List<Feature> getOptionsInCart() {
        return optionsInCart;
    }

    public void setOptionsInCart(List<Feature> optionsInCart) {
        this.optionsInCart = optionsInCart;
    }

    public Tarif getTarifInCart() {
        return tarifInCart;
    }

    public void setTarifInCart(Tarif tarifInCart) {
        this.tarifInCart = tarifInCart;
    }

    public void addTarifToCart(Tarif tarif) {
        this.id = tarif.getTarifId();
        this.name = tarif.getName();
        this.price = tarif.getPrice();
    }

    public void addOptionToCart(Feature feature) {
        this.id = feature.getFeatureId();
        this.name = feature.getFeatureName();
        this.price = feature.getFeaturePrice();
    }

    private Double total(Cart cart) {
        Double total = new Double(0);
        List<Feature> featuresCart = cart.getOptionsInCart();
        Tarif tarifCart = cart.getTarifInCart();
        for (Feature featureCart : featuresCart) {
            Double price = featureCart.getFeaturePrice();
            Double connection = featureCart.getConnectionCost();
            total += price + connection;
        }
        Double tarifPrice = tarifCart.getPrice();
        total += tarifPrice;
        return total;
    }
}
