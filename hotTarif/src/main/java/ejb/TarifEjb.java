package ejb;

import eCare.configuration.MessageSender;
import eCare.model.dao.TarifDao;
import eCare.model.po.Tarif;
import eCare.model.services.TarifServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by echerkas on 12.06.2018.
 */
@Singleton(name = "tarifEjb")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TarifEjb {

    public TarifEjb() {
    }

    @PostConstruct
    public void test() {
        System.out.println(applicationContext);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TarifServiceImpl tarifService;

    @Autowired
    private TarifDao TarifDao;

    public List<Tarif> getCheapTarifs(){
        List<Tarif> allTarifs = tarifService.findAll();
        List<Tarif> cheapTarifs;

        if (allTarifs.size() > 0) {
            Collections.sort(allTarifs, new Comparator<Tarif>() {
                public int compare(final Tarif object1, final Tarif object2) {
                    return object1.getPrice().compareTo(object2.getPrice());
                }
            });
        }
        return allTarifs;
    }

    public TarifServiceImpl getTarifService() {
        return tarifService;
    }

    public TarifDao getTarifDao() {
        return TarifDao;
    }

}
