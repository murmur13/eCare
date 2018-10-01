package eCare;

import eCare.configuration.AppConfig;
import eCare.configuration.StandaloneMvcTestViewResolver;
import eCare.controllers.AppController;
import eCare.model.dao.CustomerDao;
import eCare.model.po.Customer;
import eCare.model.services.CustomerService;
import eCare.model.services.CustomerServiceImpl;
import eCare.model.services.UserProfileService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by echerkas on 22.09.2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
public class MyMvcControllerTest {

    private MockMvc mockMvc;

    @Autowired
    CustomerDao customerDao;

    @Mock
    private StandaloneMvcTestViewResolver viewResolver;

    @Autowired
    private WebApplicationContext wac;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private CustomerServiceImpl customerImpl;

    @Mock
    private CustomerService userService;

    @InjectMocks
    private AppController userController;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setViewResolvers(new StandaloneMvcTestViewResolver())
                .build();
    }

        @Test
        public void testListUsers () throws Exception {
            ResultMatcher ok = MockMvcResultMatchers.status().isOk();
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/list");
            this.mockMvc.perform(builder)
                    .andExpect(ok);
        }

    @Test
    public void testSearch () throws Exception {
        Customer user = new Customer("wayne", "1990-01-21", "selina666");
        List<Customer> userList = new ArrayList<Customer>();
        userList.add(user);
        when(userService.findByName(user.getName())).thenReturn(userList);
        List<Customer> userFound = userService.findByName(user.getName());
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher userFound2 = MockMvcResultMatchers.model()
                .attribute("users", userFound);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/search")
                .param("nameOrPhone", "selina666"))
                .andExpect(ok)
                .andExpect(userFound2);
    }
    }
