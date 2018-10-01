package eCare;

import eCare.model.dao.CustomerDao;
import eCare.model.dao.FeatureDao;
import eCare.model.po.*;
import eCare.model.services.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

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

    private final static String role = "ADMIN";

    @Mock
    private FeatureDao featureDAO;

    @Mock
    private ContractService contractService;


    @InjectMocks
    private FeatureServiceImpl featureService;

    private ModelMap model;

    @InjectMocks
    private CustomerServiceImpl userService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(featureDAO);
        featureService = new FeatureServiceImpl();
        model = new ModelMap();
        userService = new CustomerServiceImpl();
    }

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

    @Test
    public void blockingFeaturesTestSelectedFeaturesOverheadsLimit(){
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        model.addAttribute("ModelNameTest","ModelValueTest");
        selectedFeatures.setSelectedFeatures(initializeFeatures(4));
        String result = featureService.blockingFeatures(selectedFeatures, null, model);
        assertEquals(result,"errorPage");
    }

    @Test
    public void blockingFeaturesTestCreateBlockingFeatures(){
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        model.addAttribute("ModelNameTest","ModelValueTest");
        selectedFeatures.setSelectedFeatures(initializeFeatures(2));
        BindingResult bindingResTest = Mockito.mock(BindingResult.class);
        when(bindingResTest.hasErrors()).thenReturn(false);
        String result = featureService.blockingFeatures(selectedFeatures, bindingResTest, model);
        assertEquals(result,"redirect:/features/blockingFeatures/seeAll");
    }

    @Test
    public void blockingFeaturesTestNullBlockingFeatures(){
        SelectedFeatures selectedFeatures = new SelectedFeatures();
        model.addAttribute("ModelNameTest","ModelValueTest");
        selectedFeatures.setSelectedFeatures(initializeFeatures(0));
        BindingResult bindingResTest = Mockito.mock(BindingResult.class);
        when(bindingResTest.hasErrors()).thenReturn(false);
        String result = featureService.blockingFeatures(selectedFeatures, bindingResTest, model);
        assertEquals(result,"errorPage");
    }

    @Test
    public void unblockFeaturesTest(){
        model.addAttribute("ModelNameTest","ModelValueTest");
        List<Feature> selectFeatures = new ArrayList<Feature>();
        selectFeatures.addAll(initializeFeatures(2));
        SelectedFeatures result = featureService.unblockFeatures(selectFeatures);
        assertTrue(result.getSelectedFeatures().containsAll(selectFeatures));
    }

    private List<Feature> initializeFeatures(int amountOfFeatures){
        List<Feature> selectedFeaturesList = new ArrayList<Feature>();
        for(int i = 0; i < amountOfFeatures; i++){
            Feature feature = new Feature();
            List<Feature> blockingFeatures = new ArrayList<Feature>();
            StringBuilder string = new StringBuilder();
            string.append("Feature_");
            string.append(String.valueOf(i));
            feature.setFeatureName(string.toString());
            feature.setBlockingFeatures(blockingFeatures);
            selectedFeaturesList.add(feature);
        }
        return selectedFeaturesList;
    }

}