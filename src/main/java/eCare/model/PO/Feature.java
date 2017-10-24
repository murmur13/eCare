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

    @Override
    public String toString() {
        return "feature{" +
                "featureId=" + featureId +
                ", featureName='" + featureName + '\'' +
                ", featurePrice=" + featurePrice +
                ", connectionCost=" + connectionCost +
                '}';
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
