package eCare.converter;

import eCare.model.PO.Feature;
import eCare.model.services.FeatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by echerkas on 31.03.2018.
 */

@Component
public class StringToFeatureConverter implements Converter<String, Feature> {

    static final Logger logger = LoggerFactory.getLogger(StringToFeatureConverter.class);

    @Autowired
    FeatureService featureService;

    public StringToFeatureConverter(FeatureService featureService) {
        this.featureService = featureService;
    }

    @Override
    public Feature convert(String string) {
        if (string == null) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(String.class), string, null);
        }
        System.out.println("string = " + string);
        Integer id = Integer.valueOf(string);
        Feature feature = featureService.findById(id);
        return feature;
    }
}
