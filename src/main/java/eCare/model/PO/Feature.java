package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 24.10.2017.
 */

@Entity
@Table(name = "feature")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int featureId;

    @Column(name = "name")
    private String featureName;

    @Column(name = "price")
    private double featurePrice;

    @Column(name = "connection_cost")
    private double connectionCost;

    @ManyToMany
    @JoinTable(
            name="tarif_x_feature",
            joinColumns=@JoinColumn(name="feature_feature_id", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="tarif_tarif_id", referencedColumnName="tarif_id"))
    private List<Tarif> featureTarifs;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="contract_has_feature",
            joinColumns=@JoinColumn(name="contractFeature", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="contractId", referencedColumnName="contract_id"))
    private List<Contract> featureContracts;

    @Override
    public String toString() {
        return "feature{" +
                "featureId=" + featureId +
                ", featureName='" + featureName + '\'' +
                ", featurePrice=" + featurePrice +
                ", connectionCost=" + connectionCost +
                '}';
    }

    public List<Contract> getFeatureContracts() {
        return featureContracts;
    }

    public void setFeatureContracts(List<Contract> featureContracts) {
        this.featureContracts = featureContracts;
    }

    public Feature() {
    }

    public Feature(String featureName, double featurePrice, double connectionCost) {
        this.featureName = featureName;
        this.featurePrice = featurePrice;
        this.connectionCost = connectionCost;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public double getFeaturePrice() {
        return featurePrice;
    }

    public void setFeaturePrice(double featurePrice) {
        this.featurePrice = featurePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feature feature = (Feature) o;

        if (featureId != feature.featureId) return false;
        if (Double.compare(feature.featurePrice, featurePrice) != 0) return false;
        if (Double.compare(feature.connectionCost, connectionCost) != 0) return false;
        return featureName.equals(feature.featureName);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = featureId;
        result = 31 * result + featureName.hashCode();
        temp = Double.doubleToLongBits(featurePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(connectionCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public double getConnectionCost() {
        return connectionCost;
    }

    public void setConnectionCost(double connectionCost) {
        this.connectionCost = connectionCost;
    }

    public List<Tarif> getFeatureTarifs() {
        return featureTarifs;
    }

    public void setFeatureTarifs(List<Tarif> featureTarifs) {
        this.featureTarifs = featureTarifs;
    }
}
