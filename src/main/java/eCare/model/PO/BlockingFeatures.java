package eCare.model.PO;

import javax.persistence.*;
import java.util.List;

/**
 * Created by echerkas on 29.10.2017.
 */

@Entity
@Table(name = "blocking_features")
public class BlockingFeatures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int blockingFeatureId;

    @Column(name = "blocking_name")
    private String featureName;

    @ManyToMany
    @JoinTable(
            name="feature_x_blocking_features",
            joinColumns=@JoinColumn(name="blocking_feature_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="feature_feature_id", referencedColumnName="feature_id"))
    private List<Feature> blockingFeatures;

    public List<Feature> getBlockingFeatures() {
        return blockingFeatures;
    }

    public void setBlockingFeatures(List<Feature> blockingFeatures) {
        this.blockingFeatures = blockingFeatures;
    }

    public BlockingFeatures(){

    }

    public BlockingFeatures(String featureName) {
        this.featureName = featureName;
    }

    public int getBlockingFeatureId() {
        return blockingFeatureId;
    }

    public void setBlockingFeatureId(int blockingFeatureId) {
        this.blockingFeatureId = blockingFeatureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String toString() {
        return "BlockingFeatures{" +
                "blockingFeatureId=" + blockingFeatureId +
                ", featureName='" + featureName + '\'' +
                '}';
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
