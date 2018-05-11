package eCare.model.PO;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="tarif_x_feature",
            joinColumns=@JoinColumn(name="feature_feature_id", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="tarif_tarif_id", referencedColumnName="tarif_id"))
    private List<Tarif> featureTarifs;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name="contract_has_feature",
            joinColumns=@JoinColumn(name="contractFeature", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="contractId", referencedColumnName="contract_id"))
    private List<Contract> featureContracts;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="feature_x_blocking_features",
            joinColumns=@JoinColumn(name="blocking_feature_id", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="feature_feature_id", referencedColumnName="feature_id"))
    private List<Feature> blockingFeatures;

    public List<Feature> getRequiredFeatures() {
        return requiredFeatures;
    }

    public void setRequiredFeatures(List<Feature> requiredFeatures) {
        this.requiredFeatures = requiredFeatures;
    }

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="feature_x_requiredfeature",
            joinColumns=@JoinColumn(name="featureId", referencedColumnName="feature_id"),
            inverseJoinColumns=@JoinColumn(name="requiredFeatureId", referencedColumnName="feature_id"))
    private List<Feature> requiredFeatures;

    public List<Feature> getBlockingFeatures() {
        return blockingFeatures;
    }

    public void setBlockingFeatures(List<Feature> blockingFeatures) {
        this.blockingFeatures = blockingFeatures;
    }

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

        return featureId == feature.featureId;

    }

    @Override
    public int hashCode() {
        return featureId;
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
