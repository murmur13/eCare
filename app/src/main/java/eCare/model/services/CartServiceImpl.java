package eCare.model.services;

import eCare.model.PO.Cart;
import eCare.model.PO.Contract;
import eCare.model.PO.Feature;
import eCare.model.PO.Tarif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by echerkas on 29.06.2018.
 */
@Service("cartService")
@Transactional
public class CartServiceImpl implements CartService{

    @Autowired
    TarifService tarifService;

    @Autowired
    FeatureService featureService;

    @Autowired
    ContractService contractService;

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
}
