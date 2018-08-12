package eCare.model.po;

import java.util.List;

/**
 * Created by echerkas on 29.03.2018.
 */
public class SelectedFeatures {

    private List<Feature> selectedFeatures;

    public SelectedFeatures(){}

    public List<Feature> getSelectedFeatures(){
        return selectedFeatures;
    }

    public void setSelectedFeatures(List<Feature> selectedFeatures){
        this.selectedFeatures = selectedFeatures;
    }

    @Override
    public String toString() {
        return "SelectedFeatures{" +
                "selectedFeatures=" + selectedFeatures +
                '}';
    }
}
