package eCare;

import eCare.model.DAO.FeatureDAO;
import eCare.model.PO.Feature;
import eCare.model.services.FeatureServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by echerkas on 13.05.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class FeatureServiceImplTest {

    @Mock
    private FeatureDAO featureDAO;

    @InjectMocks
    private FeatureServiceImpl featureService;

//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        FeatureServiceImpl featureService1 = new FeatureServiceImpl();
//    }

    @Test
    public void shouldFindById() throws Exception {
        Feature feature = new Feature();
        feature.setFeatureId(666);
        featureService.persist(feature);
        when(featureService.findById(666)).thenReturn(feature);
        Feature returnedFeature = featureService.findById(666);
        assertThat(featureService.findById(666), is(feature));
        assertEquals("Failed because features aren't equal", returnedFeature, feature);
    }


    @Test
    public void shouldntFindById() throws Exception {
        Feature feature = new Feature();
        feature.setFeatureId(666);
        featureService.persist(feature);
        Feature feature1 = new Feature();
        feature1.setFeatureId(13);
        featureService.persist(feature1);
        when(featureService.findById(666)).thenReturn(feature1);
        assertNotEquals("Different objects are the same..", feature, feature1);
    }

    @Test
    public void findByIdWithWrongId() throws Exception {
        when(featureService.findById(0)).thenReturn(null);
        Feature feature = featureService.findById(0);
        assertNull("Feature with non-existent id is found", feature);
    }

    @Test
    public void shouldFindAll() throws Exception {
        Feature feature = new Feature();
        feature.setFeatureName("feature");
        featureService.persist(feature);
        Feature feature1 = new Feature();
        feature1.setFeatureName("feature1");
        featureService.persist(feature1);
        List<Feature> list = new ArrayList<Feature>();
        list.add(feature);
        list.add(feature1);
        when(featureService.findAll()).thenReturn(list);
        List<Feature> features = featureService.findAll();
        assertEquals("Failed because not all features were found", features, list);
    }

}